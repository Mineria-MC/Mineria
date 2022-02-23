package com.mineria.mod.client.renderers.entity;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.entity.KunaiEntity;
import com.mineria.mod.client.models.entity.KunaiModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import com.mojang.math.Vector3f;

public class KunaiRenderer extends EntityRenderer<KunaiEntity>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Mineria.MODID, "textures/entity/kunai.png");
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(new ResourceLocation(Mineria.MODID, "kunai"), "main");
    private final KunaiModel model;

    public KunaiRenderer(EntityRendererProvider.Context ctx)
    {
        super(ctx);
        this.model = new KunaiModel(ctx.bakeLayer(LAYER));
    }

    @Override
    public void render(KunaiEntity entity, float yRot, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight)
    {
        stack.pushPose();
        stack.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - 90.0F));
        stack.mulPose(Vector3f.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot()) + 90.0F));
        VertexConsumer vertex = ItemRenderer.getFoilBufferDirect(buffer, this.model.renderType(this.getTextureLocation(entity)), false, entity.isEnchanted());
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
