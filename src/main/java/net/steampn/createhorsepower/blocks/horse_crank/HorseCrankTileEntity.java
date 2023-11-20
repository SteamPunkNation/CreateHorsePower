package net.steampn.createhorsepower.blocks.horse_crank;

import com.mojang.logging.LogUtils;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.steampn.createhorsepower.config.Config;
import net.steampn.createhorsepower.utils.BlockRegister;
import org.slf4j.Logger;

import java.util.List;

import static net.steampn.createhorsepower.utils.CHPProperties.*;
import static net.steampn.createhorsepower.utils.CHPProperties.WORKER_LARGE_STATE;
import static net.steampn.createhorsepower.utils.CHPTags.Entities.*;

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
    public Boolean hasWorker = false;

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
        AABB aabb = new AABB(pos).inflate(3.0D);

        //Update animation based on stats
        if (getGeneratedSpeed() != 0 && getSpeed() == 0)
            updateGeneratedRotation();
        else if (getGeneratedSpeed() == 0){
            updateGeneratedRotation();
        }

        //If world does not exist
        if (level == null)
            return;

        //Default state when worker is true and has one of the worker types true
        if (hasWorker && (state.getValue(WORKER_SMALL_STATE) || state.getValue(WORKER_MEDIUM_STATE) || state.getValue(WORKER_LARGE_STATE)))
            setDefaultStates(state, level, pos, crankTileEntity);

        //Check for mob (worker)
        for(Mob mob : level.getEntitiesOfClass(Mob.class, aabb)){

            if (!mob.isLeashed() || hasWorker || this.hasLeashKnot(level, aabb))
                continue;

            List<TagKey<EntityType<?>>> worker = mob.getType().getTags().toList();

            if (worker.contains(SMALL_WORKER_TAG)) enableOnlySmallWorker(state, level, pos, crankTileEntity);
            else if (worker.contains(MEDIUM_WORKER_TAG)) enableOnlyMediumWorker(state, level, pos, crankTileEntity);
            else if (worker.contains(LARGE_WORKER_TAG)) enableOnlyLargeWorker(state, level, pos, crankTileEntity);
        }
        crankTileEntity.setChanged();
    }

    public Boolean hasLeashKnot(Level level, AABB aabb){
        return level.getEntitiesOfClass(LeashFenceKnotEntity.class, aabb).isEmpty();
    }

    public void setDefaultStates(BlockState state, Level level, BlockPos pos, HorseCrankTileEntity te){
        level.setBlock(pos, state
                .setValue(WORKER_SMALL_STATE, false)
                .setValue(WORKER_MEDIUM_STATE, false)
                .setValue(WORKER_LARGE_STATE, false),
                3);
        LOGGER.info("Default state!");
        te.setChanged();
        hasWorker = false;
    }

    public void enableOnlySmallWorker(BlockState state, Level level, BlockPos pos, HorseCrankTileEntity te){
        //If horse crank has worker and worker state is true, ignore
        if (hasWorker && state.getValue(WORKER_SMALL_STATE))
            return;

        level.setBlock(pos, state
                        .setValue(WORKER_SMALL_STATE, true)
                        .setValue(WORKER_MEDIUM_STATE, false)
                        .setValue(WORKER_LARGE_STATE, false),
                3);
        LOGGER.info("Small worker attached!");
        te.setChanged();
        hasWorker = true;
    }
    public void enableOnlyMediumWorker(BlockState state, Level level, BlockPos pos, HorseCrankTileEntity te){
        //If horse crank has worker and worker state is true, ignore
        if (hasWorker && state.getValue(WORKER_MEDIUM_STATE))
            return;

        level.setBlock(pos, state
                        .setValue(WORKER_SMALL_STATE, false)
                        .setValue(WORKER_MEDIUM_STATE, true)
                        .setValue(WORKER_LARGE_STATE, false),
                3);
        LOGGER.info("Medium worker attached!");
        te.setChanged();
        hasWorker = true;
    }
    public void enableOnlyLargeWorker(BlockState state, Level level, BlockPos pos, HorseCrankTileEntity te){
        //If horse crank has worker and worker state is true, ignore
        if (hasWorker && state.getValue(WORKER_LARGE_STATE))
            return;

        level.setBlock(pos, state
                        .setValue(WORKER_SMALL_STATE, false)
                        .setValue(WORKER_MEDIUM_STATE, false)
                        .setValue(WORKER_LARGE_STATE, true),
                3);
        LOGGER.info("Large worker attached!");
        te.setChanged();
        hasWorker = true;
    }
}
