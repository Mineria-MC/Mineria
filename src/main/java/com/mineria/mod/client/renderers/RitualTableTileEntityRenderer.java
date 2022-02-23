package com.mineria.mod.client.renderers;

import com.mineria.mod.common.blocks.ritual_table.RitualTableTileEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;
import com.mojang.math.Vector3f;

public class RitualTableTileEntityRenderer implements BlockEntityRenderer<RitualTableTileEntity>
{
    public RitualTableTileEntityRenderer(BlockEntityRendererProvider.Context dispatcher)
    {
    }

    @Override
    public void render(RitualTableTileEntity tile, float p_225616_2_, PoseStack stack, MultiBufferSource buffer, int packedLight, int overlay)
    {
        stack.pushPose();
        ItemStack placedItem = tile.getPlacedItem();
        if(!placedItem.isEmpty())
        {
            stack.translate(0.5D, 1.05D, 0.5D);
            stack.scale(0.5F, 0.5F, 0.5F);
            stack.mulPose(Vector3f.XP.rotationDegrees(90));
            stack.mulPose(Vector3f.ZP.rotationDegrees(35));
            packedLight = tile.getLevel() == null ? 15728880 : LevelRenderer.getLightColor(tile.getLevel(), tile.getBlockPos().above());
            Minecraft.getInstance().getItemRenderer().renderStatic(placedItem, ItemTransforms.TransformType.FIXED, packedLight, overlay, stack, buffer, 0);
        }
        stack.popPose();
    }
}
