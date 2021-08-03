package com.mineria.mod.effects;

import com.mineria.mod.References;
import com.mineria.mod.init.EffectsInit;
import com.mineria.mod.util.MineriaUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.Effect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;

public class BowelSoundEffectInstance extends CustomEffectInstance
{
    public BowelSoundEffectInstance(int duration, int maxDuration, int amplifier, boolean ambient, boolean showParticles, boolean showIcon)
    {
        super(EffectsInit.BOWEL_SOUNDS.get(), duration, maxDuration, amplifier, ambient, showParticles, showIcon);
    }

    @Override
    public boolean tick(LivingEntity living, Runnable onChangedPotionEffect)
    {
        if(this.maxDuration - this.duration == 200)
        {
            living.curePotionEffects(new ItemStack(Items.MILK_BUCKET));
            living.getEntityWorld().playSound(null, living.getPosition(), SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 1.5F, MineriaUtils.randomPitch());
        }

        return super.tick(living, onChangedPotionEffect);
    }

    public static class Serializer implements IEffectInstanceSerializer<BowelSoundEffectInstance>
    {
        @Override
        public ResourceLocation getName()
        {
            return new ResourceLocation("bowel_sounds");
        }

        @Override
        public void encodePacket(BowelSoundEffectInstance effect, PacketBuffer buf)
        {
            IEffectInstanceSerializer.<CustomEffectInstance>getSerializer(new ResourceLocation(References.MODID, "custom")).encodePacket(effect, buf);
        }

        @Override
        public BowelSoundEffectInstance decodePacket(PacketBuffer buf)
        {
            CustomEffectInstance custom = IEffectInstanceSerializer.<CustomEffectInstance>getSerializer(new ResourceLocation(References.MODID, "custom")).decodePacket(buf);

            return new BowelSoundEffectInstance(custom.getDuration(), custom.getMaxDuration(), custom.getAmplifier(), custom.isAmbient(), custom.doesShowParticles(), custom.isShowIcon());
        }

        @Override
        public BowelSoundEffectInstance deserialize(Effect effect, CompoundNBT nbt)
        {
            CustomEffectInstance custom = IEffectInstanceSerializer.<CustomEffectInstance>getSerializer(new ResourceLocation(References.MODID, "custom")).deserialize(effect, nbt);

            return IEffectInstanceSerializer.readCurativeItems(new BowelSoundEffectInstance(custom.getDuration(), custom.getMaxDuration(), custom.getAmplifier(), custom.isAmbient(), custom.doesShowParticles(), custom.isShowIcon()), nbt);
        }
    }
}
