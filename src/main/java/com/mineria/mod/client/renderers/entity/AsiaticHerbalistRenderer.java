package com.mineria.mod.client.renderers.entity;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.entity.AsiaticHerbalistEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CrossedArmsItemLayer;
import net.minecraft.client.renderer.entity.model.VillagerModel;
import net.minecraft.util.ResourceLocation;

public class AsiaticHerbalistRenderer extends MobRenderer<AsiaticHerbalistEntity, VillagerModel<AsiaticHerbalistEntity>>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Mineria.MODID, "textures/entity/asiatic_herbalist.png");

    public AsiaticHerbalistRenderer(EntityRendererManager manager)
    {
        super(manager, new VillagerModel<>(0), 0.5F);
        this.addLayer(new CrossedArmsItemLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(AsiaticHerbalistEntity entity)
    {
        return TEXTURE;
    }

    @Override
    protected void scale(AsiaticHerbalistEntity entity, MatrixStack stack, float p_225620_3_)
    {
        stack.scale(0.9375F, 0.9375F, 0.9375F);
    }
}
