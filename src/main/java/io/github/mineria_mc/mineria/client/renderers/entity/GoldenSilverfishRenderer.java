package io.github.mineria_mc.mineria.client.renderers.entity;

import io.github.mineria_mc.mineria.Mineria;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SilverfishRenderer;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.resources.ResourceLocation;

public class GoldenSilverfishRenderer extends SilverfishRenderer {
    public static final ResourceLocation TEXTURES = new ResourceLocation(Mineria.MODID, "textures/entity/golden_fish.png");

    public GoldenSilverfishRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public ResourceLocation getTextureLocation(Silverfish entity) {
        return TEXTURES;
    }
}
