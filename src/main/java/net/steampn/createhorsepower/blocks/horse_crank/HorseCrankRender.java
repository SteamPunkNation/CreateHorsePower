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
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.steampn.createhorsepower.utils.CHPBlockPartials;

public class HorseCrankRender extends KineticBlockEntityRenderer {
    public HorseCrankRender(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected BlockState getRenderedBlockState(KineticBlockEntity be) {
        return shaft(getRotationAxisOf(be));
    }

    @Override
    protected void renderSafe(KineticBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {

        if(Backend.canUseInstancing(be.getLevel())) return;
        BlockState state = be.getBlockState();
        BlockPos pos = be.getBlockPos();

        VertexConsumer vc = buffer.getBuffer(RenderType.cutout());

        int packedLightmapCoords = LevelRenderer.getLightColor(be.getLevel(), pos);

        SuperByteBuffer crankCog = CachedBufferer.partial(CHPBlockPartials.HORSE_CRANK_COG, state);
        Direction.Axis axis = getRotationAxisOf(be);
        crankCog
                .rotateCentered(Direction.UP, axis == Direction.Axis.X ? 0 : 90 * (float) Math.PI / 180f)
                .light(packedLightmapCoords)
                .renderInto(ms, vc);
    }
}
