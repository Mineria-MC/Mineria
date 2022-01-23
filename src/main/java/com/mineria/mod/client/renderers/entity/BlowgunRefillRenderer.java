package com.mineria.mod.client.renderers.entity;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.entity.BlowgunRefillEntity;
import com.mineria.mod.client.models.entity.BlowgunRefillModel;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

public class BlowgunRefillRenderer extends EntityRenderer<BlowgunRefillEntity>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Mineria.MODID, "textures/entity/blowgun_refill.png");
    private final BlowgunRefillModel model = new BlowgunRefillModel();

    public BlowgunRefillRenderer(EntityRendererManager manager)
    {
        super(manager);
    }

    @Override
    public void render(BlowgunRefillEntity entity, float yRot, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int packedLight)
    {
        stack.pushPose();
        stack.mulPose(Vector3f.YP.rotationDegrees(MathHelper.lerp(partialTicks, entity.yRotO, entity.yRot) + 90.0F));
        stack.mulPose(Vector3f.ZP.rotationDegrees(MathHelper.lerp(partialTicks, entity.xRotO, entity.xRot) + 90.0F));
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
