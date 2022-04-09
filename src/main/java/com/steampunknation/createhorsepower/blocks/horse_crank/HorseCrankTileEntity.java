package com.steampunknation.createhorsepower.blocks.horse_crank;

import com.google.common.collect.Lists;
import com.simibubi.create.content.contraptions.base.GeneratingKineticTileEntity;
import com.simibubi.create.foundation.gui.widgets.InterpolatedChasingValue;
import com.steampunknation.createhorsepower.config.Config;
import com.steampunknation.createhorsepower.utils.BlockRegister;
import com.steampunknation.createhorsepower.utils.CHPTags;
import com.steampunknation.createhorsepower.utils.CHPUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

import java.util.List;
import java.util.UUID;


public class HorseCrankTileEntity extends GeneratingKineticTileEntity {
    //Config variables
    private static final Integer
            SMALL_CREATURE_SPEED = Config.SMALL_CREATURE_SPEED.get(),
            SMALL_CREATURE_STRESS = Config.SMALL_CREATURE_STRESS.get(),
            MEDIUM_CREATURE_SPEED = Config.MEDIUM_CREATURE_SPEED.get(),
            MEDIUM_CREATURE_STRESS = Config.MEDIUM_CREATURE_STRESS.get(),
            LARGE_CREATURE_SPEED = Config.LARGE_CREATURE_SPEED.get(),
            LARGE_CREATURE_STRESS = Config.LARGE_CREATURE_STRESS.get();


    //Create power variables
    private float generatedSpeed;
    private boolean smallWorker = false;
    private boolean mediumWorker = false;
    private boolean largeWorker = false;

    //Horse Power Variables
    protected static double[][] walkPath = { { -1, -1 }, { 0, -1 }, { 0.75, -1 }, { 0.75, 0 }, { 0.75, 0.75 }, { 0, 0.75 }, { -1, 0.75 }, { -0.75, 0 } };
    protected static double[][] searchPath = { { -1, -1 }, { 0, -1 }, { 1, -1 }, { 1, 0 }, { 1, 1 }, { 0, 1 }, { -1, 1 }, { -1, 0 } };
    public AxisAlignedBB[] searchAreas = new AxisAlignedBB[8];
    protected List<BlockPos> searchPos = null;
    protected int origin = -1;
    protected int target = origin;

    protected boolean hasWorker = false;
    protected CreatureEntity worker;
    protected CompoundNBT nbtWorker;

    protected boolean valid = false;
    protected int validationTimer = 0;
    protected int locateHorseTimer = 0;
    protected boolean running = true;
    protected boolean wasRunning = false;

    //Client
    InterpolatedChasingValue visualSpeed = new InterpolatedChasingValue();
    float angle;

    public HorseCrankTileEntity(TileEntityType<? extends HorseCrankTileEntity> type) {
        super(type);
        }

    /*
    TODO Move preset stress unit values and rotation speed to a config file.
    TODO Speed should be based on entity attached (Ex: Wolf [4 rpm] -> Cow [8 rpm] -> Horse [16 rpm])
    TODO Stress should be based on entity size (Ex: Wolf [16 stress] -> Cow [32 stress] -> Horse [64 stress])
    */

    //Rotation Speed NOT STRESS UNIT
    @Override
    public float getGeneratedSpeed() {
        if (!BlockRegister.HORSE_CRANK.has(getBlockState())) {
            return 0;
        }

        if (smallWorker) {
            generatedSpeed = SMALL_CREATURE_SPEED;
        } else if (mediumWorker) {
            generatedSpeed = MEDIUM_CREATURE_SPEED;
        } else if (largeWorker) {
            generatedSpeed = LARGE_CREATURE_SPEED;
        } else{
            generatedSpeed = 0;
        }

        return generatedSpeed;
    }

    //Stress Unit NOT ROTATION SPEED
    @Override
    public float calculateAddedStressCapacity() {
        float capacity;

        if (smallWorker) {
            capacity = SMALL_CREATURE_STRESS / 4.0f;
        } else if (mediumWorker) {
            capacity = MEDIUM_CREATURE_STRESS / 8.0f;
        } else if (largeWorker) {
            capacity = LARGE_CREATURE_STRESS / 16.0f;
        } else {
            capacity = 0;
        }

        this.lastCapacityProvided = capacity;
        return capacity;
    }

    @Override
    protected AxisAlignedBB makeRenderBoundingBox() {
        return new AxisAlignedBB(worldPosition).inflate(1);
    }

    @Override
    public void tick() {
        super.tick();

        if (level.isClientSide) {
            float targetSpeed = isVirtual() ? speed : getGeneratedSpeed();
            visualSpeed.target(targetSpeed);
            visualSpeed.tick();
            angle += visualSpeed.value * 3 / 10f;
            angle %= 360;
            return;
        }

        if (getGeneratedSpeed() != 0 && getSpeed() == 0)
            updateGeneratedRotation();
        else if (getGeneratedSpeed() == 0){
            updateGeneratedRotation();
        }

        if (hasWorker() && CHPTags.Entities.LARGE_WORKER.contains(worker.getType())){
            smallWorker = false;
            mediumWorker = false;
            largeWorker = true;
        }
        else if (hasWorker() && CHPTags.Entities.MEDIUM_WORKER.contains(worker.getType())){
            smallWorker = false;
            mediumWorker = true;
            largeWorker = false;
        }
        else if (hasWorker() && CHPTags.Entities.SMALL_WORKER.contains(worker.getType())){
            smallWorker = true;
            mediumWorker = false;
            largeWorker = false;
        }
        else {
            smallWorker = false;
            mediumWorker = false;
            largeWorker = false;
        }

        //Horse Power Reference
        validationTimer--;
        if (validationTimer <= 0) {
            valid = validateArea();
            if (valid)
                validationTimer = 220;
            else
                validationTimer = 60;
        }
        boolean flag = false;

        if (!hasWorker())
            locateHorseTimer--;
        if (!hasWorker() && nbtWorker != null && locateHorseTimer <= 0) {
            flag = findWorker();
        }
        if (locateHorseTimer <= 0)
            locateHorseTimer = 120;
        if (getWorld() != null && !getWorld().isClientSide && valid) {
            if (!running) {
                running = true;
            } else if (running) {
                running = false;
            }

            if (running != wasRunning) {
                target = getClosestTarget();
                wasRunning = running;
            }

            if (hasWorker()) {
                if (running) {

                    Vector3d pos = getPathPosition(target, false);
                    double x = pos.x;
                    double y = pos.y;
                    double z = pos.z;

                    if (searchAreas[target] == null)
                        searchAreas[target] = new AxisAlignedBB(x - 0.5D, y - 1.0D, z - 0.5D, x + 1.5D, y + 1.0D, z + 1.5D);

                    if (worker.getBoundingBox().intersects(searchAreas[target])) {
                        int next = target + 1;
                        int previous = target - 1;
                        if (next >= walkPath.length)
                            next = 0;
                        if (previous < 0)
                            previous = walkPath.length - 1;

                        target = next;
                    }

                    if (worker instanceof AbstractHorseEntity) {
                        AbstractHorseEntity horse = (AbstractHorseEntity) this.worker;
                        if (horse.isEating())
                            horse.setEating(false);
                        if (horse.isStanding())
                            horse.setStanding(false);
                    }

                    PathNavigator navigator = worker.getNavigation();
                    if (target != -1 && navigator.isStuck()) {
                        pos = getPathPosition(target, true);

                        Path path = navigator.createPath(new BlockPos(pos), 0);
                        navigator.moveTo(path, 1D);
                    }
                }
            }
        }

        if (flag) {
            setChanged();
        }
    }

    //Horse Power Reference

    //Read
    @Override
    protected void fromTag(BlockState state, CompoundNBT compound, boolean clientPacket) {
        super.fromTag(state, compound, clientPacket);

        target = compound.getInt("target");
        origin = compound.getInt("origin");
        hasWorker = compound.getBoolean("hasWorker");

        if (hasWorker && compound.contains("leash", 10)) {
            nbtWorker = compound.getCompound("leash");
            findWorker();
        }
    }

    @Override
    public void writeSafe(CompoundNBT compound, boolean clientPacket) {
        compound.putInt("target", target);
        compound.putInt("origin", origin);
        compound.putBoolean("hasWorker", hasWorker);

        if (this.worker != null) {
            if (nbtWorker == null) {
                CompoundNBT nbtTagCompound = new CompoundNBT();
                UUID uuid = worker.getUUID();
                nbtTagCompound.putUUID("UUID", uuid);
                nbtWorker = nbtTagCompound;
            }

            compound.put("leash", nbtWorker);
        }

        super.writeSafe(compound, clientPacket);
    }

    private boolean findWorker() {
        UUID uuid = nbtWorker.getUUID("UUID");
        if (level != null) {
            AxisAlignedBB aabb = new AxisAlignedBB(worldPosition).inflate(7.0D);
            Entity entity = CHPUtils.getEntityWithinArea(level, aabb, e -> e.getUUID().equals(uuid));

            if (entity != null) {
                setWorker((CreatureEntity) entity);
                return true;
            }
        }
        return false;
    }

    public void setWorkerToPlayer(PlayerEntity player) {
        if (hasWorker() && worker.canBeLeashed(player)) {
            hasWorker = false;
            worker.hasRestriction();
            worker.setLeashedTo(player, true);
            worker = null;
            nbtWorker = null;
        }
    }

    public boolean hasWorker() {
        if (worker != null && worker.isAlive() && !worker.isLeashed() && worker.distanceToSqr(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ()) < 45) {
            return true;
        } else {
            if (worker != null) {
                worker = null;
                nbtWorker = null;
                if (!getWorld().isClientSide)
                    InventoryHelper.dropItemStack(level, worldPosition.getX(), worldPosition.getY() + 1, worldPosition.getZ(), new ItemStack(Items.LEAD));
            }
            hasWorker = false;
            return false;
        }
    }

    public CreatureEntity getWorker() {
        return worker;
    }

    public void setWorker(CreatureEntity newWorker) {
        hasWorker = true;
        worker = newWorker;
        worker.restrictTo(worldPosition, 3);
        target = getClosestTarget();
        if (worker != null) {
            CompoundNBT nbtTagCompound = new CompoundNBT();
            UUID uuid = worker.getUUID();
            nbtTagCompound.putUUID("UUID", uuid);
            nbtWorker = nbtTagCompound;
        }
        setChanged();
    }

    private Vector3d getPathPosition(int i, boolean nav) {
        double x = worldPosition.getX() + (nav ? walkPath: searchPath)[i][0] * (nav ? 3: 2.5);
        double y = worldPosition.getY() - 1;
        double z = worldPosition.getZ() + (nav ? walkPath: searchPath)[i][1] * (nav ? 3: 2.5);
        return new Vector3d(x, y, z);
    }

    protected int getClosestTarget() {
        if (hasWorker()) {
            double dist = Double.MAX_VALUE;
            int closest = 0;

            for (int i = 0; i < walkPath.length; i++) {
                Vector3d pos = getPathPosition(i, false);

                double tmp = pos.distanceTo(worker.position());
                if (tmp < dist) {
                    dist = tmp;
                    closest = i;
                }
            }

            return closest;
        }
        return 0;
    }

    public boolean validateArea() {
        if (searchPos == null) {
            searchPos = Lists.newArrayList();

            for (int x = -3; x <= 3; x++) {
                for (int z = -3; z <= 3; z++) {
                    if ((x <= 1 && x >= -1) && (z <= 1 && z >= -1))
                        continue;
                    searchPos.add(getBlockPos().offset(x, 0, z));
                    searchPos.add(getBlockPos().offset(x, -1, z));
                }
            }
        }

        for (BlockPos pos : searchPos) {
            if (!getWorld().getBlockState(pos).getMaterial().isReplaceable())
                return false;
        }
        return true;
    }
}
