package io.github.mineria_mc.mineria.client.renderers.entity;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.client.models.entity.BrownBearModel;
import io.github.mineria_mc.mineria.common.entity.BrownBearEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class BrownBearRenderer extends MobRenderer<BrownBearEntity, BrownBearModel> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Mineria.MODID, "textures/entity/brown_bear.png");
    private static final ResourceLocation VARIANT_TEXTURE = new ResourceLocation(Mineria.MODID, "textures/entity/light_brown_bear.png");
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(new ResourceLocation(Mineria.MODID, "brown_bear"), "main");

    public BrownBearRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new BrownBearModel(ctx.bakeLayer(LAYER)), 0.9F);
    }

    @Override
    public ResourceLocation getTextureLocation(BrownBearEntity entity) {
        return entity.getUUID().getMostSignificantBits() % 10 == 0 ? VARIANT_TEXTURE : TEXTURE;
    }

    @Override
    protected void scale(BrownBearEntity entity, PoseStack stack, float p_225620_3_) {
        stack.scale(1.2F, 1.2F, 1.2F);
        super.scale(entity, stack, p_225620_3_);
    }
}
