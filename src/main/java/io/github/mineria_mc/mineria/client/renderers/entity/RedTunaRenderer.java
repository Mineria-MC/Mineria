package io.github.mineria_mc.mineria.client.renderers.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.entity.RedTunaEntity;
import net.minecraft.client.model.SalmonModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class RedTunaRenderer extends MobRenderer<RedTunaEntity, SalmonModel<RedTunaEntity>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Mineria.MODID, "textures/entity/red_tuna.png");

    public RedTunaRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new SalmonModel<>(ctx.bakeLayer(ModelLayers.SALMON)), 0.52F);
    }

    @Override
    protected void scale(@NotNull RedTunaEntity entity, @NotNull PoseStack stack, float partialTicks) {
        stack.scale(1.3F, 1.3F, 1.3F);
        super.scale(entity, stack, partialTicks);
    }

    @Override
    protected void setupRotations(@NotNull RedTunaEntity entity, @NotNull PoseStack stack, float ageInTicks, float yaw, float partialTicks) {
        super.setupRotations(entity, stack, ageInTicks, yaw, partialTicks);
        float f = 1.0F;
        float f1 = 1.0F;
        if (!entity.isInWater()) {
            f = 1.3F;
            f1 = 1.7F;
        }

        float f2 = f * 4.3F * Mth.sin(f1 * 0.6F * ageInTicks);
        stack.mulPose(Axis.YP.rotationDegrees(f2));
        stack.translate(0.0F, 0.0F, -0.4F);
        if (!entity.isInWater()) {
            stack.translate(0.2F, 0.1F, 0.0F);
            stack.mulPose(Axis.ZP.rotationDegrees(90.0F));
        }
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull RedTunaEntity entity) {
        return TEXTURE;
    }
}
