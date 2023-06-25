package io.github.mineria_mc.mineria.mixin;

import io.github.mineria_mc.mineria.client.mixin_extensions.PlayerItemInHandLayerExtension;
import io.github.mineria_mc.mineria.client.models.BlowgunModel;
import io.github.mineria_mc.mineria.common.items.IMineriaItem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.PlayerItemInHandLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerItemInHandLayer.class)
public class PlayerItemInHandLayerMixin<T extends Player, M extends EntityModel<T> & ArmedModel & HeadedModel> extends ItemInHandLayer<T, M> implements PlayerItemInHandLayerExtension {
    @Unique
    private BlowgunModel mineria$blowgunModel;

    public PlayerItemInHandLayerMixin(RenderLayerParent<T, M> pRenderer, ItemInHandRenderer itemInHandRenderer) {
        super(pRenderer, itemInHandRenderer);
    }

    @Unique
    @Override
    public void mineria$createBlowgunModel(EntityModelSet set) {
        mineria$blowgunModel = new BlowgunModel(set.bakeLayer(BlowgunModel.LAYER));
    }

    @Inject(method = "renderArmWithItem", at = @At("HEAD"), cancellable = true)
    private void mineria$inject_renderArmWithItem(LivingEntity living, ItemStack stack, ItemDisplayContext displayContext, HumanoidArm arm, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        if(mineria$blowgunModel != null && stack.getItem() instanceof IMineriaItem mineriaItem && mineriaItem.rendersOnHead() && living.getUseItem() == stack && living.swingTime == 0) {
            renderArmWithItem(poseStack, buffer, packedLight);
            ci.cancel();
        }
    }

    private void renderArmWithItem(PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        ModelPart modelpart = getParentModel().getHead();
        float f = modelpart.xRot;
        modelpart.xRot = Mth.clamp(modelpart.xRot, (-(float)Math.PI / 6F), ((float)Math.PI / 2F));
        modelpart.translateAndRotate(poseStack);
        modelpart.xRot = f;
        poseStack.translate(0, 24 / 16f, 0);
        mineria$blowgunModel.renderToBuffer(poseStack, buffer.getBuffer(mineria$blowgunModel.getDefaultRenderType()), packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        poseStack.popPose();
    }
}
