package net.steampn.createhorsepower.blocks.horse_crank;

import static net.steampn.createhorsepower.blocks.horse_crank.HorseCrankBlock.HAS_WORKER;
import static net.steampn.createhorsepower.blocks.horse_crank.HorseCrankBlock.LARGE_WORKER_STATE;
import static net.steampn.createhorsepower.blocks.horse_crank.HorseCrankBlock.MEDIUM_WORKER_STATE;
import static net.steampn.createhorsepower.blocks.horse_crank.HorseCrankBlock.SMALL_WORKER_STATE;

import com.mojang.logging.LogUtils;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.WalkAnimationState;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.steampn.createhorsepower.config.Config;
import net.steampn.createhorsepower.registry.BlockRegister;
import org.slf4j.Logger;

public class HorseCrankTileEntity extends GeneratingKineticBlockEntity {

  private static final Logger LOGGER = LogUtils.getLogger();
  private static final int TICKS_PER_BLOCK_UPDATE =
      20 * 2; // How often we should refresh the path blocks. Lower tolerance for lag here
  private static final int TICKS_PER_WORKER_UPDATE =
      20 * 10; // How often we should refresh the worker. High tolerance for lag here
  private static final BlockPos[] OFFSETS = generateOffsets();
  public boolean hasValidWorkingBlocks = false;
  private float rpmModifier = 0.0f;
  private Block[] cachedSurroundingBlocks;
  private PathfinderMob cachedWorkerMob;
  private long lastBlockUpdateTick = -1, lastWorkerUpdateTick = -1;
  private float lastGeneratedSpeed = 0;
  private float lastSpeed = 0;

  public HorseCrankTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
    super(type, pos, state);
  }

  private static BlockPos[] generateOffsets() {
    List<BlockPos> offsets = new ArrayList<>();
    for (int z = -3; z <= 3; z++) {
      for (int x = -3; x <= 3; x++) {
        if (x == 0 && z == 0) {
          continue; // Skip the center block
        }
        offsets.add(new BlockPos(x, -1, z));
      }
    }
    return offsets.toArray(new BlockPos[0]);
  }

  @Override
  public float getGeneratedSpeed() {
    float generatedSpeed;
    BlockState state = getBlockState();

    if (!BlockRegister.HORSE_CRANK.has(getBlockState())) {
      return 0;
    }

    if (!state.getValue(HAS_WORKER)) {
      return 0;
    }

    if (!hasValidWorkingBlocks) {
      return 0;
    }

    generatedSpeed = 4 * rpmModifier;

    return generatedSpeed;
  }

  @Override
  public float calculateAddedStressCapacity() {
    float capacity;
    BlockState state = getBlockState();

    if (!BlockRegister.HORSE_CRANK.has(getBlockState())) {
      return 0;
    }

    if (!state.getValue(HAS_WORKER)) {
      return 0;
    }

    if (state.getValue(SMALL_WORKER_STATE)) {
      capacity = Config.small_creature_stress / getSpeed();
    } else if (state.getValue(MEDIUM_WORKER_STATE)) {
      capacity = Config.medium_creature_stress / getSpeed();
    } else if (state.getValue(LARGE_WORKER_STATE)) {
      capacity = Config.large_creature_stress / getSpeed();
    } else {
      capacity = 0;
    }

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
    if (level == null || level.isClientSide()) {
      return;
    }

    Block[] blockTypeGrid = getValidSurroundingPathBlocks();
    Set<Block> blockSet = surroundingValidBlocksSet(blockTypeGrid);

    if (blockSet.contains(null)) { // If blocks in set are not all valid, disable block
      hasValidWorkingBlocks = false;
      if (this.lastCapacityProvided
          != 0f) { // If the crank has been running before this tick, update it to turn it off.
        updateAnimation();
      }
      return;
    } else { // If all valid blocks are the same
      adjustRPMModifier(blockSet);
    }

    moveWorkerTo(getWorkerMob());

    updateAnimation();
  }

  private PathfinderMob getMob(List<PathfinderMob> mobsNearCrank) {
    Stream<PathfinderMob> mobsAttachedToCrank = mobsNearCrank.stream()
        .filter(mob -> mob.isLeashed()
            && mob.getLeashHolder() instanceof LeashFenceKnotEntity
            && isBlockPosEqual(mob.getLeashHolder().blockPosition(), this.getBlockPos()));
    List<PathfinderMob> mobs = mobsAttachedToCrank.toList();
    return mobs.isEmpty() ? null : mobs.get(0);
  }

  private boolean isBlockPosEqual(BlockPos pos1, BlockPos pos2) {
    return pos1.getX() == pos2.getX() && pos1.getY() == pos2.getY() && pos1.getZ() == pos2.getZ();
  }

  private PathfinderMob getWorkerMob() {
    if (lastWorkerUpdateTick > level.getGameTime() - TICKS_PER_WORKER_UPDATE
        && cachedWorkerMob != null
        && cachedWorkerMob.isAlive()
        && cachedWorkerMob.isLeashed()
        && cachedWorkerMob.getLeashHolder() != null
        && cachedWorkerMob.getLeashHolder() instanceof LeashFenceKnotEntity
        && isBlockPosEqual(cachedWorkerMob.getLeashHolder().blockPosition(), this.getBlockPos())) {
      return cachedWorkerMob;
    }
    List<PathfinderMob> mobsNearCrank = level.getEntitiesOfClass(PathfinderMob.class,
        new AABB(this.getBlockPos()).inflate(8.0D));
    cachedWorkerMob = getMob(mobsNearCrank);
    lastWorkerUpdateTick = level.getGameTime();
    return cachedWorkerMob;
  }

  private void moveWorkerTo(Mob worker) {
    if (worker == null) {
      return;
    }

    if (worker instanceof Horse && ((Horse) worker).isEating()) {
      ((Horse) worker).setEating(false);
    }

    double baseRadius = 3.0; // Default radius
    double sizeFactor = Math.max(0.8,
        1.0 - (worker.getBbWidth() - 0.5)); // Scale radius based on size
    double radius = baseRadius * sizeFactor;

    int ticksPerRotation = (int) (20 * 10 * getTickSpeedModifier()); //200 * modifier

    BlockPos pos = this.getBlockPos();
    double bx = pos.getX();
    double by = pos.getY();
    double bz = pos.getZ();
    // add 0.5D so that we're targetting the center of the block, as blockPos selects a corner by default
    bx += 0.5D;
    bz += 0.5D;

    double distanceToWorker = worker.distanceToSqr(pos.getCenter());

    if (distanceToWorker <= (radius * radius) + 20.5) {
      double progress =
          (worker.level().getGameTime() % ticksPerRotation) / (double) ticksPerRotation;

      double currentX = worker.xo;
      double currentZ = worker.zo;

      double angle = 2 * Math.PI * progress;
      double xOffset = radius * Math.sin(angle);
      double zOffset = radius * Math.cos(angle);
      double targetX = bx + xOffset;
      double targetZ = bz + zOffset;
      worker.teleportTo(targetX, by, targetZ);
      WalkAnimationState animation = worker.walkAnimation;
      animation.update(-animation.position(), 1);
      float movementYaw = calculateYaw(currentX, currentZ, targetX, targetZ);
      worker.setYRot(movementYaw);
      worker.setYHeadRot(movementYaw);
    }
  }

  private float calculateYaw(double currentX, double currentZ, double targetX, double targetZ) {
    double deltaX = targetX - currentX;
    double deltaZ = targetZ - currentZ;

    // Calculate the angle in radians and convert to degrees
    float yaw = (float) Math.toDegrees(Math.atan2(deltaZ, deltaX));

    // Adjust to Minecraft's yaw system
    yaw = yaw - 90.0F;

    return yaw;
  }

  private void adjustRPMModifier(Set<Block> blockSet) {
    hasValidWorkingBlocks = true;
    int normalBlocks = 0;
    int greatBlocks = 0;

    for (Block block : blockSet) {
      if (Config.poor_path.contains(block)) {
        // If the block is poor, we can short circuit and set the rpmModifier to 0.5f
        rpmModifier = 0.5f;
        return;
      } else if (Config.normal_path.contains(block)) {
        ++normalBlocks;
      } else if (Config.great_path.contains(block)) {
        ++greatBlocks;
      }
    }
    if (normalBlocks > 0) {
      rpmModifier = 1.0f;
    } else if (greatBlocks == blockSet.size()) {
      rpmModifier = 2.0f;
    }
  }

  private double getTickSpeedModifier() {
    return rpmModifier > 0 ? (Math.PI - .14d) / (2 * rpmModifier) : 0;
  }

  private Set<Block> surroundingValidBlocksSet(Block[] surroundingBlocks) {
    return new HashSet<>(Arrays.asList(surroundingBlocks));
  }

  private Block[] getValidSurroundingPathBlocks() {
    if (cachedSurroundingBlocks != null
        && lastBlockUpdateTick > level.getGameTime() - TICKS_PER_BLOCK_UPDATE) {
      return cachedSurroundingBlocks;
    }

    // Precompute the valid blocks set
    Set<Block> validBlocks = new HashSet<>();
    validBlocks.addAll(Config.poor_path);
    validBlocks.addAll(Config.normal_path);
    validBlocks.addAll(Config.great_path);

    Block[] blockTypeGrid = new Block[OFFSETS.length];
    BlockPos pos = this.getBlockPos();

    for (int i = 0; i < OFFSETS.length; i++) {
      BlockPos targetPos = pos.offset(OFFSETS[i]);
      BlockState targetedBlock = level.getBlockState(targetPos);

      // Check if the block is in the precomputed set
      if (validBlocks.contains(targetedBlock.getBlock())) {
        blockTypeGrid[i] = targetedBlock.getBlock();
      }
    }

    cachedSurroundingBlocks = blockTypeGrid;
    lastBlockUpdateTick = level.getGameTime();
    return blockTypeGrid;
  }

  private void updateAnimation() {
    float currentGeneratedSpeed = getGeneratedSpeed();
    float currentSpeed = getSpeed();

    // Skip updates if speed and state haven't changed
    if (currentGeneratedSpeed == lastGeneratedSpeed && currentSpeed == lastSpeed) {
      return;
    }

    // Handle speed adjustments
    if (currentGeneratedSpeed == 0 && lastCapacityProvided != 0f) {
      updateGeneratedRotation();
    }

    if (currentGeneratedSpeed < 0 || currentSpeed < 0) {
      setSpeed(-1 * currentSpeed);
    }

    // Update rotation only once
    updateGeneratedRotation();

    // Cache the current state
    lastGeneratedSpeed = currentGeneratedSpeed;
    lastSpeed = currentSpeed;
  }
}
