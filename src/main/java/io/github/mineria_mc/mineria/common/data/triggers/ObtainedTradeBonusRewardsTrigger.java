package io.github.mineria_mc.mineria.common.data.triggers;

import com.google.gson.JsonObject;
import io.github.mineria_mc.mineria.Mineria;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import org.jetbrains.annotations.NotNull;

public class ObtainedTradeBonusRewardsTrigger extends SimpleCriterionTrigger<ObtainedTradeBonusRewardsTrigger.Instance> {
    private static final ResourceLocation ID = new ResourceLocation(Mineria.MODID, "obtained_trade_bonus_rewards");

    @Override
    public @NotNull ResourceLocation getId() {
        return ID;
    }

    @Override
    protected @NotNull Instance createInstance(@NotNull JsonObject json, @NotNull ContextAwarePredicate andPredicate, @NotNull DeserializationContext parser) {
        ContextAwarePredicate entity = EntityPredicate.fromJson(json, "entity", parser);
        ItemPredicate bonus = ItemPredicate.fromJson(json.get("bonus"));
        return new Instance(andPredicate, entity, bonus);
    }

    public void trigger(ServerPlayer player, Entity entity, ItemStack stack) {
        LootContext ctx = EntityPredicate.createContext(player, entity);
        this.trigger(player, instance -> instance.matches(ctx, stack));
    }

    public static class Instance extends AbstractCriterionTriggerInstance {
        private final ContextAwarePredicate entity;
        private final ItemPredicate bonus;

        public Instance(ContextAwarePredicate andPredicate, ContextAwarePredicate entity, ItemPredicate bonus) {
            super(ID, andPredicate);
            this.entity = entity;
            this.bonus = bonus;
        }

        public static Instance obtainedRewardsFrom(EntityPredicate predicate) {
            return new Instance(ContextAwarePredicate.ANY, EntityPredicate.wrap(predicate), ItemPredicate.ANY);
        }

        private boolean matches(LootContext ctx, ItemStack stack) {
            return this.entity.matches(ctx) && this.bonus.matches(stack);
        }

        @Override
        public @NotNull JsonObject serializeToJson(@NotNull SerializationContext serializer) {
            JsonObject json = super.serializeToJson(serializer);
            json.add("entity", this.entity.toJson(serializer));
            json.add("bonus", this.bonus.serializeToJson());
            return json;
        }
    }
}
