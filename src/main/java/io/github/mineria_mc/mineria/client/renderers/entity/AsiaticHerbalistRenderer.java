package io.github.mineria_mc.mineria.client.renderers.entity;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.entity.AsiaticHerbalistEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CrossedArmsItemLayer;
import net.minecraft.client.model.VillagerModel;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

public class AsiaticHerbalistRenderer extends MobRenderer<AsiaticHerbalistEntity, VillagerModel<AsiaticHerbalistEntity>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Mineria.MODID, "textures/entity/asiatic_herbalist.png");
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(new ResourceLocation(Mineria.MODID, "asiatic_herbalist"), "main");

    public AsiaticHerbalistRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new VillagerModel<>(ctx.bakeLayer(LAYER)), 0.5F);
        this.addLayer(new CrossedArmsItemLayer<>(this, ctx.getItemInHandRenderer()));
    }

    @Nonnull
    @Override
    public ResourceLocation getTextureLocation(@Nonnull AsiaticHerbalistEntity entity) {
        return TEXTURE;
    }

    @Override
    protected void scale(AsiaticHerbalistEntity entity, PoseStack stack, float p_225620_3_) {
        stack.scale(0.9375F, 0.9375F, 0.9375F);
    }
}
