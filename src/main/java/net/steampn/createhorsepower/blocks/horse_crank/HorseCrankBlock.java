package net.steampn.createhorsepower.blocks.horse_crank;

import com.mojang.logging.LogUtils;
import com.simibubi.create.content.kinetics.base.KineticBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.LeadItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.steampn.createhorsepower.utils.CHPShapes;
import net.steampn.createhorsepower.utils.TileEntityRegister;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;


import static net.steampn.createhorsepower.utils.CHPProperties.*;

public class HorseCrankBlock extends KineticBlock implements IBE<HorseCrankTileEntity>, ICogWheel {

    private static final Logger LOGGER = LogUtils.getLogger();
    public HorseCrankBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState()
                .setValue(WORKER_SMALL_STATE, false)
                .setValue(WORKER_MEDIUM_STATE, false)
                .setValue(WORKER_LARGE_STATE,false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(WORKER_SMALL_STATE,WORKER_MEDIUM_STATE,WORKER_LARGE_STATE));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return CHPShapes.HORSE_CRANK;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pState.hasBlockEntity() && pState.getBlock() != pNewState.getBlock()){
            pLevel.removeBlockEntity(pPos);
        }
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
    public boolean hideStressImpact() {
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
    public InteractionResult use(BlockState state, Level worldIn, @NotNull BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        HorseCrankTileEntity horseCrankTileEntity = (HorseCrankTileEntity) worldIn.getBlockEntity(pos);
        AABB aabb = new AABB(pos).inflate(1.0D);

        if (horseCrankTileEntity == null)
            return InteractionResult.FAIL;

        if (worldIn.isClientSide || !horseCrankTileEntity.hasLeashKnot(worldIn, aabb)) {
            ItemStack stack = player.getItemInHand(handIn);
            return stack.is(Items.LEAD) ? InteractionResult.SUCCESS : InteractionResult.PASS;
        }
        else if(!horseCrankTileEntity.hasLeashKnot(worldIn, aabb) && horseCrankTileEntity.hasWorker){
            removeLeashKnot(worldIn, aabb, horseCrankTileEntity);
            return InteractionResult.PASS;
        }
        else {
            if (LeadItem.bindPlayerMobs(player, worldIn, pos) == InteractionResult.SUCCESS)
                LOGGER.info("Leashed!");
            return InteractionResult.PASS;
        }
    }

    public void removeLeashKnot(Level level, AABB aabb, HorseCrankTileEntity te){
        for(Mob mob : level.getEntitiesOfClass(Mob.class, aabb)){
            if (mob.isLeashed() && te.hasWorker)
                mob.dropLeash(true, false);
        }
    }
}
