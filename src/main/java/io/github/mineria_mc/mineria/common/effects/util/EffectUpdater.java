package io.github.mineria_mc.mineria.common.effects.util;

import io.github.mineria_mc.mineria.common.effects.instances.ModdedMobEffectInstance;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.ObjIntConsumer;
import java.util.function.ToIntFunction;

public class EffectUpdater<T extends ModdedMobEffectInstance> {
    private final List<Entry<T>> orderedEntries;
    private final Set<Entry<T>> entries;

    public EffectUpdater() {
        this.orderedEntries = new ArrayList<>();
        this.entries = new HashSet<>();
    }

    public EffectUpdater<T> compareOrdered(int comparisonOrder, Comparator<T> comparator, BiConsumer<T, T> setter) {
        orderedEntries.add(comparisonOrder, new Entry<>(comparator, setter));
        return this;
    }

    public EffectUpdater<T> compare(Comparator<T> comparator, BiConsumer<T, T> setter) {
        this.entries.add(new Entry<>(comparator, setter));
        return this;
    }

    public EffectUpdater<T> compareOrderedInts(int comparisonOrder, ToIntFunction<T> getter, ObjIntConsumer<T> setter) {
        return compareOrdered(comparisonOrder, Comparator.comparingInt(getter), (inst, other) -> setter.accept(inst, getter.applyAsInt(other)));
    }

    public EffectUpdater<T> compareBooleans(Function<T, Boolean> getter, BiConsumer<T, Boolean> setter) {
        return compare((o1, o2) -> Boolean.compare(getter.apply(o1), getter.apply(o2)), (inst, other) -> setter.accept(inst, getter.apply(other)));
    }

    public boolean updateEffect(T instance, T other) {
        boolean combined = false;
        for (int i = 0; i < this.orderedEntries.size(); i++) {
            Entry<T> entry = this.orderedEntries.get(i);
            if (entry.comparator().compare(other, instance) > 0) {
                if (checkComparators(this.orderedEntries, i, other, instance)) {
                    combined = true;
                    for (int j = i; j < this.orderedEntries.size(); j++) {
                        this.orderedEntries.get(j).setter().accept(instance, other);
                    }
                    break;
                }
            }
        }

        for (Entry<T> entry : this.entries) {
            if (entry.comparator().compare(other, instance) != 0) {
                combined = true;
                entry.setter().accept(instance, other);
            }
        }

        return combined;
    }

    public EffectUpdater<T> copy() {
        EffectUpdater<T> result = new EffectUpdater<>();
        result.orderedEntries.addAll(this.orderedEntries);
        result.entries.addAll(this.entries);
        return result;
    }

    private static <T extends ModdedMobEffectInstance> boolean checkComparators(List<Entry<T>> comparators, int index, T comparing, T compared) {
        if (index > 0) {
            if (comparators.get(index).comparator().compare(comparing, compared) == 0) {
                return checkComparators(comparators, index - 1, comparing, compared);
            }
            return false;
        }
        return true;
    }

    protected record Entry<T>(Comparator<T> comparator, BiConsumer<T, T> setter) {
    }
}
