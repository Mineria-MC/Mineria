package com.mineria.mod.common.data;

import com.google.gson.JsonObject;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.util.GsonHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

public class OakLeavesBillhookModifier extends LootModifier
{
    private final float chance;
    private final Item result;

    public OakLeavesBillhookModifier(LootItemCondition[] conditionsIn, float chance, Item result)
    {
        super(conditionsIn);
        this.chance = chance;
        this.result = result;
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context)
    {
        Random rand = context.getRandom();

        if(rand.nextFloat() < chance)
        {
            generatedLoot.add(new ItemStack(result));
        }

        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<OakLeavesBillhookModifier>
    {
        @Override
        public OakLeavesBillhookModifier read(ResourceLocation location, JsonObject object, LootItemCondition[] ailootcondition)
        {
            float chance = GsonHelper.getAsFloat(object, "chance");
            Item result = ForgeRegistries.ITEMS.getValue(new ResourceLocation(GsonHelper.getAsString(object, "drops")));
            return new OakLeavesBillhookModifier(ailootcondition, chance, result);
        }

        @Override
        public JsonObject write(OakLeavesBillhookModifier instance)
        {
            JsonObject json = makeConditions(instance.conditions);
            json.addProperty("chance", instance.chance);
            json.addProperty("drops", ForgeRegistries.ITEMS.getKey(instance.result).toString());
            return json;
        }
    }
}
