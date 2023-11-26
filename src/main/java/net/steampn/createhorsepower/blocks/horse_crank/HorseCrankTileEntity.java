package net.steampn.createhorsepower.blocks.horse_crank;

import com.mojang.logging.LogUtils;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.steampn.createhorsepower.config.Config;
import net.steampn.createhorsepower.entities.CHPLeashKnotEntity;
import net.steampn.createhorsepower.utils.BlockRegister;
import org.slf4j.Logger;

import java.util.List;

import static net.steampn.createhorsepower.utils.CHPProperties.*;

public class HorseCrankTileEntity extends GeneratingKineticBlockEntity {
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
    private static final Logger LOGGER = LogUtils.getLogger();

    public HorseCrankTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    //Rotation Speed NOT STRESS UNIT
    @Override
    public float getGeneratedSpeed() {
        BlockState state = getBlockState();

        if (!BlockRegister.HORSE_CRANK.has(getBlockState())) {
            return 0;
        }

        if (state.getValue(WORKER_SMALL_STATE)) {
            generatedSpeed = SMALL_CREATURE_SPEED;
        } else if (state.getValue(WORKER_MEDIUM_STATE)) {
            generatedSpeed = MEDIUM_CREATURE_SPEED;
        } else if (state.getValue(WORKER_LARGE_STATE)) {
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
        BlockState state = getBlockState();

        if (state.getValue(WORKER_SMALL_STATE)) {
            capacity = SMALL_CREATURE_STRESS / 4.0f;
        } else if (state.getValue(WORKER_MEDIUM_STATE)) {
            capacity = MEDIUM_CREATURE_STRESS / 8.0f;
        } else if (state.getValue(WORKER_LARGE_STATE)) {
            capacity = LARGE_CREATURE_STRESS / 16.0f;
        } else {
            capacity = 0;
        }

        this.lastCapacityProvided = capacity;
        return capacity;
    }

    @Override
    protected AABB createRenderBoundingBox() {
        return new AABB(this.getBlockPos()).inflate(2);
    }

    @Override
    public void tick() {
        super.tick();
        BlockState state = this.getBlockState();
        Level level = this.getLevel();
        BlockPos pos = this.getBlockPos();
        HorseCrankTileEntity crankTileEntity = this;

        //Update animation based on stats
        if (getGeneratedSpeed() != 0 && getSpeed() == 0)
            updateGeneratedRotation();
        else if (getGeneratedSpeed() == 0){
            updateGeneratedRotation();
        }

        //If world does not exist
        if (level == null)
            return;

        if (level.getBlockState(pos).getValue(HAS_WORKER)) {
            if (!level.isClientSide()) {
                Mob workerEntity = getWorker(level, pos);
                if(workerEntity == null || !workerEntity.isAlive() || !workerEntity.isLeashed()) {
                    LOGGER.info("Worker is not available or alive or leashed. Resetting state.");
                    level.setBlock(pos, state.setValue(HAS_WORKER, false).setValue(WORKER_LARGE_STATE, false)
                            .setValue(WORKER_MEDIUM_STATE, false).setValue(WORKER_SMALL_STATE, false), 3);
                }
            }
        }

        crankTileEntity.setChanged();
    }

    private Mob getWorker(Level world, BlockPos pos) {
        List<Mob> mobs = world.getEntitiesOfClass(Mob.class, new AABB(pos).inflate(3.0D));
        LOGGER.info("Mobs in TE range: " + mobs);
        for(Mob mob : mobs) {
            LOGGER.info(mob + " is leashed?: " + (mob.isLeashed() ? "True" : "False"));
            if(mob.isLeashed()
                    && mob.getLeashHolder() != null
                    && mob.getLeashHolder() instanceof CHPLeashKnotEntity
                    && ((CHPLeashKnotEntity) mob.getLeashHolder()).getPos().equals(pos)) {
                LOGGER.info("(Tile Entity) Worker found: " + mob);
                return mob;
            }
        }
        LOGGER.info("(Tile Entity) No worker found at pos: " + pos);
        return null;
    }
}
