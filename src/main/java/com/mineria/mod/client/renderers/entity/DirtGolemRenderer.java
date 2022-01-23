package com.mineria.mod.client.renderers.entity;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.entity.DirtGolemEntity;
import com.mineria.mod.client.models.entity.DirtGolemModel;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

public class DirtGolemRenderer extends MobRenderer<DirtGolemEntity, DirtGolemModel>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Mineria.MODID, "textures/entity/dirt_golem.png");

    public DirtGolemRenderer(EntityRendererManager manager)
    {
        super(manager, new DirtGolemModel(), 0.7F);
    }

    @Override
    protected void setupRotations(DirtGolemEntity golem, MatrixStack stack, float p_225621_3_, float p_225621_4_, float p_225621_5_)
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
    public ResourceLocation getTextureLocation(DirtGolemEntity p_110775_1_)
    {
        return TEXTURE;
    }
}
