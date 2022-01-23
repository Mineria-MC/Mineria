package com.mineria.mod.mixin;

import com.mineria.mod.util.MineriaHooks;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.IPacket;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.ServerPlayNetHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetHandler.class)
public class ServerPlayNetHandlerMixin
{
    @Shadow @Final public NetworkManager connection;

    @Inject(method = "send(Lnet/minecraft/network/IPacket;Lio/netty/util/concurrent/GenericFutureListener;)V", at = @At("HEAD"), cancellable = true)
    public void send(IPacket<?> packetIn, GenericFutureListener<? extends Future<? super Void>> futureListeners, CallbackInfo ci)
    {
        if(MineriaHooks.replacePacket(packetIn, this.connection))
            ci.cancel();
    }
}
