package com.mineria.mod.init;

import com.mineria.mod.References;
import com.mineria.mod.effects.BowelSoundsEffect;
import net.minecraft.potion.Effect;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EffectsInit
{
    public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, References.MODID);

    public static final RegistryObject<BowelSoundsEffect> BOWEL_SOUNDS = EFFECTS.register("bowel_sounds", BowelSoundsEffect::new);
}
