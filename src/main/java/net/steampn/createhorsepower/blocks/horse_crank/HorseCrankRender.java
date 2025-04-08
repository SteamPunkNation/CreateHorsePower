package net.steampn.createhorsepower.blocks.horse_crank;

import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.steampn.createhorsepower.utils.CHPBlockPartials;

public class HorseCrankRender extends KineticBlockEntityRenderer<HorseCrankTileEntity> {
    public HorseCrankRender(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected SuperByteBuffer getRotatedModel(HorseCrankTileEntity be, BlockState state) {
        return CachedBuffers.partial(CHPBlockPartials.HORSE_CRANK_COG, state);
    }
}
