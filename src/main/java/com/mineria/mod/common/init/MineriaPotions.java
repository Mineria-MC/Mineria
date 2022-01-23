package com.mineria.mod.common.init;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.effects.potions.MineriaPotion;
import com.mineria.mod.common.effects.instances.PoisonEffectInstance;
import com.mineria.mod.common.effects.PoisonSource;
import com.mineria.mod.common.effects.potions.YewPoisoningPotion;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MineriaPotions
{
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTION_TYPES, Mineria.MODID);

    public static final RegistryObject<MineriaPotion> CLASS_2_POISON = POTIONS.register("class_two_poison", () -> new MineriaPotion("class_two_poison", PoisonEffectInstance.getEffects(2, 60 * 20, 0, PoisonSource.UNKNOWN).toArray(new EffectInstance[0])));
    public static final RegistryObject<MineriaPotion> CLASS_3_POISON = POTIONS.register("class_three_poison", () -> new MineriaPotion("class_three_poison", PoisonEffectInstance.getEffects(3, 25 * 20, 0, PoisonSource.UNKNOWN).toArray(new EffectInstance[0])));
    public static final RegistryObject<MineriaPotion> VAMPIRE = POTIONS.register("vampire", () -> new MineriaPotion("vampire", new EffectInstance(MineriaEffects.VAMPIRE.get(), 90 * 20, 0)));
    public static final RegistryObject<MineriaPotion> LONG_VAMPIRE = POTIONS.register("long_vampire", () -> new MineriaPotion("vampire", new EffectInstance(MineriaEffects.VAMPIRE.get(), 180 * 20, 0)));
    public static final RegistryObject<MineriaPotion> STRONG_VAMPIRE = POTIONS.register("strong_vampire", () -> new MineriaPotion("vampire", new EffectInstance(MineriaEffects.VAMPIRE.get(), 60 * 20, 1)));
    public static final RegistryObject<MineriaPotion> VERY_STRONG_VAMPIRE = POTIONS.register("very_strong_vampire", () -> new MineriaPotion("vampire", new EffectInstance(MineriaEffects.VAMPIRE.get(), 45 * 20, 2)));
    public static final RegistryObject<MineriaPotion> YEW_POISONING = POTIONS.register("yew_poisoning", YewPoisoningPotion::new);
}