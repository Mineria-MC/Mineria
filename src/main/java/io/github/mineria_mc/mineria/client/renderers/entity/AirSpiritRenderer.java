package io.github.mineria_mc.mineria.client.renderers.entity;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.client.models.entity.AirSpiritModel;
import io.github.mineria_mc.mineria.common.entity.AirSpiritEntity;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class AirSpiritRenderer extends MobRenderer<AirSpiritEntity, AirSpiritModel> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Mineria.MODID, "textures/entity/air_spirit.png");
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(new ResourceLocation(Mineria.MODID, "air_spirit"), "main");

    public AirSpiritRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new AirSpiritModel(ctx.bakeLayer(LAYER)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(AirSpiritEntity entity) {
        return TEXTURE;
    }
}
