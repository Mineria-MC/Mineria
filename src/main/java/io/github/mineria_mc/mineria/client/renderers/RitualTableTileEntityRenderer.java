package io.github.mineria_mc.mineria.client.renderers;

import io.github.mineria_mc.mineria.common.blocks.ritual_table.RitualTableTileEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;

public class RitualTableTileEntityRenderer implements BlockEntityRenderer<RitualTableTileEntity> {
    private final ItemRenderer itemRenderer;

    public RitualTableTileEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        this.itemRenderer = ctx.getItemRenderer();
    }

    @Override
    public void render(RitualTableTileEntity tile, float p_225616_2_, PoseStack stack, MultiBufferSource buffer, int packedLight, int overlay) {
        stack.pushPose();
        ItemStack placedItem = tile.getPlacedItem();
        if (!placedItem.isEmpty()) {
            stack.translate(0.5D, 1.05D, 0.5D);
            stack.scale(0.5F, 0.5F, 0.5F);
            stack.mulPose(Axis.XP.rotationDegrees(90));
            stack.mulPose(Axis.ZP.rotationDegrees(35));
            packedLight = tile.getLevel() == null ? 15728880 : LevelRenderer.getLightColor(tile.getLevel(), tile.getBlockPos().above());
            this.itemRenderer.renderStatic(placedItem, ItemTransforms.TransformType.FIXED, packedLight, overlay, stack, buffer, 0);
        }
        stack.popPose();
    }
}
