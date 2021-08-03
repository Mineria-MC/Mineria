package com.mineria.mod.effects;

import com.mineria.mod.capabilities.CapabilityRegistry;
import com.mineria.mod.init.ItemsInit;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Arrays;
import java.util.Comparator;

public enum PoisonSource
{
    ELDERBERRY("poison_source.mineria.elderberry", 0, ItemsInit.ELDERBERRY_TEA),
    STRYCHNOS_TOXIFERA("poison_source.mineria.strychnos_toxifera", 1, ItemsInit.STRYCHNOS_TOXIFERA_TEA),
    STRYCHNOS_NUX_VOMICA("poison_source.mineria.strychnos_nux_vomica", 2, ItemsInit.STRYCHNOS_NUX_VOMICA_TEA),
    BELLADONNA("poison_source.mineria.belladonna", 3, ItemsInit.BELLADONNA_TEA),
    MANDRAKE("poison_source.mineria.mandrake", 4, ItemsInit.MANDRAKE_TEA);

    private static final PoisonSource[] VALUES = Arrays.stream(values()).sorted(Comparator.comparingInt(PoisonSource::getId)).toArray(PoisonSource[]::new);

    private final String translationKey;
    private final int id;
    private final Item poisonousItem;

    PoisonSource(String translationKey, int id, Item poisonousItem)
    {
        this.translationKey = translationKey;
        this.id = id;
        this.poisonousItem = poisonousItem;
    }

    public int getId()
    {
        return id;
    }

    public TranslationTextComponent getTranslationComponent(int potionClass, int amplifier)
    {
        return new TranslationTextComponent(this.translationKey, potionClass, amplifier);
    }

    public void onPotionCured(LivingEntity living)
    {
        living.getCapability(CapabilityRegistry.INGESTED_FOOD_CAP).ifPresent(cap -> cap.removeIngestedFood(this.poisonousItem));
    }

    public static PoisonSource byId(int id)
    {
        if (id < 0 || id >= VALUES.length) {
            id = 0;
        }

        return VALUES[id];
    }
}
