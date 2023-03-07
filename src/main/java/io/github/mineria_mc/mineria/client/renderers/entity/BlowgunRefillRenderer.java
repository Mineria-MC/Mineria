package io.github.mineria_mc.mineria.client.renderers.entity;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.entity.BlowgunRefillEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import javax.annotation.Nonnull;

public class BlowgunRefillRenderer extends EntityRenderer<BlowgunRefillEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Mineria.MODID, "textures/entity/blowgun_refill.png");

    public BlowgunRefillRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(BlowgunRefillEntity entity, float yRot, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        stack.pushPose();
        stack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - 90.0F));
        stack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot())));
        stack.mulPose(Axis.XP.rotationDegrees(45.0F));
        stack.scale(0.05625F, 0.05625F, 0.05625F);
        stack.translate(-4.0F, 0.0F, 0.0F);
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutout(this.getTextureLocation(entity)));
        PoseStack.Pose last = stack.last();
        Matrix4f pose = last.pose();
        Matrix3f normal = last.normal();

        for(int j = 0; j < 4; ++j) {
            stack.mulPose(Axis.XP.rotationDegrees(90.0F));
            this.vertex(pose, normal, consumer, 3, -1, 0, 0, 2 / 16f, 0, 1, 0, packedLight);
            this.vertex(pose, normal, consumer, 5, -1, 0, 2 / 16f, 3 / 16f, 0, 1, 0, packedLight);
            this.vertex(pose, normal, consumer, 5, 1, 0, 2 / 16f, 3 / 16f, 0, 1, 0, packedLight);
            this.vertex(pose, normal, consumer, 3, 1, 0, 0, 3 / 16f, 0, 1, 0, packedLight);

            int uOff = j % 2 == 0 ? 5 : 0;
            this.vertex(pose, normal, consumer, -3, -2, 0, uOff / 16f, 0, 0, 1, 0, packedLight);
            this.vertex(pose, normal, consumer, 3, -2, 0, (5 + uOff) / 16f, 0, 0, 1, 0, packedLight);
            this.vertex(pose, normal, consumer, 3, 2, 0, (5 + uOff) / 16f, 2 / 16f, 0, 1, 0, packedLight);
            this.vertex(pose, normal, consumer, -3, 2, 0, uOff / 16f, 2 / 16f, 0, 1, 0, packedLight);
        }

        stack.popPose();
        super.render(entity, yRot, partialTicks, stack, buffer, packedLight);
    }

    public void vertex(Matrix4f pose, Matrix3f normal, VertexConsumer consumer, float x, float y, float z, float u, float v, int normalX, int normalZ, int normalY, int packedLight) {
        consumer.vertex(pose, x, y, z).color(255, 255, 255, 255).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(normal, (float)normalX, (float)normalY, (float)normalZ).endVertex();
    }

    @Nonnull
    @Override
    public ResourceLocation getTextureLocation(@Nonnull BlowgunRefillEntity entity) {
        return TEXTURE;
    }
}
