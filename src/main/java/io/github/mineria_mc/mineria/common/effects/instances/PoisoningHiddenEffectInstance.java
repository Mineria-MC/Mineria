package io.github.mineria_mc.mineria.common.effects.instances;

import io.github.mineria_mc.mineria.common.capabilities.MineriaCapabilities;
import io.github.mineria_mc.mineria.common.capabilities.ticking_data.TickingDataTypes;
import io.github.mineria_mc.mineria.common.effects.util.IMobEffectInstanceSerializer;
import io.github.mineria_mc.mineria.common.effects.util.PoisonSource;
import io.github.mineria_mc.mineria.common.init.MineriaEffectInstanceSerializers;
import io.github.mineria_mc.mineria.common.init.MineriaItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import javax.annotation.Nonnull;

public class PoisoningHiddenEffectInstance extends ModdedMobEffectInstance {
    protected PoisonSource poisonSource;
    protected boolean isWithPoison;

    public PoisoningHiddenEffectInstance(MobEffect potion, int duration, int amplifier, PoisonSource source) {
        this(potion, duration, duration, amplifier, source);
    }

    public PoisoningHiddenEffectInstance(MobEffect potion, int duration, int maxDuration, int amplifier, PoisonSource source) {
        super(potion, duration, maxDuration, amplifier, false, false, false, false, null);
        this.poisonSource = source;
    }

    public PoisoningHiddenEffectInstance withPoison() {
        this.isWithPoison = true;
        parentEffect = MobEffects.POISON;
        return this;
    }

    public PoisonSource getPoisonSource() {
        return poisonSource;
    }

    @Override
    public boolean update(@Nonnull MobEffectInstance effectInstance) {
        if(effectInstance instanceof PoisoningHiddenEffectInstance instance) {
            this.poisonSource = instance.poisonSource;
            setDuration(instance.getDuration());
            maxDuration = instance.getMaxDuration();
            setAmplifier(instance.getAmplifier());
            parentEffect = instance.getParentEffect();
            return true;
        }
        return false;
    }

    @Override
    public ResourceLocation getSerializerId() {
        return MineriaEffectInstanceSerializers.POISONING_HIDDEN_EFFECT.getId();
    }

    @Override
    public PoisoningHiddenEffectInstance copy() {
        return new PoisoningHiddenEffectInstance(getEffect(), getDuration(), getMaxDuration(), getAmplifier(), getPoisonSource());
    }

    @Override
    public boolean isCurativeItem(ItemStack stack) {
        return isWithPoison ? super.isCurativeItem(stack) : Ingredient.of(MineriaItems.Tags.ANTI_POISONS).test(stack);
    }

    @Override
    public void onEffectRemoved(LivingEntity living) {
        living.getCapability(MineriaCapabilities.TICKING_DATA).ifPresent(cap -> cap.remove(TickingDataTypes.POISON_EXPOSURE, this.poisonSource));
    }

    public static class Serializer implements IMobEffectInstanceSerializer<PoisoningHiddenEffectInstance> {
        @Override
        public void encodePacket(PoisoningHiddenEffectInstance effect, FriendlyByteBuf buf) {
            MineriaEffectInstanceSerializers.MODDED.get().encodePacket(effect, buf);
            buf.writeResourceLocation(effect.poisonSource.getId());
        }

        @Override
        public PoisoningHiddenEffectInstance decodePacket(FriendlyByteBuf buf) {
            ModdedMobEffectInstance modded = MineriaEffectInstanceSerializers.MODDED.get().decodePacket(buf);
            PoisonSource source = PoisonSource.byName(buf.readResourceLocation());
            return new PoisoningHiddenEffectInstance(modded.getEffect(), modded.getDuration(), modded.getMaxDuration(), modded.getAmplifier(), source);
        }

        @Override
        public PoisoningHiddenEffectInstance deserialize(MobEffect effect, CompoundTag nbt) {
            ModdedMobEffectInstance modded = MineriaEffectInstanceSerializers.MODDED.get().deserialize(effect, nbt);
            PoisonSource source = PoisonSource.byName(new ResourceLocation(nbt.getString("PoisonSource")));
            return new PoisoningHiddenEffectInstance(modded.getEffect(), modded.getDuration(), modded.getMaxDuration(), modded.getAmplifier(), source);
        }
    }
}
