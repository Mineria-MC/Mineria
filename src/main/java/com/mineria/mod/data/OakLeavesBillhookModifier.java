package com.mineria.mod.data;

import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
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

    public OakLeavesBillhookModifier(ILootCondition[] conditionsIn, float chance, Item result)
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
        public OakLeavesBillhookModifier read(ResourceLocation location, JsonObject object, ILootCondition[] ailootcondition)
        {
            float chance = JSONUtils.getFloat(object, "chance");
            Item result = ForgeRegistries.ITEMS.getValue(new ResourceLocation(JSONUtils.getString(object, "drops")));
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
