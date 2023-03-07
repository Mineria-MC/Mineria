package io.github.mineria_mc.mineria.client.renderers.entity;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.client.models.entity.DruidicWolfModel;
import io.github.mineria_mc.mineria.common.entity.DruidicWolfEntity;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class DruidicWolfRenderer extends MobRenderer<DruidicWolfEntity, DruidicWolfModel> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Mineria.MODID, "textures/entity/druidic_wolf.png");
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(new ResourceLocation(Mineria.MODID, "druidic_wolf"), "main");

    public DruidicWolfRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new DruidicWolfModel(ctx.bakeLayer(LAYER)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(DruidicWolfEntity entity) {
        return TEXTURE;
    }
}
