package com.mineria.mod.client.renderers.entity;

import com.mineria.mod.Mineria;
import com.mineria.mod.client.models.entity.BrownBearModel;
import com.mineria.mod.common.entity.BrownBearEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

import java.util.Random;

public class BrownBearRenderer extends MobRenderer<BrownBearEntity, BrownBearModel>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Mineria.MODID, "textures/entity/brown_bear.png");
    private static final ResourceLocation VARIANT_TEXTURE = new ResourceLocation(Mineria.MODID, "textures/entity/light_brown_bear.png");

    public BrownBearRenderer(EntityRendererManager manager)
    {
        super(manager, new BrownBearModel(), 0.9F);
    }

    @Override
    public ResourceLocation getTextureLocation(BrownBearEntity entity)
    {
        return entity.getUUID().getMostSignificantBits() % 10 == 0 ? VARIANT_TEXTURE : TEXTURE;
    }

    @Override
    protected void scale(BrownBearEntity entity, MatrixStack stack, float p_225620_3_)
    {
        stack.scale(1.2F, 1.2F, 1.2F);
        super.scale(entity, stack, p_225620_3_);
    }
}
