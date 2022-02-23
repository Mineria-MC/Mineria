package com.mineria.mod.common.init;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.effects.CustomEffectInstance;
import com.mineria.mod.common.effects.EffectInstanceSerializer;
import com.mineria.mod.common.effects.IEffectInstanceSerializer;
import com.mineria.mod.common.effects.instances.BowelSoundEffectInstance;
import com.mineria.mod.common.effects.instances.PoisonEffectInstance;
import com.mineria.mod.util.DeferredRegisterUtil;
import com.mineria.mod.util.MineriaUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

public class MineriaEffectInstanceSerializers
{
    public static final IForgeRegistry<IEffectInstanceSerializer<?>> REGISTRY = new RegistryBuilder<IEffectInstanceSerializer<?>>()
            .setName(new ResourceLocation(Mineria.MODID, "effect_instance_serializer"))
            .setType(MineriaUtils.<IEffectInstanceSerializer<?>>castClass(IEffectInstanceSerializer.class))
            .disableSaving()
            .create();
    public static final DeferredRegister<IEffectInstanceSerializer<?>> SERIALIZERS = DeferredRegister.create(REGISTRY, Mineria.MODID);

    public static final RegistryObject<IEffectInstanceSerializer<MobEffectInstance>> DEFAULT = SERIALIZERS.register("default", EffectInstanceSerializer::new);
    public static final RegistryObject<IEffectInstanceSerializer<CustomEffectInstance>> CUSTOM = SERIALIZERS.register("custom", CustomEffectInstance.Serializer::new);
    public static final RegistryObject<IEffectInstanceSerializer<PoisonEffectInstance>> POISON = SERIALIZERS.register("poison", PoisonEffectInstance.Serializer::new);
    public static final RegistryObject<IEffectInstanceSerializer<BowelSoundEffectInstance>> BOWEL_SOUNDS = SERIALIZERS.register("bowel_sounds", BowelSoundEffectInstance.Serializer::new);

    public static IEffectInstanceSerializer<?> byName(ResourceLocation location)
    {
        return DeferredRegisterUtil.findEntryByName(MineriaEffectInstanceSerializers.SERIALIZERS, location).orElse(MineriaEffectInstanceSerializers.DEFAULT.get());
    }
}
