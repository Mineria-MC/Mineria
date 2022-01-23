package com.mineria.mod.common.data.triggers;

import com.google.gson.JsonObject;
import com.mineria.mod.Mineria;
import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.loot.ConditionArraySerializer;
import net.minecraft.util.ResourceLocation;

public class UsedAnvilTrigger extends AbstractCriterionTrigger<UsedAnvilTrigger.Instance>
{
    private static final ResourceLocation ID = new ResourceLocation(Mineria.MODID, "used_anvil");

    @Override
    public ResourceLocation getId()
    {
        return ID;
    }

    @Override
    protected Instance createInstance(JsonObject json, EntityPredicate.AndPredicate andPredicate, ConditionArrayParser parser)
    {
        ItemPredicate left = ItemPredicate.fromJson(json.get("left"));
        ItemPredicate right = ItemPredicate.fromJson(json.get("right"));
        ItemPredicate output = ItemPredicate.fromJson(json.get("output"));
        return new Instance(andPredicate, left, right, output);
    }

    public void trigger(ServerPlayerEntity player, ItemStack left, ItemStack right, ItemStack output)
    {
        this.trigger(player, instance -> instance.matches(left, right, output));
    }

    public static class Instance extends CriterionInstance
    {
        private final ItemPredicate left;
        private final ItemPredicate right;
        private final ItemPredicate output;

        public Instance(EntityPredicate.AndPredicate andPredicate, ItemPredicate left, ItemPredicate right, ItemPredicate output)
        {
            super(ID, andPredicate);
            this.left = left;
            this.right = right;
            this.output = output;
        }

        private boolean matches(ItemStack left, ItemStack right, ItemStack output)
        {
            return this.left.matches(left) && this.right.matches(right) && this.output.matches(output);
        }

        @Override
        public JsonObject serializeToJson(ConditionArraySerializer serializer)
        {
            JsonObject json = super.serializeToJson(serializer);
            json.add("left", this.left.serializeToJson());
            json.add("right", this.right.serializeToJson());
            json.add("output", this.output.serializeToJson());
            return json;
        }
    }
}
