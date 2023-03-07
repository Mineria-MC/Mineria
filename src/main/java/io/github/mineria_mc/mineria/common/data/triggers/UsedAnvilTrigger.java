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

public class UsedAnvilTrigger extends SimpleCriterionTrigger<UsedAnvilTrigger.Instance> {
    private static final ResourceLocation ID = new ResourceLocation(Mineria.MODID, "used_anvil");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    protected Instance createInstance(JsonObject json, EntityPredicate.Composite andPredicate, DeserializationContext parser) {
        ItemPredicate left = ItemPredicate.fromJson(json.get("left"));
        ItemPredicate right = ItemPredicate.fromJson(json.get("right"));
        ItemPredicate output = ItemPredicate.fromJson(json.get("output"));
        return new Instance(andPredicate, left, right, output);
    }

    public void trigger(ServerPlayer player, ItemStack left, ItemStack right, ItemStack output) {
        this.trigger(player, instance -> instance.matches(left, right, output));
    }

    public static class Instance extends AbstractCriterionTriggerInstance {
        private final ItemPredicate left;
        private final ItemPredicate right;
        private final ItemPredicate output;

        public Instance(EntityPredicate.Composite andPredicate, ItemPredicate left, ItemPredicate right, ItemPredicate output) {
            super(ID, andPredicate);
            this.left = left;
            this.right = right;
            this.output = output;
        }

        private boolean matches(ItemStack left, ItemStack right, ItemStack output) {
            return this.left.matches(left) && this.right.matches(right) && this.output.matches(output);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext serializer) {
            JsonObject json = super.serializeToJson(serializer);
            json.add("left", this.left.serializeToJson());
            json.add("right", this.right.serializeToJson());
            json.add("output", this.output.serializeToJson());
            return json;
        }
    }
}
