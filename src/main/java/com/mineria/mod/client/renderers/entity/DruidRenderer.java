package com.mineria.mod.client.renderers.entity;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.entity.AbstractDruidEntity;
import com.mineria.mod.client.models.entity.DruidModel;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CrossedArmsItemLayer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.util.ResourceLocation;

public class DruidRenderer<T extends AbstractDruidEntity> extends MobRenderer<T, DruidModel<T>>
{
    public DruidRenderer(EntityRendererManager manager)
    {
        super(manager, new DruidModel<>(0, 0, 64, 64), 0.5F);
        this.addLayer(new HeldItemLayer<T, DruidModel<T>>(this) {
            @Override
            public void render(MatrixStack stack, IRenderTypeBuffer buffer, int p_225628_3_, T entity, float p_225628_5_, float p_225628_6_, float p_225628_7_, float p_225628_8_, float p_225628_9_, float p_225628_10_)
            {
                if (entity.isCastingSpell())
                {
                    super.render(stack, buffer, p_225628_3_, entity, p_225628_5_, p_225628_6_, p_225628_7_, p_225628_8_, p_225628_9_, p_225628_10_);
                }
            }
        });
        this.addLayer(new CrossedArmsItemLayer<T, DruidModel<T>>(this) {
            @Override
            public void render(MatrixStack stack, IRenderTypeBuffer buffer, int p_225628_3_, T entity, float p_225628_5_, float p_225628_6_, float p_225628_7_, float p_225628_8_, float p_225628_9_, float p_225628_10_)
            {
                if(!entity.isCastingSpell())
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
