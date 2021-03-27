package com.mineria.mod.entity.render;

import com.mineria.mod.References;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SilverfishRenderer;
import net.minecraft.entity.monster.SilverfishEntity;
import net.minecraft.util.ResourceLocation;

public class GoldenSilverfishRenderer extends SilverfishRenderer
{
    public static final ResourceLocation TEXTURES = new ResourceLocation(References.MODID, "textures/entity/golden_fish.png");

    public GoldenSilverfishRenderer(EntityRendererManager renderManagerIn)
    {
        super(renderManagerIn);
    }

    @Override
    public ResourceLocation getEntityTexture(SilverfishEntity entity)
    {
        return TEXTURES;
    }
}
