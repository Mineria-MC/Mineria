package com.mineria.mod.client.renderers.entity;

import com.mineria.mod.common.entity.MineriaAreaEffectCloudEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;

public class MineriaAreaEffectCloudRenderer extends EntityRenderer<MineriaAreaEffectCloudEntity>
{
    public MineriaAreaEffectCloudRenderer(EntityRendererManager manager)
    {
        super(manager);
    }

    @Override
    public ResourceLocation getTextureLocation(MineriaAreaEffectCloudEntity entity)
    {
        return PlayerContainer.BLOCK_ATLAS;
    }
}
