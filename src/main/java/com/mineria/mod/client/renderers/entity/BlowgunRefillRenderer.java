package com.mineria.mod.client.renderers.entity;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.entity.BlowgunRefillEntity;
import com.mineria.mod.client.models.entity.BlowgunRefillModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import com.mojang.math.Vector3f;

public class BlowgunRefillRenderer extends EntityRenderer<BlowgunRefillEntity>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Mineria.MODID, "textures/entity/blowgun_refill.png");
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(new ResourceLocation(Mineria.MODID, "dart"), "main");
    private final BlowgunRefillModel model;

    public BlowgunRefillRenderer(EntityRendererProvider.Context ctx)
    {
        super(ctx);
        this.model = new BlowgunRefillModel(ctx.bakeLayer(LAYER));
    }

    @Override
    public void render(BlowgunRefillEntity entity, float yRot, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight)
    {
        stack.pushPose();
        stack.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) + 90.0F));
        stack.mulPose(Vector3f.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot()) + 90.0F));
        stack.mulPose(Vector3f.XP.rotationDegrees(90.0F));
        this.model.renderToBuffer(stack, buffer.getBuffer(this.model.renderType(this.getTextureLocation(entity))), packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        stack.popPose();
        super.render(entity, yRot, partialTicks, stack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(BlowgunRefillEntity entity)
    {
        return TEXTURE;
    }
}
