package com.mineria.mod.client.renderers;

import com.mineria.mod.common.blocks.ritual_table.RitualTableTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;

public class RitualTableTileEntityRenderer extends TileEntityRenderer<RitualTableTileEntity>
{
    public RitualTableTileEntityRenderer(TileEntityRendererDispatcher dispatcher)
    {
        super(dispatcher);
    }

    @Override
    public void render(RitualTableTileEntity tile, float p_225616_2_, MatrixStack stack, IRenderTypeBuffer buffer, int packedLight, int overlay)
    {
        stack.pushPose();
        ItemStack placedItem = tile.getPlacedItem();
        if(!placedItem.isEmpty())
        {
            stack.translate(0.5D, 1.05D, 0.5D);
            stack.scale(0.5F, 0.5F, 0.5F);
            stack.mulPose(Vector3f.XP.rotationDegrees(90));
            stack.mulPose(Vector3f.ZP.rotationDegrees(35));
            packedLight = tile.getLevel() == null ? 15728880 : WorldRenderer.getLightColor(tile.getLevel(), tile.getBlockPos().above());
            Minecraft.getInstance().getItemRenderer().renderStatic(placedItem, ItemCameraTransforms.TransformType.FIXED, packedLight, overlay, stack, buffer);
        }
        stack.popPose();
    }
}
