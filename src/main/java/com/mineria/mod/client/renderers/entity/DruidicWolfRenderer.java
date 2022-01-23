package com.mineria.mod.client.renderers.entity;

import com.mineria.mod.Mineria;
import com.mineria.mod.client.models.entity.DruidicWolfModel;
import com.mineria.mod.common.entity.DruidicWolfEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class DruidicWolfRenderer extends MobRenderer<DruidicWolfEntity, DruidicWolfModel>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Mineria.MODID, "textures/entity/druidic_wolf.png");

    public DruidicWolfRenderer(EntityRendererManager manager)
    {
        super(manager, new DruidicWolfModel(), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(DruidicWolfEntity entity)
    {
        return TEXTURE;
    }
}
