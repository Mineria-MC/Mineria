package com.mineria.mod.mixin;

import com.mineria.mod.util.MineriaHooks;
import net.minecraft.network.play.server.SPlayEntityEffectPacket;
import net.minecraft.potion.EffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SPlayEntityEffectPacket.class)
public class SPlayEntityEffectPacketMixin
{
    @Inject(method = "<init>(ILnet/minecraft/potion/EffectInstance;)V", at = @At("TAIL"))
    public void init(int entityIdIn, EffectInstance effect, CallbackInfo ci)
    {
        MineriaHooks.EFFECTS_FROM_PACKET.put((SPlayEntityEffectPacket) (Object) this, effect);
    }
}
