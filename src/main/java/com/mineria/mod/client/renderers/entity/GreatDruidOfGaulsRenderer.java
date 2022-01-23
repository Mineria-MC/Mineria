package com.mineria.mod.client.renderers.entity;

import com.mineria.mod.Mineria;
import com.mineria.mod.client.models.entity.GreatDruidOfGaulsModel;
import com.mineria.mod.common.entity.GreatDruidOfGaulsEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class GreatDruidOfGaulsRenderer extends MobRenderer<GreatDruidOfGaulsEntity, GreatDruidOfGaulsModel>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Mineria.MODID, "textures/entity/great_druid_of_gauls.png");

    public GreatDruidOfGaulsRenderer(EntityRendererManager manager)
    {
        super(manager, new GreatDruidOfGaulsModel(0, 0, 64, 64), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(GreatDruidOfGaulsEntity p_110775_1_)
    {
        return TEXTURE;
    }
}
