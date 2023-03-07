package io.github.mineria_mc.mineria.common.init;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.effects.instances.BowelSoundMobEffectInstance;
import io.github.mineria_mc.mineria.common.effects.instances.CustomMobEffectInstance;
import io.github.mineria_mc.mineria.common.effects.instances.ModdedMobEffectInstance;
import io.github.mineria_mc.mineria.common.effects.instances.PoisonMobEffectInstance;
import io.github.mineria_mc.mineria.common.effects.util.MobEffectInstanceSerializer;
import io.github.mineria_mc.mineria.common.effects.util.IMobEffectInstanceSerializer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class MineriaEffectInstanceSerializers {
    public static final DeferredRegister<IMobEffectInstanceSerializer<?>> SERIALIZERS = DeferredRegister.create(MineriaRegistries.Keys.EFFECT_SERIALIZER, Mineria.MODID);

    public static final RegistryObject<IMobEffectInstanceSerializer<MobEffectInstance>> DEFAULT = SERIALIZERS.register("default", MobEffectInstanceSerializer::new);
    public static final RegistryObject<IMobEffectInstanceSerializer<ModdedMobEffectInstance>> MODDED = SERIALIZERS.register("custom", CustomMobEffectInstance.Serializer::new);
    public static final RegistryObject<IMobEffectInstanceSerializer<PoisonMobEffectInstance>> POISON = SERIALIZERS.register("poison", PoisonMobEffectInstance.Serializer::new);
    public static final RegistryObject<IMobEffectInstanceSerializer<BowelSoundMobEffectInstance>> BOWEL_SOUNDS = SERIALIZERS.register("bowel_sounds", BowelSoundMobEffectInstance.Serializer::new);
}
