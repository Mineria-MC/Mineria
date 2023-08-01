package io.github.mineria_mc.mineria.common.capabilities.ticking_data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A capability that merges the old poison capability and element capability.
 */
public interface ITickingDataCapability extends INBTSerializable<CompoundTag> {
    /**
     * Updates the state of the stored elements: removes them if their {@link Candidate#getTickLimit() tick limit} has been reached.
     */
    void tick();

    <T extends Candidate> void store(DataType<T> type, @Nonnull T value);
    
    <T extends Candidate> boolean contains(DataType<T> type, @Nonnull T value);

    /**
     * @return The amount of times the given value was stored without being removed.
     */
    <T extends Candidate> int occurrences(DataType<T> type, @Nonnull T value);

    /**
     * @return The number of ticks elapsed since the last store of the value.
     */
    <T extends Candidate> long ticksSinceStore(DataType<T> type, @Nonnull T value);

    <T extends Candidate> void remove(DataType<T> type, @Nonnull T value);

    /**
     * Updates the old {@code mineria:poison_exposure} and {@code mineria:element_exposure} into entries of this capability.<br>
     * Called when LivingEntity capabilities are loaded.
     * @param type The type of data associated with the specified map.
     * @param exposureMap A compound holding the map data.
     * @param <T> The type of data to store (specified by `type`)
     */
    <T extends Candidate> void updateLegacyCapability(DataType<T> type, CompoundTag exposureMap);

    /**
     * Record defining a type of data to store.
     * @see TickingDataTypes
     * @param id The unique identifier of the type of data.
     * @param candidateById A function that maps strings from serialization into a value of type T.
     * @param filter A filter deciding which values should be stored or not.
     * @param <T> The type of value to store in the map.
     */
    record DataType<T extends Candidate>(ResourceLocation id, Function<String, @Nullable T> candidateById, Predicate<T> filter) {
        private static final Map<ResourceLocation, DataType<?>> DATA_TYPES = new HashMap<>();

        public DataType {
            DATA_TYPES.putIfAbsent(id, this);
        }

        public DataType(ResourceLocation id, Function<String, @Nullable T> candidateById) {
            this(id, candidateById, e -> true);
        }

        @Nullable
        public static DataType<?> byId(ResourceLocation id) {
            return DATA_TYPES.get(id);
        }
    }

    interface Candidate {
        String getSerializationString();

        long getTickLimit();
    }
}
