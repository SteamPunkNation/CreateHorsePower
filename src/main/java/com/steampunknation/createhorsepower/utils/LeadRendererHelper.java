package com.steampunknation.createhorsepower.utils;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.LightType;

public class LeadRendererHelper {

    protected static <T extends MobEntity> int getBlockLight(T entity, BlockPos pos) {
        return entity.isOnFire() ? 15: entity.level.getBrightness(LightType.BLOCK, pos);
    }

    public static void renderLeash(CreatureEntity entity, Vector3d sourcePos, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn) {
        if (entity == null)
            return;

        matrixStackIn.pushPose();
        double d0 = (double) (MathHelper.lerp(partialTicks, entity.yBodyRot, entity.yBodyRotO) * ((float) Math.PI / 180F)) + (Math.PI / 2D);
        Vector3d startPos = entity.getLeashOffset();
        double d1 = Math.cos(d0) * startPos.z + Math.sin(d0) * startPos.x;
        double d2 = Math.sin(d0) * startPos.z - Math.cos(d0) * startPos.x;
        double d3 = MathHelper.lerp(partialTicks, entity.xo, entity.getX()) + d1;
        double d4 = MathHelper.lerp(partialTicks, entity.yo, entity.getY()) + startPos.y;
        double d5 = MathHelper.lerp(partialTicks, entity.zo, entity.getZ()) + d2;
        matrixStackIn.translate(0.5, startPos.y - 1, 0.5);

        float f = (float) (d3 - sourcePos.x);
        float f1 = (float) (d4 - sourcePos.y);
        float f2 = (float) (d5 - sourcePos.z);

        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.leash());
        Matrix4f matrix4f = matrixStackIn.last().pose();
        float f4 = MathHelper.fastInvSqrt(f * f + f2 * f2) * 0.025F / 2.0F;
        float f5 = f2 * f4;
        float f6 = f * f4;

        BlockPos entityBlockPos = new BlockPos(entity.getEyePosition(partialTicks));
        BlockPos sourceBlockPos = new BlockPos(sourcePos);
        int entityBlockLight = getBlockLight(entity, entityBlockPos);
        int sourceBlockLight = entity.level.getBrightness(LightType.BLOCK, sourceBlockPos);
        int entitySkyLight = entity.level.getBrightness(LightType.SKY, entityBlockPos);
        int sourceSkyLight = entity.level.getBrightness(LightType.SKY, sourceBlockPos);
        MobRenderer.renderSide(ivertexbuilder, matrix4f, f, f1, f2, entityBlockLight, sourceBlockLight, entitySkyLight, sourceSkyLight, 0.025F, 0.025F, f5, f6);
        MobRenderer.renderSide(ivertexbuilder, matrix4f, f, f1, f2, entityBlockLight, sourceBlockLight, entitySkyLight, sourceSkyLight, 0.025F, 0.0F, f5, f6);
        matrixStackIn.popPose();
    }

}
