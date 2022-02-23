package com.mineria.mod.mixin;

import com.mineria.mod.util.MineriaHooks;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientboundUpdateMobEffectPacket.class)
public class ClientboundUpdateMobEffectPacketMixin
{
    @Inject(method = "<init>(ILnet/minecraft/world/effect/MobEffectInstance;)V", at = @At("TAIL"))
    public void init(int entityIdIn, MobEffectInstance effect, CallbackInfo ci)
    {
        MineriaHooks.EFFECTS_FROM_PACKET.put((ClientboundUpdateMobEffectPacket) (Object) this, effect);
    }
}
