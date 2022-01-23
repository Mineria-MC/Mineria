package com.mineria.mod.client.renderers.entity;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.entity.WizardEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.WitchModel;
import net.minecraft.util.ResourceLocation;

public class WizardRenderer extends MobRenderer<WizardEntity, WitchModel<WizardEntity>>
{
    public static final ResourceLocation TEXTURE = new ResourceLocation(Mineria.MODID, "textures/entity/wizard.png");

    public WizardRenderer(EntityRendererManager manager)
    {
        super(manager, new WitchModel<>(0F), 0.5F);
    }

    @Override
    public void render(WizardEntity entity, float p_225623_2_, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int packedLight)
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
    protected void scale(WizardEntity entity, MatrixStack stack, float partialTicks)
    {
        stack.scale(0.9375F, 0.9375F, 0.9375F);
    }
}
