package io.github.mineria_mc.mineria.common.init;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.data.loot_functions.AppendEnchantmentsFunction;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class MineriaLootItemFunctionTypes {
    public static final DeferredRegister<LootItemFunctionType> FUNCTION_TYPES = DeferredRegister.create(Registries.LOOT_FUNCTION_TYPE, Mineria.MODID);

    public static final RegistryObject<LootItemFunctionType> APPEND_ENCHANTMENTS = FUNCTION_TYPES.register("append_enchantments", () -> new LootItemFunctionType(new AppendEnchantmentsFunction.Serializer()));
}
