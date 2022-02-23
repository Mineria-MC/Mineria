package com.mineria.mod.client.renderers.entity;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.entity.FireGolemEntity;
import com.mineria.mod.client.models.entity.FireGolemModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import com.mojang.math.Vector3f;

public class FireGolemRenderer extends MobRenderer<FireGolemEntity, FireGolemModel<FireGolemEntity>>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Mineria.MODID, "textures/entity/fire_golem.png");
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(new ResourceLocation(Mineria.MODID, "fire_golem"), "main");

    public FireGolemRenderer(EntityRendererProvider.Context ctx)
    {
        super(ctx, new FireGolemModel<>(ctx.bakeLayer(LAYER)), 0.7F);
    }

    @Override
    protected void setupRotations(FireGolemEntity golem, PoseStack stack, float p_225621_3_, float p_225621_4_, float p_225621_5_)
    {
        super.setupRotations(golem, stack, p_225621_3_, p_225621_4_, p_225621_5_);
        if (!((double) golem.animationSpeed < 0.01D))
        {
            float f1 = golem.animationPosition - golem.animationSpeed * (1.0F - p_225621_5_) + 6.0F;
            float f2 = (Math.abs(f1 % 13.0F - 6.5F) - 3.25F) / 3.25F;
            stack.mulPose(Vector3f.ZP.rotationDegrees(6.5F * f2));
        }
    }

    @Override
    public ResourceLocation getTextureLocation(FireGolemEntity entity)
    {
        return TEXTURE;
    }
}
