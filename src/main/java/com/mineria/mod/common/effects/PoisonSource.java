package com.mineria.mod.common.effects;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.capabilities.CapabilityRegistry;
import com.mineria.mod.common.effects.instances.PoisonEffectInstance;
import com.mineria.mod.common.init.MineriaEffects;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

public class PoisonSource
{
    private static final Map<ResourceLocation, PoisonSource> BY_NAME = new HashMap<>();
    private static final Int2ObjectMap<PoisonSource> ORDINAL = new Int2ObjectOpenHashMap<>();

    public static final long DEFAULT_EXPOSURE_TIME = 12000; // 20 * 60 * 10

    public static final PoisonSource ELDERBERRY = create(new ResourceLocation(Mineria.MODID, "elderberry"), "poison_source.mineria.elderberry").color(4598843).exposureTime(20 * 45).poisonApplied((living, source) -> {
        living.getCapability(CapabilityRegistry.POISON_EXPOSURE).ifPresent(cap -> {
            if(cap.getTicksSinceLastExposure(source) < source.getMaxExposureTime())
            {
                switch (cap.exposureCount(source))
                {
                    case 0:
                        living.addEffect(new EffectInstance(Effects.CONFUSION, 600, 0));
                        break;
                    case 1:
                        living.addEffect(new EffectInstance(Effects.CONFUSION, 1200, 0));
                        break;
                    case 2:
                        living.removeEffect(Effects.CONFUSION);
                        PoisonEffectInstance.applyPoisonEffect(living, 1, 24000, 0, source);
                        break;
                }
            }
            cap.poison(source);
        });
    }).build();
    public static final PoisonSource STRYCHNOS_TOXIFERA = create(new ResourceLocation(Mineria.MODID, "strychnos_toxifera"), "poison_source.mineria.strychnos_toxifera").color(7308866).poisonApplied((living, source) -> {
        PoisonEffectInstance.applyPoisonEffect(living, 3, 24000, 0, source);
        living.getCapability(CapabilityRegistry.POISON_EXPOSURE).ifPresent(cap -> cap.poison(source));
    }).build();
    public static final PoisonSource STRYCHNOS_NUX_VOMICA = create(new ResourceLocation(Mineria.MODID, "strychnos_nux_vomica"), "poison_source.mineria.strychnos_nux_vomica").color(16749430).exposureTime(20 * 60 * 5).poisonApplied((living, source) -> {
        living.getCapability(CapabilityRegistry.POISON_EXPOSURE).ifPresent(cap -> {
            if(cap.getTicksSinceLastExposure(source) < source.getMaxExposureTime())
            {
                switch (cap.exposureCount(source))
                {
                    case 0:
                        PoisonEffectInstance.applyPoisonEffect(living, 1, 24000, 0, source);
                        break;
                    case 1:
                        PoisonEffectInstance.applyPoisonEffect(living, 2, 24000, 0, source);
                        break;
                    case 2:
                        PoisonEffectInstance.applyPoisonEffect(living, 3, 24000, 0, source);
                        break;
                }
            }
            cap.poison(source);
        });
    }).build();
    public static final PoisonSource BELLADONNA = create(new ResourceLocation(Mineria.MODID, "belladonna"), "poison_source.mineria.belladonna").color(11963578).poisonApplied((living, source) -> {
        living.getCapability(CapabilityRegistry.POISON_EXPOSURE).ifPresent(cap -> {
            if(cap.getTicksSinceLastExposure(source) < source.getMaxExposureTime())
            {
                switch (cap.exposureCount(source))
                {
                    case 0:
                        PoisonEffectInstance.applyPoisonEffect(living, 2, 24000, 0, source);
                        break;
                    case 2:
                        PoisonEffectInstance.applyPoisonEffect(living, 3, 24000, 0, source);
                        break;
                }
            }
            cap.poison(source);
        });
    }).build();
    public static final PoisonSource MANDRAKE = create(new ResourceLocation(Mineria.MODID, "mandrake"), "poison_source.mineria.mandrake").color(12224486).poisonApplied((living, source) -> {
        living.getCapability(CapabilityRegistry.POISON_EXPOSURE).ifPresent(cap -> {
            if(cap.getTicksSinceLastExposure(source) < source.getMaxExposureTime())
            {
                switch (cap.exposureCount(source))
                {
                    case 0:
                        PoisonEffectInstance.applyPoisonEffect(living, 2, 24000, 0, source);
                        break;
                    case 1:
                        PoisonEffectInstance.applyPoisonEffect(living, 3, 24000, 0, source);
                        break;
                }
            }
            cap.poison(source);
        });
    }).build();
    public static final PoisonSource YEW = create(new ResourceLocation(Mineria.MODID, "yew"), "poison_source.mineria.yew").color(7476511).exposureTime(20 * 60 * 3).poisonApplied((living, source) -> {
        living.getCapability(CapabilityRegistry.POISON_EXPOSURE).ifPresent(cap -> {
            if(cap.getTicksSinceLastExposure(source) < source.getMaxExposureTime())
            {
                switch (cap.exposureCount(source))
                {
                    case 0:
                        living.addEffect(new EffectInstance(MineriaEffects.NO_NATURAL_REGENERATION.get(), 20 * 60));
                        living.addEffect(new EffectInstance(MineriaEffects.HALLUCINATIONS.get(), 20 * 60));
                        living.addEffect(new EffectInstance(Effects.CONFUSION, 20 * 60));
                        break;
                    case 1:
                        PoisonEffectInstance.applyPoisonEffect(living, 2, 24000, 0, source);
                        living.addEffect(new EffectInstance(MineriaEffects.HALLUCINATIONS.get(), 20 * 45));
                        break;
                    case 2:
                        living.removeEffect(Effects.CONFUSION);
                        PoisonEffectInstance.applyPoisonEffect(living, 3, 24000, 0, source);
                        break;
                }
            }
            cap.poison(source);
        });
    }).build();
    public static final PoisonSource UNKNOWN = create(new ResourceLocation(Mineria.MODID, "unknown"), "poison_source.mineria.unknown").build();

    private static int ordinal = 0;

    private final ResourceLocation id;
    private final String translationKey;
    private final String descriptionTranslationKey;
    private final int color;
    private final long exposureTime;
    private final BiConsumer<LivingEntity, PoisonSource> poisonApplier;

    private PoisonSource(ResourceLocation id, String translationKey, String descriptionTranslationKey, int color, long exposureTime, BiConsumer<LivingEntity, PoisonSource> poisonApplier)
    {
        if(BY_NAME.containsKey(id))
            throw new IllegalArgumentException("PoisonSource with id '" + id + "' already exists!");

        this.id = id;
        this.translationKey = translationKey;
        this.descriptionTranslationKey = descriptionTranslationKey;
        this.color = color;
        this.exposureTime = exposureTime;
        this.poisonApplier = poisonApplier;
        BY_NAME.put(id, this);
        ORDINAL.put(ordinal++, this);
    }

    public ResourceLocation getId()
    {
        return id;
    }

    public String getTranslationKey()
    {
        return this.translationKey;
    }

    public TranslationTextComponent getDescription(int potionClass, int amplifier)
    {
        return new TranslationTextComponent(this.descriptionTranslationKey, potionClass, amplifier);
    }

    public int getColor()
    {
        return color;
    }

    public long getMaxExposureTime()
    {
        return this.exposureTime;
    }

    public void poison(LivingEntity living)
    {
        this.poisonApplier.accept(living, this);
    }

    public int ordinal()
    {
        for(Int2ObjectMap.Entry<PoisonSource> entry : ORDINAL.int2ObjectEntrySet())
        {
            if(entry.getValue().equals(this))
                return entry.getIntKey();
        }

        return -1;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PoisonSource that = (PoisonSource) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id);
    }

    public static PoisonSource byName(ResourceLocation name)
    {
        return BY_NAME.getOrDefault(name, PoisonSource.UNKNOWN);
    }

    @Nullable
    public static PoisonSource byOrdinal(int ordinal)
    {
        return ordinal < 0 ? null : ORDINAL.get(ordinal);
    }

    public static Collection<PoisonSource> getAllPoisonSources()
{
    return BY_NAME.values();
}

    public static PoisonSource.Builder create(ResourceLocation id, String translationKey)
    {
        return new PoisonSource.Builder(id, translationKey);
    }

    private static class Builder
    {
        private final ResourceLocation id;
        private final String translationKey;
        private String descriptionTranslationKey;
        private int color = 5149489;
        private long exposureTime = DEFAULT_EXPOSURE_TIME;
        private BiConsumer<LivingEntity, PoisonSource> poisonApplier = (living, poisonSource) -> {};

        private Builder(ResourceLocation id, String translationKey)
        {
            this.id = id;
            this.translationKey = translationKey;
            this.descriptionTranslationKey = translationKey.concat(".desc");
        }

        public Builder descriptionKey(String descriptionTranslationKey)
        {
            this.descriptionTranslationKey = descriptionTranslationKey;
            return this;
        }

        public Builder color(int color)
        {
            this.color = color;
            return this;
        }

        public Builder exposureTime(long exposureTime)
        {
            this.exposureTime = exposureTime;
            return this;
        }

        public Builder poisonApplied(BiConsumer<LivingEntity, PoisonSource> poisonApplier)
        {
            this.poisonApplier = poisonApplier;
            return this;
        }

        public PoisonSource build()
        {
            return new PoisonSource(id, translationKey, descriptionTranslationKey, color, exposureTime, poisonApplier);
        }
    }
}
