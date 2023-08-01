package io.github.mineria_mc.mineria.common.init;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.data.FishFishingModifier;
import io.github.mineria_mc.mineria.common.data.OakLeavesBillhookModifier;
import com.mojang.serialization.Codec;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MineriaLootModifierSerializers {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Mineria.MODID);

    public static final RegistryObject<Codec<OakLeavesBillhookModifier>> OAK_LEAVES_BILLHOOK = SERIALIZERS.register("oak_leaves_billhook", () -> OakLeavesBillhookModifier.CODEC);
    public static final RegistryObject<Codec<FishFishingModifier>> FISH_FISHING = SERIALIZERS.register("fish_fishing", () -> FishFishingModifier.CODEC);
}
