package com.mineria.mod.common.data.triggers;

import com.google.gson.JsonObject;
import com.mineria.mod.Mineria;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.resources.ResourceLocation;

public class ObtainedTradeBonusRewardsTrigger extends SimpleCriterionTrigger<ObtainedTradeBonusRewardsTrigger.Instance>
{
    private static final ResourceLocation ID = new ResourceLocation(Mineria.MODID, "obtained_trade_bonus_rewards");

    @Override
    public ResourceLocation getId()
    {
        return ID;
    }

    @Override
    protected Instance createInstance(JsonObject json, EntityPredicate.Composite andPredicate, DeserializationContext parser)
    {
        EntityPredicate.Composite entity = EntityPredicate.Composite.fromJson(json, "entity", parser);
        ItemPredicate bonus = ItemPredicate.fromJson(json.get("bonus"));
        return new Instance(andPredicate, entity, bonus);
    }

    public void trigger(ServerPlayer player, Entity entity, ItemStack stack)
    {
        LootContext ctx = EntityPredicate.createContext(player, entity);
        this.trigger(player, instance -> instance.matches(ctx, stack));
    }

    public static class Instance extends AbstractCriterionTriggerInstance
    {
        private final EntityPredicate.Composite entity;
        private final ItemPredicate bonus;

        public Instance(EntityPredicate.Composite andPredicate, EntityPredicate.Composite entity, ItemPredicate bonus)
        {
            super(ID, andPredicate);
            this.entity = entity;
            this.bonus = bonus;
        }

        private boolean matches(LootContext ctx, ItemStack stack)
        {
            return this.entity.matches(ctx) && this.bonus.matches(stack);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext serializer)
        {
            JsonObject json = super.serializeToJson(serializer);
            json.add("entity", this.entity.toJson(serializer));
            json.add("bonus", this.bonus.serializeToJson());
            return json;
        }
    }
}
