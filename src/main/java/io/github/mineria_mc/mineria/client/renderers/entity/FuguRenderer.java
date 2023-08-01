package io.github.mineria_mc.mineria.client.renderers.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.entity.FuguEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PufferfishBigModel;
import net.minecraft.client.model.PufferfishMidModel;
import net.minecraft.client.model.PufferfishSmallModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class FuguRenderer extends MobRenderer<FuguEntity, EntityModel<FuguEntity>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Mineria.MODID, "textures/entity/fugu.png");

    private final EntityModel<FuguEntity> small;
    private final EntityModel<FuguEntity> mid;
    private final EntityModel<FuguEntity> big = getModel();
    private int puffState = 3;

    public FuguRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new PufferfishBigModel<>(ctx.bakeLayer(ModelLayers.PUFFERFISH_BIG)), 0.2F);
        this.mid = new PufferfishMidModel<>(ctx.bakeLayer(ModelLayers.PUFFERFISH_MEDIUM));
        this.small = new PufferfishSmallModel<>(ctx.bakeLayer(ModelLayers.PUFFERFISH_SMALL));
    }

    public @NotNull ResourceLocation getTextureLocation(@NotNull FuguEntity pEntity) {
        return TEXTURE;
    }

    public void render(FuguEntity entity, float yaw, float partialTicks, @NotNull PoseStack stack, @NotNull MultiBufferSource buffer, int light) {
        int i = entity.getPuffState();
        if (i != this.puffState) {
            if (i == 0) {
                this.model = this.small;
            } else if (i == 1) {
                this.model = this.mid;
            } else {
                this.model = this.big;
            }
        }

        this.puffState = i;
        this.shadowRadius = 0.1F + 0.1F * (float)i;
        super.render(entity, yaw, partialTicks, stack, buffer, light);
    }

    protected void setupRotations(@NotNull FuguEntity entity, PoseStack stack, float age, float yaw, float partialTicks) {
        stack.translate(0.0F, Mth.cos(age * 0.05F) * 0.08F, 0.0F);
        super.setupRotations(entity, stack, age, yaw, partialTicks);
    }
}
