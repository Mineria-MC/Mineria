package io.github.mineria_mc.mineria.client.renderers.entity;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.client.models.entity.GreatDruidOfGaulsModel;
import io.github.mineria_mc.mineria.common.entity.GreatDruidOfGaulsEntity;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class GreatDruidOfGaulsRenderer extends MobRenderer<GreatDruidOfGaulsEntity, GreatDruidOfGaulsModel> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Mineria.MODID, "textures/entity/great_druid_of_gauls.png");
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(new ResourceLocation(Mineria.MODID, "great_druid_of_gauls"), "main");

    public GreatDruidOfGaulsRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new GreatDruidOfGaulsModel(ctx.bakeLayer(LAYER)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(GreatDruidOfGaulsEntity p_110775_1_) {
        return TEXTURE;
    }
}
