package io.github.mineria_mc.mineria.mixin;

import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@SuppressWarnings("UnusedMixin") // Applies in dev mode
@Mixin(DefaultPlayerSkin.class)
public class DefaultPlayerSkinMixin {
    private static final ResourceLocation STEVE = new ResourceLocation("textures/entity/player/wide/steve.png");

    @Inject(method = "getDefaultSkin(Ljava/util/UUID;)Lnet/minecraft/resources/ResourceLocation;", at = @At("HEAD"), cancellable = true)
    private static void mineria$inject_getDefaultSkin(UUID playerUUID, CallbackInfoReturnable<ResourceLocation> cir) {
        cir.setReturnValue(STEVE);
    }

    @Inject(method = "getSkinModelName", at = @At("HEAD"), cancellable = true)
    private static void mineria$inject_getSkinModelName(UUID playerUUID, CallbackInfoReturnable<String> cir) {
        cir.setReturnValue("default");
    }
}
