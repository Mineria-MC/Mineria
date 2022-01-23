package com.mineria.mod.client.renderers.entity;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.entity.ElementalOrbEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;

import static net.minecraft.util.ColorHelper.PackedColor.*;

public class ElementalOrbRenderer extends EntityRenderer<ElementalOrbEntity>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Mineria.MODID, "textures/entity/elemental_orb.png");
    private static final RenderType RENDER_TYPE = RenderType.itemEntityTranslucentCull(TEXTURE);

    public ElementalOrbRenderer(EntityRendererManager manager)
    {
        super(manager);
        this.shadowRadius = 0;
    }

    @Override
    protected int getBlockLightLevel(ElementalOrbEntity entity, BlockPos pos)
    {
        return 15;
    }

    @Override
    public void render(ElementalOrbEntity entity, float yRot, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int packedLight)
    {
        stack.pushPose();
        stack.translate(0.0D, 0.2D, 0.0D);
        stack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        stack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
        stack.scale(0.6F, 0.6F, 0.6F);
        IVertexBuilder builder = buffer.getBuffer(RENDER_TYPE);
        MatrixStack.Entry entry = stack.last();
        Matrix4f pose = entry.pose();
        Matrix3f normal = entry.normal();
        vertex(builder, pose, normal, -0.5F, -0.25F, entity.getElementType().getColor(), 0, 1, packedLight);
        vertex(builder, pose, normal, 0.5F, -0.25F, entity.getElementType().getColor(), 1, 1, packedLight);
        vertex(builder, pose, normal, 0.5F, 0.75F, entity.getElementType().getColor(), 1, 0, packedLight);
        vertex(builder, pose, normal, -0.5F, 0.75F, entity.getElementType().getColor(), 0, 0, packedLight);
        stack.popPose();
        super.render(entity, yRot, partialTicks, stack, buffer, packedLight);
    }

    private static void vertex(IVertexBuilder builder, Matrix4f pose, Matrix3f normal, float x, float y, int color, float u, float v, int packedLight)
    {
        builder.vertex(pose, x, y, 0.0F).color(red(color), green(color), blue(color), 255).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
    }

    @Override
    public ResourceLocation getTextureLocation(ElementalOrbEntity entity)
    {
        return TEXTURE;
    }
}
