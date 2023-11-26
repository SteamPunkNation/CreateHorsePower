package net.steampn.createhorsepower.blocks.horse_crank;

import com.jozufozu.flywheel.backend.Backend;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.block.state.BlockState;
import net.steampn.createhorsepower.utils.CHPBlockPartials;

public class HorseCrankRenderer extends KineticBlockEntityRenderer {
    public HorseCrankRenderer(Context dispatcher) {
        super(dispatcher);
    }

    @Override
    protected BlockState getRenderedBlockState(KineticBlockEntity te) {
        return shaft(getRotationAxisOf(te));
    }

    @Override
    protected void renderSafe(KineticBlockEntity te, float partialTicks, PoseStack ps, MultiBufferSource buffer, int light, int overlay) {
        super.renderSafe(te, partialTicks, ps, buffer, light, overlay);

        if(Backend.canUseInstancing(te.getLevel())) return;
        BlockState state = te.getBlockState();
        BlockPos pos = te.getBlockPos();

        VertexConsumer vc = buffer.getBuffer(RenderType.cutout());

        int packedLightmapCoords = LevelRenderer.getLightColor(te.getLevel(), pos);

        SuperByteBuffer crankCog = CachedBufferer.partial(CHPBlockPartials.HORSE_CRANK_COG, state);
        Axis axis = getRotationAxisOf(te);
        crankCog
                .rotateCentered(Direction.UP, axis == Direction.Axis.X ? 0 : 90 * (float) Math.PI / 180f)
                .light(packedLightmapCoords)
                .renderInto(ps, vc);
    }
}
