package io.github.mineria_mc.mineria.common.init;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.effects.util.IMobEffectInstanceSerializer;
import io.github.mineria_mc.mineria.common.world.feature.structure.data_markers.IDataMarkerHandler;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

public final class MineriaRegistries {
    private static final RegistryBuilder<IMobEffectInstanceSerializer<?>> EFFECT_SERIALIZER_BUILDER = new RegistryBuilder<IMobEffectInstanceSerializer<?>>().setDefaultKey(new ResourceLocation(Mineria.MODID, "default")).disableSaving();
    public static final Supplier<IForgeRegistry<IMobEffectInstanceSerializer<?>>> EFFECT_SERIALIZERS = MineriaEffectInstanceSerializers.SERIALIZERS.makeRegistry(() -> EFFECT_SERIALIZER_BUILDER);

    private static final RegistryBuilder<IDataMarkerHandler> DATA_MARKER_HANDLER_BUILDER = new RegistryBuilder<IDataMarkerHandler>().setDefaultKey(new ResourceLocation(Mineria.MODID, "none")).disableSaving();
    public static final Supplier<IForgeRegistry<IDataMarkerHandler>> DATA_MARKER_HANDLERS = MineriaDataMarkerHandlers.HANDLERS.makeRegistry(() -> DATA_MARKER_HANDLER_BUILDER);

    public static final class Keys {
        public static final ResourceKey<Registry<IMobEffectInstanceSerializer<?>>> EFFECT_SERIALIZER = key("effect_instance_serializer");
        public static final ResourceKey<Registry<IDataMarkerHandler>> DATA_MARKER_HANDLER = key("data_marker_handler");

        private static <T> ResourceKey<Registry<T>> key(String name) {
            return ResourceKey.createRegistryKey(new ResourceLocation(Mineria.MODID, name));
        }

        public static void init() {
        }
    }

    public static void init() {
        Keys.init();
    }
}
