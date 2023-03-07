package io.github.mineria_mc.mineria.common.data.triggers;

import com.google.gson.JsonObject;
import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.data.predicates.ShapedRecipePredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.resources.ResourceLocation;

public class ShapedRecipeUsedTrigger extends SimpleCriterionTrigger<ShapedRecipeUsedTrigger.Instance> {
    private static final ResourceLocation ID = new ResourceLocation(Mineria.MODID, "shaped_recipe_used");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    protected Instance createInstance(JsonObject json, EntityPredicate.Composite andPredicate, DeserializationContext parser) {
        ShapedRecipePredicate recipe = ShapedRecipePredicate.fromJson(json.get("recipe"));
        return new Instance(andPredicate, recipe);
    }

    public void trigger(ServerPlayer player, CraftingContainer inv) {
        this.trigger(player, instance -> instance.matches(inv));
    }

    public static class Instance extends AbstractCriterionTriggerInstance {
        private final ShapedRecipePredicate recipe;

        public Instance(EntityPredicate.Composite andPredicate, ShapedRecipePredicate recipe) {
            super(ID, andPredicate);
            this.recipe = recipe;
        }

        private boolean matches(CraftingContainer inv) {
            return this.recipe.matches(inv);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext serializer) {
            JsonObject json = super.serializeToJson(serializer);
            json.add("recipe", this.recipe.serializeToJson());
            return json;
        }
    }
}
