package com.steampunknation.createhorsepower.blocks.horse_crank;

import com.simibubi.create.content.contraptions.base.KineticBlock;
import com.simibubi.create.content.contraptions.relays.elementary.ICogWheel;
import com.simibubi.create.foundation.block.ITE;
import com.steampunknation.createhorsepower.utils.CHPTags;
import com.steampunknation.createhorsepower.utils.TileEntityRegister;
import com.steampunknation.createhorsepower.utils.CHPShapes;
import net.minecraft.block.BlockState;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.LeadItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;


public class HorseCrankBlock extends KineticBlock implements ITE<HorseCrankTileEntity>, ICogWheel {

    public HorseCrankBlock(Properties properties) {
        super(properties);
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return TileEntityRegister.HORSE_CRANK.create();
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return CHPShapes.HORSE_CRANK;
    }

    @Override
    public boolean hasShaftTowards(IWorldReader world, BlockPos pos, BlockState state, Direction face) {
        return face == Direction.DOWN;
    }

    @Override
    public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        worldIn.removeBlockEntity(pos);
    }

    @Override
    public Axis getRotationAxis(BlockState state) {
        return Axis.Y;
    }

    @Override
    public Class<HorseCrankTileEntity> getTileEntityClass() {
        return HorseCrankTileEntity.class;
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {

        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();

        for (MobEntity mobentity : worldIn.getEntitiesOfClass(MobEntity.class, new AxisAlignedBB((double) i - 7.0D, (double) j - 7.0D, (double) k - 7.0D, (double) i + 7.0D, (double) j + 7.0D, (double) k + 7.0D))) {
            if (mobentity.getLeashHolder() == player){
                if (CHPTags.Entities.SMALL_WORKER.contains(mobentity.getType())
                || CHPTags.Entities.MEDIUM_WORKER.contains(mobentity.getType())
                || CHPTags.Entities.LARGE_WORKER.contains(mobentity.getType())) {
                    if (worldIn.isClientSide) {
                        ItemStack stack = player.getItemInHand(handIn);
                        return stack.getItem() == Items.LEAD ? ActionResultType.SUCCESS : ActionResultType.PASS;
                    } else {
                        return LeadItem.bindPlayerMobs(player, worldIn, pos);
                    }
                }
            }
        }
        return ActionResultType.FAIL;
    }
}
