package net.steampn.createhorsepower.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.steampn.createhorsepower.client.model.CHPLeashKnotModel;
import net.steampn.createhorsepower.entities.CHPLeashKnotEntity;

@OnlyIn(Dist.CLIENT)
public class CHPLeashKnotRenderer extends EntityRenderer<CHPLeashKnotEntity> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation("textures/entity/lead_knot.png");
    private final CHPLeashKnotModel<CHPLeashKnotEntity> model;

    public CHPLeashKnotRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
        this.model = new CHPLeashKnotModel<>(ctx.bakeLayer(CHPLeashKnotModel.LAYER_LOCATION));
    }

    public void render(CHPLeashKnotEntity entity, float p_115247_, float p_115248_, PoseStack poseStack, MultiBufferSource buffer, int p_115251_) {
        poseStack.pushPose();
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        this.model.setupAnim(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        VertexConsumer vertexconsumer = buffer.getBuffer(this.model.renderType(TEXTURE));
        this.model.renderToBuffer(poseStack, vertexconsumer, p_115251_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
        super.render(entity, p_115247_, p_115248_, poseStack, buffer, p_115251_);
    }

    @Override
    public ResourceLocation getTextureLocation(CHPLeashKnotEntity entity) {
        return TEXTURE;
    }
}
