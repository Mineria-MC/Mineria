package com.mineria.mod.client.renderers.entity;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.entity.BuddhistEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.model.VillagerModel;
import net.minecraft.resources.ResourceLocation;

public class BuddhistRenderer extends MobRenderer<BuddhistEntity, VillagerModel<BuddhistEntity>>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Mineria.MODID, "textures/entity/buddhist.png");
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(new ResourceLocation(Mineria.MODID, "buddhist"), "main");

    public BuddhistRenderer(EntityRendererProvider.Context ctx)
    {
        super(ctx, new VillagerModel<>(ctx.bakeLayer(LAYER)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(BuddhistEntity entity)
    {
        return TEXTURE;
    }

    @Override
    protected void scale(BuddhistEntity entity, PoseStack stack, float p_225620_3_)
    {
        stack.scale(0.9375F, 0.9375F, 0.9375F);
    }
}
