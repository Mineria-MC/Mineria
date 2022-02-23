package com.mineria.mod.client.renderers.entity;

import com.mineria.mod.Mineria;
import com.mineria.mod.client.models.entity.WaterSpiritModel;
import com.mineria.mod.common.entity.WaterSpiritEntity;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class WaterSpiritRenderer extends MobRenderer<WaterSpiritEntity, WaterSpiritModel>
{
    private static final ResourceLocation WATER_TEXTURE = new ResourceLocation(Mineria.MODID, "textures/entity/water_spirit.png");
    private static final ResourceLocation FROZEN_WATER_TEXTURE = new ResourceLocation(Mineria.MODID, "textures/entity/frozen_water_spirit.png");
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(new ResourceLocation(Mineria.MODID, "water_spirit"), "main");

    public WaterSpiritRenderer(EntityRendererProvider.Context ctx)
    {
        super(ctx, new WaterSpiritModel(ctx.bakeLayer(LAYER)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(WaterSpiritEntity entity)
    {
        return entity.isFrozen() ? FROZEN_WATER_TEXTURE : WATER_TEXTURE;
    }
}
