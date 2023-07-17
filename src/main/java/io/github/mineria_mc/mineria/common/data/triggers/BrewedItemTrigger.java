package io.github.mineria_mc.mineria.common.data.triggers;

import com.google.gson.JsonObject;
import io.github.mineria_mc.mineria.Mineria;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Made this thinking I would use it, but I actually don't -_-
 */
public class BrewedItemTrigger extends SimpleCriterionTrigger<BrewedItemTrigger.Instance> {
    private static final ResourceLocation ID = new ResourceLocation(Mineria.MODID, "brewed_item");

    @Override
    public @NotNull ResourceLocation getId() {
        return ID;
    }

    @Override
    protected @NotNull Instance createInstance(JsonObject json, @NotNull ContextAwarePredicate andPredicate, @NotNull DeserializationContext parser) {
        ItemPredicate item = ItemPredicate.fromJson(json.get("item"));
        return new Instance(andPredicate, item);
    }

    public void trigger(ServerPlayer player, ItemStack stack) {
        this.trigger(player, instance -> instance.matches(stack));
    }

    public static class Instance extends AbstractCriterionTriggerInstance {
        private final ItemPredicate item;

        public Instance(ContextAwarePredicate andPredicate, ItemPredicate item) {
            super(ID, andPredicate);
            this.item = item;
        }

        private boolean matches(ItemStack stack) {
            return this.item.matches(stack);
        }

        @Override
        public @NotNull JsonObject serializeToJson(@NotNull SerializationContext serializer) {
            JsonObject json = super.serializeToJson(serializer);
            json.add("item", this.item.serializeToJson());
            return json;
        }
    }
}
