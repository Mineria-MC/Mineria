package com.mineria.mod.mixin;

import com.mineria.mod.effects.CustomEffectInstance;
import com.mineria.mod.effects.PoisonEffectInstance;
import com.mineria.mod.effects.PoisonSource;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.network.play.server.SPlayEntityEffectPacket;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ClientPlayNetHandler.class)
public class ClientPlayNetHandlerMixin
{
    @Shadow private Minecraft client;

    @Shadow private ClientWorld world;

    /**
     * @reason Injections do not work
     * @author LGatodu47
     */
    @Overwrite
    public void handleEntityEffect(SPlayEntityEffectPacket packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, (ClientPlayNetHandler) (Object) this, this.client);
        Entity entity = this.world.getEntityByID(packetIn.getEntityId());
        if (entity instanceof LivingEntity)
        {
            byte effectID = ObfuscationReflectionHelper.getPrivateValue(SPlayEntityEffectPacket.class, packetIn, "field_149432_b");
            Effect effect = Effect.get(effectID & 0xFF);
            if (effect != null)
            {
                byte flags = ObfuscationReflectionHelper.getPrivateValue(SPlayEntityEffectPacket.class, packetIn, "field_186985_e");
                boolean showIcon = (flags & 4) == 4;
                boolean shouldRender = packetIn.shouldShowIcon();
                int duration = ObfuscationReflectionHelper.getPrivateValue(SPlayEntityEffectPacket.class, packetIn, "field_149431_d");
                int maxDuration = packetIn.getDuration(); // instead used to get maxDuration
                byte amplifier = ObfuscationReflectionHelper.getPrivateValue(SPlayEntityEffectPacket.class, packetIn, "field_149433_c");

                if(maxDuration > -1)
                {
                    int potionClass = packetIn.getAmplifier(); // instead used to get potionClass
                    int poisonSource = packetIn.getEffectId(); // instead used to get poisonSource

                    if(potionClass > -1 && poisonSource > -1)
                    {
                        PoisonEffectInstance poisonInstance = new PoisonEffectInstance(potionClass, duration, maxDuration, amplifier, PoisonSource.byId(poisonSource));
                        poisonInstance.setPotionDurationMax(packetIn.isMaxDuration());
                        ((LivingEntity) entity).func_233646_e_(poisonInstance);
                    }
                    else
                    {
                        CustomEffectInstance customInstance = CustomEffectInstance.makeCustomEffectInstance(effect, duration, maxDuration, amplifier, packetIn.getIsAmbient(), packetIn.doesShowParticles(), showIcon, shouldRender);
                        customInstance.setPotionDurationMax(packetIn.isMaxDuration());
                        ((LivingEntity) entity).func_233646_e_(customInstance);
                    }
                }
                else
                {
                    EffectInstance instance = CustomEffectInstance.makeEffectInstance(effect, duration, amplifier, packetIn.getIsAmbient(), packetIn.doesShowParticles(), showIcon, shouldRender);
                    instance.setPotionDurationMax(packetIn.isMaxDuration());
                    ((LivingEntity) entity).func_233646_e_(instance);
                }
            }
        }
    }
}
