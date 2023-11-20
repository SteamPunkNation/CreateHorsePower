package net.steampn.createhorsepower.blocks.horse_crank;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.steampn.createhorsepower.utils.BlockRegister;
import org.jetbrains.annotations.Nullable;

public class HorseCrankItem extends BlockItem {
    public HorseCrankItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    protected boolean placeBlock(BlockPlaceContext context, BlockState state) {
        return super.placeBlock(context, state);
    }

    @Nullable
    @Override
    protected BlockState getPlacementState(BlockPlaceContext context) {
        BlockState state = BlockRegister.HORSE_CRANK.getDefaultState();
        return state;
    }
}
