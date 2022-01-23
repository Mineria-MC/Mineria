package com.mineria.mod.client.renderers.entity;

import com.mineria.mod.Mineria;
import com.mineria.mod.client.models.entity.AirSpiritModel;
import com.mineria.mod.common.entity.AirSpiritEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class AirSpiritRenderer extends MobRenderer<AirSpiritEntity, AirSpiritModel>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Mineria.MODID, "textures/entity/air_spirit.png");

    public AirSpiritRenderer(EntityRendererManager manager)
    {
        super(manager, new AirSpiritModel(), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(AirSpiritEntity entity)
    {
        return TEXTURE;
    }
}
