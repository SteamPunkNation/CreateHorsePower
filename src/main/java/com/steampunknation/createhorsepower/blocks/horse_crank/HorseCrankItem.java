package com.steampunknation.createhorsepower.blocks.horse_crank;

import com.steampunknation.createhorsepower.utils.BlockRegister;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;

import javax.annotation.Nullable;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class HorseCrankItem extends BlockItem {
    public HorseCrankItem(HorseCrankBlock block, Properties properties) {
        super(block, properties);
    }

    @Nullable
    @Override
    protected BlockState getPlacementState(BlockItemUseContext context) {
        BlockState state = BlockRegister.HORSE_CRANK.get().getStateForPlacement(context);
        return state != null && this.canPlace(context, state) ? state : null;
    }

    @Override
    protected boolean placeBlock(BlockItemUseContext context, BlockState state) {
       return super.placeBlock(context, state);
    }

    public static HorseCrankItemRenderer getRenderer() {
        return HorseCrankItemRenderer.INSTANCE == null ? new HorseCrankItemRenderer() : HorseCrankItemRenderer.INSTANCE;
    }

    public static Supplier<Callable<ItemStackTileEntityRenderer>> renderer() {
        return () -> HorseCrankItem::getRenderer;
    }
}
