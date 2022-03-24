package com.steampunknation.createhorsepower.blocks.horse_crank;

import com.steampunknation.createhorsepower.utils.BlockRegister;
import com.steampunknation.createhorsepower.utils.CHPBlockPartials;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.simibubi.create.foundation.render.PartialBufferer;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;

public class HorseCrankItemRenderer extends ItemStackTileEntityRenderer {
    public static HorseCrankItemRenderer INSTANCE;

    public HorseCrankItemRenderer() {
        INSTANCE = this;
    }

    public BlockState state = BlockRegister.HORSE_CRANK.getDefaultState();

    public float offset = 1.4f;
    public float inventoryOffset = -0.3f;

    @Override
    public void renderByItem(ItemStack stack, ItemCameraTransforms.TransformType type, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, int p_239207_6_) {
        float o = offset;
        if (type == ItemCameraTransforms.TransformType.GUI) {
            o += inventoryOffset;
        }
        IVertexBuilder vertexBuilder = buffer.getBuffer(RenderType.cutout());
        PartialBufferer.get(CHPBlockPartials.HORSE_CRANK_COG, state)
                .light(light)
                .translate(0, o, 0)
                .renderInto(matrixStack, vertexBuilder);
        PartialBufferer.get(CHPBlockPartials.HORSE_CRANK_BASE, state)
                .light(light)
                .translate(0, o, 0)
                .renderInto(matrixStack, vertexBuilder);
        super.renderByItem(stack, type, matrixStack, buffer, light, p_239207_6_);
    }
}
