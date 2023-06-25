package io.github.mineria_mc.mineria.client.renderers.entity;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.client.models.entity.DirtGolemModel;
import io.github.mineria_mc.mineria.common.entity.DirtGolemEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class DirtGolemRenderer extends MobRenderer<DirtGolemEntity, DirtGolemModel> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Mineria.MODID, "textures/entity/dirt_golem.png");
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(new ResourceLocation(Mineria.MODID, "dirt_golem"), "main");

    public DirtGolemRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new DirtGolemModel(ctx.bakeLayer(LAYER)), 0.7F);
    }

    @Override
    protected void setupRotations(DirtGolemEntity golem, PoseStack stack, float p_225621_3_, float p_225621_4_, float p_225621_5_) {
        super.setupRotations(golem, stack, p_225621_3_, p_225621_4_, p_225621_5_);
        if (!((double) golem.walkAnimation.speed() < 0.01D)) {
            float f1 = golem.walkAnimation.position() - golem.walkAnimation.speed() * (1.0F - p_225621_5_) + 6.0F;
            float f2 = (Math.abs(f1 % 13.0F - 6.5F) - 3.25F) / 3.25F;
            stack.mulPose(Axis.ZP.rotationDegrees(6.5F * f2));
        }
    }

    @Override
    public ResourceLocation getTextureLocation(DirtGolemEntity entity) {
        return TEXTURE;
    }
}
