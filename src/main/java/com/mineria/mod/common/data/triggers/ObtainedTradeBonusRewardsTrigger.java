package com.mineria.mod.common.data.triggers;

import com.google.gson.JsonObject;
import com.mineria.mod.Mineria;
import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.loot.ConditionArraySerializer;
import net.minecraft.loot.LootContext;
import net.minecraft.util.ResourceLocation;

public class ObtainedTradeBonusRewardsTrigger extends AbstractCriterionTrigger<ObtainedTradeBonusRewardsTrigger.Instance>
{
    private static final ResourceLocation ID = new ResourceLocation(Mineria.MODID, "obtained_trade_bonus_rewards");

    @Override
    public ResourceLocation getId()
    {
        return ID;
    }

    @Override
    protected Instance createInstance(JsonObject json, EntityPredicate.AndPredicate andPredicate, ConditionArrayParser parser)
    {
        EntityPredicate.AndPredicate entity = EntityPredicate.AndPredicate.fromJson(json, "entity", parser);
        ItemPredicate bonus = ItemPredicate.fromJson(json.get("bonus"));
        return new Instance(andPredicate, entity, bonus);
    }

    public void trigger(ServerPlayerEntity player, Entity entity, ItemStack stack)
    {
        LootContext ctx = EntityPredicate.createContext(player, entity);
        this.trigger(player, instance -> instance.matches(ctx, stack));
    }

    public static class Instance extends CriterionInstance
    {
        private final EntityPredicate.AndPredicate entity;
        private final ItemPredicate bonus;

        public Instance(EntityPredicate.AndPredicate andPredicate, EntityPredicate.AndPredicate entity, ItemPredicate bonus)
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
        public JsonObject serializeToJson(ConditionArraySerializer serializer)
        {
            JsonObject json = super.serializeToJson(serializer);
            json.add("entity", this.entity.toJson(serializer));
            json.add("bonus", this.bonus.serializeToJson());
            return json;
        }
    }
}
