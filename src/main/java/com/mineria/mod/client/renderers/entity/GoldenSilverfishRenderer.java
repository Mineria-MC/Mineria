package com.mineria.mod.client.renderers.entity;

import com.mineria.mod.Mineria;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SilverfishRenderer;
import net.minecraft.entity.monster.SilverfishEntity;
import net.minecraft.util.ResourceLocation;

public class GoldenSilverfishRenderer extends SilverfishRenderer
{
    public static final ResourceLocation TEXTURES = new ResourceLocation(Mineria.MODID, "textures/entity/golden_fish.png");

    public GoldenSilverfishRenderer(EntityRendererManager renderManagerIn)
    {
        super(renderManagerIn);
    }

    @Override
    public ResourceLocation getTextureLocation(SilverfishEntity entity)
    {
        return TEXTURES;
    }
}
