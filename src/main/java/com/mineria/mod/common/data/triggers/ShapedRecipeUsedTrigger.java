package com.mineria.mod.common.data.triggers;

import com.google.gson.JsonObject;
import com.mineria.mod.Mineria;
import com.mineria.mod.common.data.predicates.ShapedRecipePredicate;
import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.loot.ConditionArraySerializer;
import net.minecraft.util.ResourceLocation;

public class ShapedRecipeUsedTrigger extends AbstractCriterionTrigger<ShapedRecipeUsedTrigger.Instance>
{
    private static final ResourceLocation ID = new ResourceLocation(Mineria.MODID, "shaped_recipe_used");

    @Override
    public ResourceLocation getId()
    {
        return ID;
    }

    @Override
    protected Instance createInstance(JsonObject json, EntityPredicate.AndPredicate andPredicate, ConditionArrayParser parser)
    {
        ShapedRecipePredicate recipe = ShapedRecipePredicate.fromJson(json.get("recipe"));
        return new Instance(andPredicate, recipe);
    }

    public void trigger(ServerPlayerEntity player, CraftingInventory inv)
    {
        this.trigger(player, instance -> instance.matches(inv));
    }

    public static class Instance extends CriterionInstance
    {
        private final ShapedRecipePredicate recipe;

        public Instance(EntityPredicate.AndPredicate andPredicate, ShapedRecipePredicate recipe)
        {
            super(ID, andPredicate);
            this.recipe = recipe;
        }

        private boolean matches(CraftingInventory inv)
        {
            return this.recipe.matches(inv);
        }

        @Override
        public JsonObject serializeToJson(ConditionArraySerializer serializer)
        {
            JsonObject json = super.serializeToJson(serializer);
            json.add("recipe", this.recipe.serializeToJson());
            return json;
        }
    }
}
