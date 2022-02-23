package com.mineria.mod.client.renderers.entity;

import com.mineria.mod.common.entity.MineriaAreaEffectCloudEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.resources.ResourceLocation;

public class MineriaAreaEffectCloudRenderer extends EntityRenderer<MineriaAreaEffectCloudEntity>
{
    public MineriaAreaEffectCloudRenderer(EntityRendererProvider.Context ctx)
    {
        super(ctx);
    }

    @Override
    public ResourceLocation getTextureLocation(MineriaAreaEffectCloudEntity entity)
    {
        return InventoryMenu.BLOCK_ATLAS;
    }
}
