package com.mineria.mod.common.init;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.enchantments.FourElementsEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MineriaEnchantments
{
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, Mineria.MODID);

    public static final RegistryObject<FourElementsEnchantment> FOUR_ELEMENTS = ENCHANTMENTS.register("four_elements", () -> new FourElementsEnchantment(FourElementsEnchantment.ElementType.NONE));
    public static final RegistryObject<FourElementsEnchantment> FIRE_ELEMENT = ENCHANTMENTS.register("fire_element", () -> new FourElementsEnchantment(FourElementsEnchantment.ElementType.FIRE));
    public static final RegistryObject<FourElementsEnchantment> WATER_ELEMENT = ENCHANTMENTS.register("water_element", () -> new FourElementsEnchantment(FourElementsEnchantment.ElementType.WATER));
    public static final RegistryObject<FourElementsEnchantment> AIR_ELEMENT = ENCHANTMENTS.register("air_element", () -> new FourElementsEnchantment(FourElementsEnchantment.ElementType.AIR));
    public static final RegistryObject<FourElementsEnchantment> GROUND_ELEMENT = ENCHANTMENTS.register("ground_element", () -> new FourElementsEnchantment(FourElementsEnchantment.ElementType.GROUND));
}
