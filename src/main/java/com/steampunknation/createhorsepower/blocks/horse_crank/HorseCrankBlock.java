package com.steampunknation.createhorsepower.blocks.horse_crank;

import com.google.common.collect.Lists;
import com.simibubi.create.content.contraptions.base.KineticBlock;
import com.simibubi.create.content.contraptions.relays.elementary.ICogWheel;
import com.simibubi.create.foundation.block.ITE;
import com.steampunknation.createhorsepower.TileEntityRegister;
import com.steampunknation.createhorsepower.CHPShapes;
import com.steampunknation.createhorsepower.utils.CHPUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.LeadItem;
import net.minecraft.nbt.CompoundNBT;
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
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.List;


public class HorseCrankBlock extends KineticBlock implements ITE<HorseCrankTileEntity>, ICogWheel {

    public HorseCrankBlock(Properties properties){
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

    //Horse Power Reference
    public void onWorkerAttached(PlayerEntity playerIn, CreatureEntity creature) {}

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        ItemStack stack = handIn == Hand.MAIN_HAND ? player.getItemInHand(handIn): ItemStack.EMPTY;
        HorseCrankTileEntity te = (HorseCrankTileEntity) worldIn.getBlockEntity(pos);
        HorseCrankTileEntity teH = null;
        if (te == null)
            return ActionResultType.FAIL;
        if (te instanceof HorseCrankTileEntity)
            teH = (HorseCrankTileEntity) te;

        CreatureEntity creature = null;
        if (teH != null) {
            AxisAlignedBB aabb = new AxisAlignedBB(pos).inflate(7.0D);

            Entity entity = CHPUtils.getEntityWithinArea(worldIn, aabb, e -> e.isLeashed() && e.getLeashHolder() == player);
            if (entity != null) {
                creature = (CreatureEntity) entity;
            }
        }
        if (teH != null && ((stack.getItem() instanceof LeadItem && creature != null) || creature != null)) {
            if (!teH.hasWorker()) {
                creature.dropLeash(true, false);
                teH.setWorker(creature);
                onWorkerAttached(player, creature);
                return ActionResultType.SUCCESS;
            } else {
                return ActionResultType.FAIL;
            }
        }

        ItemStack result = ItemStack.EMPTY;

        if (result.isEmpty()) {
            if (!stack.isEmpty())
                return ActionResultType.FAIL;
            if (teH != null)
                teH.setWorkerToPlayer(player);
        }

        if (!result.isEmpty())
            ItemHandlerHelper.giveItemToPlayer(player, result, EquipmentSlotType.MAINHAND.getIndex());

        te.setChanged();
        return ActionResultType.PASS;
    }
}
