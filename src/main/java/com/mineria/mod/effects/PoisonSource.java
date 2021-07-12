package com.mineria.mod.effects;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Arrays;
import java.util.Comparator;

public enum PoisonSource
{
    ELDERBERRY("poison_source.mineria.elderberry", 0),
    STRYCHNOS_TOXIFERA("poison_source.mineria.strychnos_toxifera", 1),
    STRYCHNOS_NUX_VOMICA("poison_source.mineria.strychnos_nux_vomica", 2),
    BELLADONNA("poison_source.mineria.belladonna", 3),
    MANDRAKE("poison_source.mineria.mandrake", 4);

    private static final PoisonSource[] VALUES = Arrays.stream(values()).sorted(Comparator.comparingInt(PoisonSource::getId)).toArray(PoisonSource[]::new);

    private final String translationKey;
    private final int id;

    PoisonSource(String translationKey, int id)
    {
        this.translationKey = translationKey;
        this.id = id;
    }

    public int getId()
    {
        return id;
    }

    public ITextComponent getTranslationComponent(int potionClass, int amplifier)
    {
        return new TranslationTextComponent(this.translationKey, potionClass, amplifier);
    }

    public static PoisonSource byId(int id)
    {
        if (id < 0 || id >= VALUES.length) {
            id = 0;
        }

        return VALUES[id];
    }
}
