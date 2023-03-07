package io.github.mineria_mc.mineria.common.init;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.enchantments.ElementType;
import io.github.mineria_mc.mineria.common.enchantments.FourElementsEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MineriaEnchantments {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, Mineria.MODID);

    public static final RegistryObject<FourElementsEnchantment> FOUR_ELEMENTS = ENCHANTMENTS.register("four_elements", () -> new FourElementsEnchantment(ElementType.NONE));
    public static final RegistryObject<FourElementsEnchantment> FIRE_ELEMENT = ENCHANTMENTS.register("fire_element", () -> new FourElementsEnchantment(ElementType.FIRE));
    public static final RegistryObject<FourElementsEnchantment> WATER_ELEMENT = ENCHANTMENTS.register("water_element", () -> new FourElementsEnchantment(ElementType.WATER));
    public static final RegistryObject<FourElementsEnchantment> AIR_ELEMENT = ENCHANTMENTS.register("air_element", () -> new FourElementsEnchantment(ElementType.AIR));
    public static final RegistryObject<FourElementsEnchantment> GROUND_ELEMENT = ENCHANTMENTS.register("ground_element", () -> new FourElementsEnchantment(ElementType.GROUND));
}
