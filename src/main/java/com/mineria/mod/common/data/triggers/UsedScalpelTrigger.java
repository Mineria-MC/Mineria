package com.mineria.mod.common.data.triggers;

import com.google.gson.JsonObject;
import com.mineria.mod.Mineria;
import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.loot.ConditionArraySerializer;
import net.minecraft.loot.LootContext;
import net.minecraft.util.ResourceLocation;

public class UsedScalpelTrigger extends AbstractCriterionTrigger<UsedScalpelTrigger.Instance>
{
    private static final ResourceLocation ID = new ResourceLocation(Mineria.MODID, "used_scalpel");

    @Override
    public ResourceLocation getId()
    {
        return ID;
    }

    @Override
    protected Instance createInstance(JsonObject json, EntityPredicate.AndPredicate andPredicate, ConditionArrayParser parser)
    {
        EntityPredicate.AndPredicate user = EntityPredicate.AndPredicate.fromJson(json, "user", parser);
        EntityPredicate.AndPredicate target = EntityPredicate.AndPredicate.fromJson(json, "target", parser);
        return new Instance(andPredicate, user, target);
    }

    public void trigger(ServerPlayerEntity player, LivingEntity user, LivingEntity target)
    {
        LootContext userCtx = EntityPredicate.createContext(player, user);
        LootContext targetCtx = EntityPredicate.createContext(player, target);
        this.trigger(player, instance -> instance.matches(userCtx, targetCtx));
    }

    public static class Instance extends CriterionInstance
    {
        private final EntityPredicate.AndPredicate user;
        private final EntityPredicate.AndPredicate target;

        public Instance(EntityPredicate.AndPredicate andPredicate, EntityPredicate.AndPredicate user, EntityPredicate.AndPredicate target)
        {
            super(ID, andPredicate);
            this.user = user;
            this.target = target;
        }

        private boolean matches(LootContext user, LootContext target)
        {
            return this.user.matches(user) && this.target.matches(target);
        }

        @Override
        public JsonObject serializeToJson(ConditionArraySerializer serializer)
        {
            JsonObject json = super.serializeToJson(serializer);
            json.add("user", this.user.toJson(serializer));
            json.add("target", this.target.toJson(serializer));
            return json;
        }
    }
}
