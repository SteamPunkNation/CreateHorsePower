package net.steampn.createhorsepower.blocks.horse_crank;

import com.mojang.logging.LogUtils;
import com.simibubi.create.content.kinetics.base.KineticBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.steampn.createhorsepower.entities.CHPLeashKnotEntity;
import net.steampn.createhorsepower.utils.CHPShapes;
import net.steampn.createhorsepower.utils.TileEntityRegister;
import org.slf4j.Logger;

import java.util.List;

import static net.steampn.createhorsepower.utils.CHPProperties.*;
import static net.steampn.createhorsepower.utils.CHPUtils.checkForMobsInVicinity;
import static net.steampn.createhorsepower.utils.CHPUtils.getWorker;

public class HorseCrankBlock extends KineticBlock implements IBE<HorseCrankTileEntity>, ICogWheel {
    private static final Logger LOGGER = LogUtils.getLogger();
    public HorseCrankBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState()
                .setValue(HAS_WORKER, false)
                .setValue(WORKER_SMALL_STATE, false)
                .setValue(WORKER_MEDIUM_STATE, false)
                .setValue(WORKER_LARGE_STATE,false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(HAS_WORKER,WORKER_SMALL_STATE,WORKER_MEDIUM_STATE,WORKER_LARGE_STATE));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return CHPShapes.HORSE_CRANK;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pState.hasBlockEntity() && pState.getBlock() != pNewState.getBlock()) pLevel.removeBlockEntity(pPos);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return Direction.Axis.Y;
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == Direction.DOWN;
    }

    @Override
    public Class<HorseCrankTileEntity> getBlockEntityClass() {
        return HorseCrankTileEntity.class;
    }

    @Override
    public BlockEntityType<? extends HorseCrankTileEntity> getBlockEntityType() {
        return TileEntityRegister.HORSE_CRANK.get();
    }


    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return TileEntityRegister.HORSE_CRANK.create(pos,state);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        ItemStack stack = player.getItemInHand(handIn);

        if (handIn != InteractionHand.MAIN_HAND) return InteractionResult.PASS;

        LOGGER.info("Item is: " + stack.getItem());

        if (worldIn.isClientSide()) return InteractionResult.PASS;

        if(stack.isEmpty() && state.getValue(HAS_WORKER)) return removeLeashKnot(worldIn, pos);

        else if (!(stack.getItem() == Items.LEAD)) return InteractionResult.PASS;

        List<Mob> mobsInVicinity = worldIn.getEntitiesOfClass(Mob.class, new AABB(pos).inflate(2.0D));

        long mobsAttachedToPlayer = mobsInVicinity.stream().filter(mob -> mob.isLeashed() && mob.getLeashHolder() == player).count();

        if(mobsAttachedToPlayer > 1) {
            player.displayClientMessage(Component.translatable("tooltip1.createhorsepower.horse_crank"), true);
            return InteractionResult.FAIL;
        }

        if(state.getValue(HAS_WORKER)) {
            player.displayClientMessage(Component.translatable("tooltip2.createhorsepower.horse_crank"), true);
            return InteractionResult.FAIL; // already has a worker, so early return
        }

        return checkForMobsInVicinity(mobsInVicinity, player, worldIn, pos, state);
    }

    private InteractionResult removeLeashKnot(Level worldIn, BlockPos pos){
        Mob workerEntity = getWorker(worldIn, pos);
        if(workerEntity != null && workerEntity.isLeashed()) {
            worldIn.getEntitiesOfClass(CHPLeashKnotEntity.class, new AABB(pos).inflate(0.2D))
                    .forEach(Entity::kill);
            LOGGER.info("Leash Knot killed!");
            workerEntity.dropLeash(true, !workerEntity.isSilent());
//                    player.addItem(new ItemStack(Items.LEAD, 1));
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }

//    public static void updateBlockStateBasedOnMob(Mob mob, BlockState state, Level world, BlockPos pos) {
//        if(mob.getType().getTags().toList().contains(LARGE_WORKER_TAG)) {
//            world.setBlock(pos, state.setValue(HAS_WORKER, true).setValue(WORKER_LARGE_STATE, true), 3);
//        }
//        else if(mob.getType().getTags().toList().contains(MEDIUM_WORKER_TAG)) {
//            world.setBlock(pos, state.setValue(HAS_WORKER, true).setValue(WORKER_MEDIUM_STATE, true), 3);
//        }
//        else if(mob.getType().getTags().toList().contains(SMALL_WORKER_TAG)) {
//            world.setBlock(pos, state.setValue(HAS_WORKER, true).setValue(WORKER_SMALL_STATE, true), 3);
//        }
//    }
}
