package com.mineria.mod.client.renderers.entity;

import com.mineria.mod.Mineria;
import com.mineria.mod.client.models.entity.DruidModel;
import com.mineria.mod.common.entity.AbstractDruidEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CrossedArmsItemLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class DruidRenderer<T extends AbstractDruidEntity> extends MobRenderer<T, DruidModel<T>>
{
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(new ResourceLocation(Mineria.MODID, "druid"), "main");

    public DruidRenderer(EntityRendererProvider.Context ctx)
    {
        super(ctx, new DruidModel<>(ctx.bakeLayer(LAYER)), 0.5F);
        this.addLayer(new ItemInHandLayer<>(this)
        {
            @Override
            public void render(PoseStack stack, MultiBufferSource buffer, int p_225628_3_, T entity, float p_225628_5_, float p_225628_6_, float p_225628_7_, float p_225628_8_, float p_225628_9_, float p_225628_10_)
            {
                if (entity.isCastingSpell())
                {
                    super.render(stack, buffer, p_225628_3_, entity, p_225628_5_, p_225628_6_, p_225628_7_, p_225628_8_, p_225628_9_, p_225628_10_);
                }
            }
        });
        this.addLayer(new CrossedArmsItemLayer<>(this)
        {
            @Override
            public void render(PoseStack stack, MultiBufferSource buffer, int p_225628_3_, T entity, float p_225628_5_, float p_225628_6_, float p_225628_7_, float p_225628_8_, float p_225628_9_, float p_225628_10_)
            {
                if (!entity.isCastingSpell())
                {
                    super.render(stack, buffer, p_225628_3_, entity, p_225628_5_, p_225628_6_, p_225628_7_, p_225628_8_, p_225628_9_, p_225628_10_);
                }
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(T entity)
    {
        return new ResourceLocation(Mineria.MODID, "textures/entity/".concat(entity.getType().getRegistryName().getPath()).concat(".png"));
    }
}
