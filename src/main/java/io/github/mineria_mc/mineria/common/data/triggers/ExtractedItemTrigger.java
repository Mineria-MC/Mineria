package io.github.mineria_mc.mineria.common.data.triggers;

import com.google.gson.JsonObject;
import io.github.mineria_mc.mineria.Mineria;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.resources.ResourceLocation;

public class ExtractedItemTrigger extends SimpleCriterionTrigger<ExtractedItemTrigger.Instance> {
    private static final ResourceLocation ID = new ResourceLocation(Mineria.MODID, "extracted_item");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    protected Instance createInstance(JsonObject json, EntityPredicate.Composite andPredicate, DeserializationContext parser) {
        ItemPredicate item = ItemPredicate.fromJson(json.get("item"));
        return new Instance(andPredicate, item);
    }

    public void trigger(ServerPlayer player, ItemStack stack) {
        super.trigger(player, instance -> instance.matches(stack));
    }

    public static class Instance extends AbstractCriterionTriggerInstance {
        private final ItemPredicate item;

        public Instance(EntityPredicate.Composite predicate, ItemPredicate item) {
            super(ID, predicate);
            this.item = item;
        }

        private boolean matches(ItemStack stack) {
            return this.item.matches(stack);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext serializer) {
            JsonObject json = super.serializeToJson(serializer);
            json.add("item", this.item.serializeToJson());
            return json;
        }
    }
}
