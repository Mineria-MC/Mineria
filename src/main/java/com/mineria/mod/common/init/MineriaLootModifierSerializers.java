package com.mineria.mod.common.init;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.data.OakLeavesBillhookModifier;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MineriaLootModifierSerializers
{
    public static final DeferredRegister<GlobalLootModifierSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS, Mineria.MODID);

    public static final RegistryObject<OakLeavesBillhookModifier.Serializer> OAK_LEAVES_BILLHOOK = SERIALIZERS.register("oak_leaves_billhook_serializer", OakLeavesBillhookModifier.Serializer::new);
}
