package com.steampunknation.createhorsepower.blocks.horse_crank;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.simibubi.create.content.contraptions.base.KineticTileEntityRenderer;
import com.simibubi.create.foundation.render.PartialBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.steampunknation.createhorsepower.CHPBlockPartials;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class HorseCrankRenderer extends KineticTileEntityRenderer {

    public HorseCrankRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    protected SuperByteBuffer getRotatedModel(KineticTileEntity te) {
        return PartialBufferer.get(CHPBlockPartials.HORSE_CRANK_COG, te.getBlockState());
    }

    @Override
    protected BlockState getRenderedBlockState(KineticTileEntity te) {
        return shaft(Direction.Axis.Y);
    }

    @Override
    protected void renderSafe(KineticTileEntity te, float partialTicks, MatrixStack ms, IRenderTypeBuffer buffer, int light, int overlay) {
        super.renderSafe(te, partialTicks, ms, buffer, light, overlay);

        BlockState state = te.getBlockState();
        BlockPos pos = te.getBlockPos();

        IVertexBuilder vb = buffer.getBuffer(RenderType.cutout());

        int packedLightmapCoords = WorldRenderer.getLightColor(te.getLevel(), pos);

        SuperByteBuffer crankCog = PartialBufferer.get(CHPBlockPartials.HORSE_CRANK_COG, state);
        Direction.Axis axis = getRotationAxisOf(te);
        crankCog.rotateCentered(Direction.UP, axis == Direction.Axis.X ? 0 : 90 * (float) Math.PI / 180f)
                .light(packedLightmapCoords)
                .renderInto(ms, vb);
    }
}
