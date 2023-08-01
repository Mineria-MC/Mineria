package io.github.mineria_mc.mineria.common.effects.util;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.capabilities.MineriaCapabilities;
import io.github.mineria_mc.mineria.common.capabilities.ticking_data.ITickingDataCapability;
import io.github.mineria_mc.mineria.common.capabilities.ticking_data.TickingDataTypes;
import io.github.mineria_mc.mineria.common.effects.instances.PoisonMobEffectInstance;
import io.github.mineria_mc.mineria.common.effects.instances.PoisoningHiddenEffectInstance;
import io.github.mineria_mc.mineria.common.init.MineriaEffects;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PoisonSource implements ITickingDataCapability.Candidate {
    private static final Map<ResourceLocation, PoisonSource> BY_NAME = new HashMap<>();
    private static final Int2ObjectMap<PoisonSource> ORDINAL = new Int2ObjectOpenHashMap<>();

    public static final int DEFAULT_POISON_COLOR = 5149489;
    public static final long DEFAULT_EXPOSURE_TIME = 12000; // 10 minutes

    public static final PoisonSource ELDERBERRY = create(new ResourceLocation(Mineria.MODID, "elderberry"), "poison_source.mineria.elderberry")
            .color(4598843)
            .exposureTime(20 * 45)
            .poisonApplied((living, source, exposureCount) -> switch (exposureCount) {
                case 0 -> new MobEffectInstance[] {new PoisoningHiddenEffectInstance(MobEffects.CONFUSION, 600, 0, source)};
                case 1 -> new MobEffectInstance[] {new PoisoningHiddenEffectInstance(MobEffects.CONFUSION, 1200, 0, source)};
                case 2 -> PoisonMobEffectInstance.getPoisonEffects(1, 24000, 0, source);
                default -> new MobEffectInstance[0];
            })
            .build();
    public static final PoisonSource STRYCHNOS_TOXIFERA = create(new ResourceLocation(Mineria.MODID, "strychnos_toxifera"), "poison_source.mineria.strychnos_toxifera")
            .color(7308866)
            .poisonApplied((living, source, exposureCount) -> PoisonMobEffectInstance.getPoisonEffects(3, 24000, 0, source))
            .build();
    public static final PoisonSource STRYCHNOS_NUX_VOMICA = create(new ResourceLocation(Mineria.MODID, "strychnos_nux_vomica"), "poison_source.mineria.strychnos_nux_vomica")
            .color(16749430)
            .exposureTime(20 * 60 * 5)
            .poisonApplied((living, source, exposureCount) -> switch (exposureCount) {
                case 0 -> PoisonMobEffectInstance.getPoisonEffects(1, 24000, 0, source);
                case 1 -> PoisonMobEffectInstance.getPoisonEffects(2, 24000, 0, source);
                case 2 -> PoisonMobEffectInstance.getPoisonEffects(3, 24000, 0, source);
                default -> new MobEffectInstance[0];
            })
            .build();
    public static final PoisonSource BELLADONNA = create(new ResourceLocation(Mineria.MODID, "belladonna"), "poison_source.mineria.belladonna")
            .color(11963578)
            .poisonApplied((living, source, exposureCount) -> switch (exposureCount) {
                case 0 -> PoisonMobEffectInstance.getPoisonEffects(2, 24000, 0, source);
                case 2 -> PoisonMobEffectInstance.getPoisonEffects(3, 24000, 0, source);
                default -> new MobEffectInstance[0];
            })
            .build();
    public static final PoisonSource MANDRAKE = create(new ResourceLocation(Mineria.MODID, "mandrake"), "poison_source.mineria.mandrake")
            .color(12224486)
            .poisonApplied((living, source, exposureCount) -> switch (exposureCount) {
                case 0 -> PoisonMobEffectInstance.getPoisonEffects(2, 24000, 0, source);
                case 1 -> PoisonMobEffectInstance.getPoisonEffects(3, 24000, 0, source);
                default -> new MobEffectInstance[0];
            })
            .build();
    public static final PoisonSource YEW = create(new ResourceLocation(Mineria.MODID, "yew"), "poison_source.mineria.yew")
            .color(7476511)
            .exposureTime(20 * 60 * 3)
            .poisonApplied((living, source, exposureCount) -> switch (exposureCount) {
                case 0 -> new MobEffectInstance[] {
                        new PoisoningHiddenEffectInstance(MineriaEffects.NO_NATURAL_REGENERATION.get(), 20 * 60, 0, source),
                        new PoisoningHiddenEffectInstance(MineriaEffects.HALLUCINATIONS.get(), 20 * 60, 0, source),
                        new PoisoningHiddenEffectInstance(MobEffects.CONFUSION, 20 * 60, 0, source)
                };
                case 1 -> PoisonMobEffectInstance.getPoisonEffects(2, 24000, 0, source,
                        new PoisoningHiddenEffectInstance(MineriaEffects.HALLUCINATIONS.get(), 20 * 45, 0, source).withPoison()
                );
                case 2 -> PoisonMobEffectInstance.getPoisonEffects(3, 24000, 0, source);
                default -> new MobEffectInstance[0];
            })
            .build();
    public static final PoisonSource UNKNOWN = create(new ResourceLocation(Mineria.MODID, "unknown"), "poison_source.mineria.unknown").build();

    private static int ordinalCounter = 0;

    private final ResourceLocation id;
    private final String translationKey;
    private final String descriptionTranslationKey;
    private final int color;
    private final long exposureTime;
    private final PoisonApplier poisonApplier;
    private final int ordinal;

    private PoisonSource(ResourceLocation id, String translationKey, String descriptionTranslationKey, int color, long exposureTime, PoisonApplier poisonApplier) {
        if (BY_NAME.containsKey(id)) {
            throw new IllegalArgumentException("PoisonSource with id '" + id + "' already exists!");
        }

        this.id = id;
        this.translationKey = translationKey;
        this.descriptionTranslationKey = descriptionTranslationKey;
        this.color = color;
        this.exposureTime = exposureTime;
        this.poisonApplier = poisonApplier;
        this.ordinal = ordinalCounter++;
        BY_NAME.put(id, this);
        ORDINAL.put(ordinal(), this);
    }

    public ResourceLocation getId() {
        return id;
    }

    public String getTranslationKey() {
        return this.translationKey;
    }

    public MutableComponent getDescription(int potionClass, int amplifier) {
        return Component.translatable(this.descriptionTranslationKey, potionClass, amplifier);
    }

    public int getColor() {
        return color;
    }

    public long getMaxExposureTime() {
        return this.exposureTime;
    }

    @Override
    public long getTickLimit() {
        return getMaxExposureTime();
    }

    @Override
    public String getSerializationString() {
        return getId().toString();
    }

    public boolean applyPoisoning(LivingEntity living) {
        return living.getCapability(MineriaCapabilities.TICKING_DATA).map(cap -> {
            boolean appliedPoison = false;
            if(cap.ticksSinceStore(TickingDataTypes.POISON_EXPOSURE, this) < this.exposureTime) {
                for (MobEffectInstance effect : this.poisonApplier.getEffects(living, this, cap.occurrences(TickingDataTypes.POISON_EXPOSURE, this))) {
                    living.addEffect(effect);
                    appliedPoison = true;
                }
            }
            cap.store(TickingDataTypes.POISON_EXPOSURE, this);
            return appliedPoison;
        }).orElse(false);
    }

    public int ordinal() {
        return ordinal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PoisonSource that = (PoisonSource) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static PoisonSource byName(ResourceLocation name) {
        return BY_NAME.getOrDefault(name, PoisonSource.UNKNOWN);
    }

    @Nullable
    public static PoisonSource byOrdinal(int ordinal) {
        return ordinal < 0 ? null : ORDINAL.get(ordinal);
    }

    public static Collection<PoisonSource> getAllPoisonSources() {
        return BY_NAME.values();
    }

    public static PoisonSource.Builder create(ResourceLocation id, String translationKey) {
        return new PoisonSource.Builder(id, translationKey);
    }

    public static class Builder {
        private final ResourceLocation id;
        private final String translationKey;
        private String descriptionTranslationKey;
        private int color = DEFAULT_POISON_COLOR;
        private long exposureTime = DEFAULT_EXPOSURE_TIME;
        private PoisonApplier poisonApplier = (living, poisonSource, exposureCount) -> new MobEffectInstance[0];

        private Builder(ResourceLocation id, String translationKey) {
            this.id = id;
            this.translationKey = translationKey;
            this.descriptionTranslationKey = translationKey.concat(".desc");
        }

        public Builder descriptionKey(String descriptionTranslationKey) {
            this.descriptionTranslationKey = descriptionTranslationKey;
            return this;
        }

        public Builder color(int color) {
            this.color = color;
            return this;
        }

        public Builder exposureTime(long exposureTime) {
            this.exposureTime = exposureTime;
            return this;
        }

        public Builder poisonApplied(PoisonApplier poisonApplier) {
            this.poisonApplier = poisonApplier;
            return this;
        }

        public PoisonSource build() {
            return new PoisonSource(id, translationKey, descriptionTranslationKey, color, exposureTime, poisonApplier);
        }
    }

    @FunctionalInterface
    public interface PoisonApplier {
        MobEffectInstance[] getEffects(@Nullable LivingEntity entity, PoisonSource source, int exposureCount);
    }
}
