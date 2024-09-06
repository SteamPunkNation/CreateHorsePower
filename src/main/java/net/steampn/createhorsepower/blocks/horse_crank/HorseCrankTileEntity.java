package net.steampn.createhorsepower.blocks.horse_crank;

import com.mojang.logging.LogUtils;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.steampn.createhorsepower.config.Config;
import net.steampn.createhorsepower.registry.BlockRegister;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static net.steampn.createhorsepower.blocks.horse_crank.HorseCrankBlock.*;

public class HorseCrankTileEntity extends GeneratingKineticBlockEntity {
    private static final Logger LOGGER = LogUtils.getLogger();
    public boolean hasValidWorkingBlocks = false;
    private float rpmModifier = 0.0f;

    public HorseCrankTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public float getGeneratedSpeed() {
        float generatedSpeed;
        BlockState state = getBlockState();

        if(!BlockRegister.HORSE_CRANK.has(getBlockState())) return 0;

        if(!state.getValue(HAS_WORKER)) return 0;

        if(!hasValidWorkingBlocks) return 0;

        generatedSpeed = 4 * rpmModifier;

        return generatedSpeed;
    }

    @Override
    public float calculateAddedStressCapacity() {
        float capacity;
        BlockState state = getBlockState();

        if(!BlockRegister.HORSE_CRANK.has(getBlockState())) return 0;

        if(!state.getValue(HAS_WORKER)) return 0;

        if(state.getValue(SMALL_WORKER_STATE)) capacity = Config.small_creature_stress / getSpeed();
        else if(state.getValue(MEDIUM_WORKER_STATE)) capacity = Config.medium_creature_stress / getSpeed();
        else if(state.getValue(LARGE_WORKER_STATE)) capacity = Config.large_creature_stress / getSpeed();
        else capacity = 0;

        this.lastCapacityProvided = capacity;
        return Math.abs(capacity);
    }

    @Override
    protected AABB createRenderBoundingBox() {
        return new AABB(this.getBlockPos()).inflate(2);
    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
    }

    @Override
    public void tick() {
        super.tick();
        if (level == null || level.isClientSide()) return;

        Block[] blockTypeGrid = getValidSurroundingPathBlocks();
        Set<Block> blockSet = surroundingValidBlocksSet(blockTypeGrid);

        // If blocks in set are not all valid, disable block
        if (blockSet.contains(null)) hasValidWorkingBlocks = false;
        // If all valid blocks are the same
        else adjustRPMModifier(blockSet);

        moveWorkerTo(getWorkerNearCrank());

        updateAnimation();
    }

    private Mob getMob(List<Mob> mobsNearCrank){
        Stream<Mob> mobsAttachedToCrank = mobsNearCrank.stream()
                .filter(mob -> mob.isLeashed() &&  mob.getLeashHolder() instanceof LeashFenceKnotEntity);
        List<Mob> mobs = mobsAttachedToCrank.toList();
        return mobs.isEmpty() ? null : mobs.get(0);
    }

    private Mob getWorkerNearCrank(){
        List<Mob> mobsNearCrank = level.getEntitiesOfClass(Mob.class, new AABB(this.getBlockPos()).inflate(7.0D));
        return getMob(mobsNearCrank);
    }

    private void moveWorkerTo(Mob worker){
        if (worker == null) return;

        if(worker instanceof Horse && ((Horse) worker).isEating()){
            ((Horse) worker).setEating(false);
        }

        double radius = 3.5f;
        int ticksPerRotation = 20 * 10;

        BlockPos pos = this.getBlockPos();
        double bx = pos.getX();
        double by = pos.getY();
        double bz = pos.getZ();

        double distanceToWorker = worker.distanceToSqr(pos.getCenter());

        if (distanceToWorker <= (radius * radius) + 0.5){
            double progress = (worker.level().getGameTime() % ticksPerRotation) / (double) ticksPerRotation;

            double angle = 2 * Math.PI * progress;
            double xOffset = radius * Math.sin(angle);
            double zOffset = radius * Math.cos(angle);

            worker.getNavigation().moveTo(bx + xOffset, by, bz + zOffset, 1D);
        }
    }

    private void adjustRPMModifier(Set<Block> blockSet){
        hasValidWorkingBlocks = true;
        // If all blocks in set are valid but different, apply rpm to the lowest value
        if (blockSet.stream().anyMatch(Config.poor_path::contains)){
            rpmModifier = 0.5f;
        }
        else if (blockSet.stream().anyMatch(Config.normal_path::contains)){
            rpmModifier = 1.0f;
        }
        else if (blockSet.stream().anyMatch(Config.great_path::contains)){
            rpmModifier = 2.0f;
        }
    }

    private Set<Block> surroundingValidBlocksSet(Block[] surroundingBlocks){
        return new HashSet<>(Arrays.asList(surroundingBlocks));
    }

    private Block[] getValidSurroundingPathBlocks(){
        BlockPos pos = this.getBlockPos();
        Block[] blockTypeGrid = new Block[48];
        int gridCount = 0;

        for (int z = pos.getZ() - 3; z <= pos.getZ() + 3; z++){
            for (int x = pos.getX() - 3; x <= pos.getX() + 3; x++){
                if(z == pos.getZ() && x == pos.getX()) continue;
                BlockPos targetPos = new BlockPos(x, pos.getY() - 1, z);

                BlockState targetedBlock = level.getBlockState(targetPos);

                if(Stream.of(Config.poor_path, Config.normal_path, Config.great_path)
                        .anyMatch(path -> path.contains(targetedBlock.getBlock()))){
                    blockTypeGrid[gridCount] = targetedBlock.getBlock();
                }
                gridCount++;
            }
        }
        return blockTypeGrid;
    }

    private void updateAnimation(){
        if (getGeneratedSpeed() == 0) updateGeneratedRotation();

        if (getGeneratedSpeed() < 0 || getSpeed() < 0){
            setSpeed(-1 * getSpeed());
        }
        updateGeneratedRotation();
    }
}
