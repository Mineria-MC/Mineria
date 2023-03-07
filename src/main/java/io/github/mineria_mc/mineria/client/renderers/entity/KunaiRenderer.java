package io.github.mineria_mc.mineria.client.renderers.entity;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.entity.KunaiEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;

import javax.annotation.Nonnull;

public class KunaiRenderer extends EntityRenderer<KunaiEntity> {
    // Unused
    private static final ResourceLocation TEXTURE = new ResourceLocation(Mineria.MODID, "textures/item/kunai.png");
    private final ItemRenderer itemRenderer;

    public KunaiRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
        this.itemRenderer = ctx.getItemRenderer();
    }

    @Override
    public void render(KunaiEntity entity, float yRot, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        stack.pushPose();
        stack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - 90.0F));
        stack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot()) - 45f));
        ItemStack kunai = entity.getKunaiItem().copy();
        if(entity.isEnchanted()) {
            // So the item renderer actually renders the enchantment glint
            kunai.enchant(Enchantments.AQUA_AFFINITY, 0);
        }
        this.itemRenderer.renderStatic(kunai, ItemTransforms.TransformType.NONE, packedLight, OverlayTexture.NO_OVERLAY, stack, buffer, 0);
        stack.popPose();
        super.render(entity, yRot, partialTicks, stack, buffer, packedLight);
    }

    @Nonnull
    @Override
    public ResourceLocation getTextureLocation(@Nonnull KunaiEntity entity) {
        return TEXTURE;
    }
}
