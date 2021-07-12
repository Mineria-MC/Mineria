package com.mineria.mod.mixin;

import com.mineria.mod.effects.CustomEffectInstance;
import com.mineria.mod.effects.PoisonEffectInstance;
import net.minecraft.network.play.server.SPlayEntityEffectPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SPlayEntityEffectPacket.class)
public class SPlayEntityEffectPacketMixin
{
    private int potionClass = -1;
    private int poisonSource = -1;
    private int maxDuration = -1;
    private boolean shouldRender;

    @Inject(method = "<init>(ILnet/minecraft/potion/EffectInstance;)V", at = @At("TAIL"))
    public void init(int entityIdIn, EffectInstance effect, CallbackInfo ci)
    {
        this.shouldRender = effect.shouldRender();

        if(effect instanceof CustomEffectInstance)
            this.maxDuration = ((CustomEffectInstance) effect).getMaxDuration();

        if(effect instanceof PoisonEffectInstance)
        {
            this.potionClass = ((PoisonEffectInstance) effect).getPotionClass();
            this.poisonSource = ((PoisonEffectInstance) effect).getPoisonSource().getId();
        }
    }

    /**
     * @reason Include poisonSource
     * @author LGatodu47
     */
    @OnlyIn(Dist.CLIENT)
    @Overwrite
    public byte getEffectId()
    {
        return (byte) this.poisonSource;
    }

    /**
     * @reason Include potionClass
     * @author LGatodu47
     */
    @OnlyIn(Dist.CLIENT)
    @Overwrite
    public byte getAmplifier()
    {
        return (byte) this.potionClass;
    }

    /**
     * @reason Include maxDuration
     * @author LGatodu47
     */
    @OnlyIn(Dist.CLIENT)
    @Overwrite
    public int getDuration()
    {
        return this.maxDuration;
    }

    /**
     * @reason Include shouldRender
     * @author LGatodu47
     */
    @OnlyIn(Dist.CLIENT)
    @Overwrite
    public boolean shouldShowIcon()
    {
        return this.shouldRender;
    }
}
