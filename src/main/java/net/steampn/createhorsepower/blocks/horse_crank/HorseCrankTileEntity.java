package net.steampn.createhorsepower.blocks.horse_crank;

import com.mojang.logging.LogUtils;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.steampn.createhorsepower.config.Config;
import net.steampn.createhorsepower.utils.BlockRegister;
import org.slf4j.Logger;

import static net.steampn.createhorsepower.utils.CHPProperties.*;
import static net.steampn.createhorsepower.utils.CHPUtils.getWorker;
import static net.steampn.createhorsepower.utils.CHPUtils.updateBlockStateBasedOnMob;

public class HorseCrankTileEntity extends GeneratingKineticBlockEntity {
    //Config variables
    private static final Integer
            SMALL_CREATURE_SPEED = Config.SMALL_CREATURE_SPEED.get(),
            SMALL_CREATURE_STRESS = Config.SMALL_CREATURE_STRESS.get(),
            MEDIUM_CREATURE_SPEED = Config.MEDIUM_CREATURE_SPEED.get(),
            MEDIUM_CREATURE_STRESS = Config.MEDIUM_CREATURE_STRESS.get(),
            LARGE_CREATURE_SPEED = Config.LARGE_CREATURE_SPEED.get(),
            LARGE_CREATURE_STRESS = Config.LARGE_CREATURE_STRESS.get();

    public Boolean
            smallWorkerState = this.getBlockState().hasProperty(WORKER_SMALL_STATE) ? this.getBlockState().getValue(WORKER_SMALL_STATE) : false,
            mediumWorkerState = this.getBlockState().hasProperty(WORKER_MEDIUM_STATE) ? this.getBlockState().getValue(WORKER_MEDIUM_STATE) : false,
            largeWorkerState = this.getBlockState().hasProperty(WORKER_LARGE_STATE) ? this.getBlockState().getValue(WORKER_LARGE_STATE) : false,
            hasWorker = this.getBlockState().hasProperty(HAS_WORKER) ? this.getBlockState().getValue(HAS_WORKER) : false;

    private static final Logger LOGGER = LogUtils.getLogger();

    public HorseCrankTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    //Rotation Speed NOT STRESS UNIT
    @Override
    public float getGeneratedSpeed() {
        float generatedSpeed;
        BlockState state = getBlockState();

        if (!BlockRegister.HORSE_CRANK.has(getBlockState())) return 0;

        if (state.getValue(WORKER_SMALL_STATE))  generatedSpeed = SMALL_CREATURE_SPEED;
        else if (state.getValue(WORKER_MEDIUM_STATE))  generatedSpeed = MEDIUM_CREATURE_SPEED;
        else if (state.getValue(WORKER_LARGE_STATE))  generatedSpeed = LARGE_CREATURE_SPEED;
        else generatedSpeed = 0;


        return generatedSpeed;
    }

    //Stress Unit NOT ROTATION SPEED
    @Override
    public float calculateAddedStressCapacity() {
        float capacity;
        BlockState state = getBlockState();

        if (!BlockRegister.HORSE_CRANK.has(getBlockState())) return 0;

        if (state.getValue(WORKER_SMALL_STATE)) capacity = SMALL_CREATURE_STRESS / 4.0f;
        else if (state.getValue(WORKER_MEDIUM_STATE)) capacity = MEDIUM_CREATURE_STRESS / 8.0f;
        else if (state.getValue(WORKER_LARGE_STATE)) capacity = LARGE_CREATURE_STRESS / 16.0f;
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
        compound.putBoolean("mediumWorkerState", mediumWorkerState);
        compound.putBoolean("largeWorkerState", largeWorkerState);
        compound.putBoolean("hasWorker", hasWorker);

        LOGGER.info("Written to NBT: HasWorker {}, SmallWorkerState {}, MediumWorkerState {}, LargeWorkerState {}",
                hasWorker, smallWorkerState, mediumWorkerState, largeWorkerState);

        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        hasWorker = compound.getBoolean("hasWorker");
        smallWorkerState = compound.getBoolean("smallWorkerState");
        mediumWorkerState = compound.getBoolean("mediumWorkerState");
        largeWorkerState = compound.getBoolean("largeWorkerState");

        LOGGER.info("Read from NBT: HasWorker {}, SmallWorkerState {}, MediumWorkerState {}, LargeWorkerState {}",
                hasWorker, smallWorkerState, mediumWorkerState, largeWorkerState);
        super.read(compound, clientPacket);
    }

    @Override
    public void tick() {
        BlockPos pos = this.getBlockPos();
        super.tick();
        if (level == null || level.isClientSide()) return;

        Mob workerEntity = getWorker(level, pos);
        if (workerEntity == null || !workerEntity.isAlive() || !workerEntity.isLeashed()) {
            updateToDefaultState(getBlockState(), pos, level);
        }

        if(workerEntity != null) updateBlockStateBasedOnMob(workerEntity, this.getBlockState(), level, pos);

        smallWorkerState = this.getBlockState().getValue(WORKER_SMALL_STATE);
        mediumWorkerState = this.getBlockState().getValue(WORKER_MEDIUM_STATE);
        largeWorkerState = this.getBlockState().getValue(WORKER_LARGE_STATE);
        hasWorker = this.getBlockState().getValue(HAS_WORKER);

        updateAnimation();
    }

    private void updateToDefaultState(BlockState state, BlockPos pos, Level level){
        LOGGER.info("Worker is not available or alive or leashed. Resetting state.");
        level.setBlock(pos, state.setValue(HAS_WORKER, false).setValue(WORKER_LARGE_STATE, false)
                .setValue(WORKER_MEDIUM_STATE, false).setValue(WORKER_SMALL_STATE, false), 3);

        // Update the Tile Entity's internal state
        hasWorker = false;
        smallWorkerState = false;
        mediumWorkerState = false;
        largeWorkerState = false;

        // Mark the Tile Entity as changed to trigger saving
        setChanged();
    }

    private void updateAnimation(){
        //Update animation based on stats
        if (getGeneratedSpeed() == 0) updateGeneratedRotation();

        if (getGeneratedSpeed() < 0 || getSpeed() < 0){
            setSpeed(-1 * getSpeed());
        }
        updateGeneratedRotation();
    }
}
