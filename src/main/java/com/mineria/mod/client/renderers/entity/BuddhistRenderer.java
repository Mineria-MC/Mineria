package com.mineria.mod.client.renderers.entity;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.entity.BuddhistEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.VillagerModel;
import net.minecraft.util.ResourceLocation;

public class BuddhistRenderer extends MobRenderer<BuddhistEntity, VillagerModel<BuddhistEntity>>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Mineria.MODID, "textures/entity/buddhist.png");

    public BuddhistRenderer(EntityRendererManager manager)
    {
        super(manager, new VillagerModel<>(0), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(BuddhistEntity entity)
    {
        return TEXTURE;
    }

    @Override
    protected void scale(BuddhistEntity entity, MatrixStack stack, float p_225620_3_)
    {
        stack.scale(0.9375F, 0.9375F, 0.9375F);
    }
}
