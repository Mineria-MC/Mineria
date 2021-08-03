package com.mineria.mod.init;

import com.mineria.mod.References;
import com.mineria.mod.data.OakLeavesBillhookModifier;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class LootModifierSerializersInit
{
    public static final DeferredRegister<GlobalLootModifierSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS, References.MODID);

    public static final RegistryObject<OakLeavesBillhookModifier.Serializer> OAK_LEAVES_BILLHOOK = SERIALIZERS.register("oak_leaves_billhook_serializer", OakLeavesBillhookModifier.Serializer::new);
}
