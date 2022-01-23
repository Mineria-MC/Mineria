package com.mineria.mod.client.renderers.entity;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.entity.KunaiEntity;
import com.mineria.mod.client.models.entity.KunaiModel;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

public class KunaiRenderer extends EntityRenderer<KunaiEntity>
{
    public static final ResourceLocation TEXTURE = new ResourceLocation(Mineria.MODID, "textures/entity/kunai.png");
    private final KunaiModel model = new KunaiModel();

    public KunaiRenderer(EntityRendererManager manager)
    {
        super(manager);
    }

    @Override
    public void render(KunaiEntity entity, float yRot, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int packedLight)
    {
        stack.pushPose();
        stack.mulPose(Vector3f.YP.rotationDegrees(MathHelper.lerp(partialTicks, entity.yRotO, entity.yRot) - 90.0F));
        stack.mulPose(Vector3f.ZP.rotationDegrees(MathHelper.lerp(partialTicks, entity.xRotO, entity.xRot) + 90.0F));
        IVertexBuilder vertex = ItemRenderer.getFoilBufferDirect(buffer, this.model.renderType(this.getTextureLocation(entity)), false, entity.isEnchanted());
        this.model.renderToBuffer(stack, vertex, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        stack.popPose();
        super.render(entity, yRot, partialTicks, stack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(KunaiEntity entity)
    {
        return TEXTURE;
    }
}
