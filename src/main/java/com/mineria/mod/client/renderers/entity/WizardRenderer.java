package com.mineria.mod.client.renderers.entity;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.entity.WizardEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.model.WitchModel;
import net.minecraft.resources.ResourceLocation;

public class WizardRenderer extends MobRenderer<WizardEntity, WitchModel<WizardEntity>>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Mineria.MODID, "textures/entity/wizard.png");
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(new ResourceLocation(Mineria.MODID, "wizard"), "main");

    public WizardRenderer(EntityRendererProvider.Context ctx)
    {
        super(ctx, new WitchModel<>(ctx.bakeLayer(LAYER)), 0.5F);
    }

    @Override
    public void render(WizardEntity entity, float p_225623_2_, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight)
    {
        this.model.setHoldingItem(!entity.getMainHandItem().isEmpty());
        super.render(entity, p_225623_2_, partialTicks, stack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(WizardEntity p_110775_1_)
    {
        return TEXTURE;
    }

    @Override
    protected void scale(WizardEntity entity, PoseStack stack, float partialTicks)
    {
        stack.scale(0.9375F, 0.9375F, 0.9375F);
    }
}
