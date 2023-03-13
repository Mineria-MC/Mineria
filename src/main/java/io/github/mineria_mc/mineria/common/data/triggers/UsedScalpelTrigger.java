package io.github.mineria_mc.mineria.common.data.triggers;

import com.google.gson.JsonObject;
import io.github.mineria_mc.mineria.Mineria;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.resources.ResourceLocation;

public class UsedScalpelTrigger extends SimpleCriterionTrigger<UsedScalpelTrigger.Instance> {
    private static final ResourceLocation ID = new ResourceLocation(Mineria.MODID, "used_scalpel");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    protected Instance createInstance(JsonObject json, EntityPredicate.Composite andPredicate, DeserializationContext parser) {
        EntityPredicate.Composite user = EntityPredicate.Composite.fromJson(json, "user", parser);
        EntityPredicate.Composite target = EntityPredicate.Composite.fromJson(json, "target", parser);
        return new Instance(andPredicate, user, target);
    }

    public void trigger(ServerPlayer player, LivingEntity user, LivingEntity target) {
        LootContext userCtx = EntityPredicate.createContext(player, user);
        LootContext targetCtx = EntityPredicate.createContext(player, target);
        this.trigger(player, instance -> instance.matches(userCtx, targetCtx));
    }

    public static class Instance extends AbstractCriterionTriggerInstance {
        public static final Instance ANY = new Instance(EntityPredicate.Composite.ANY, EntityPredicate.Composite.ANY, EntityPredicate.Composite.ANY);

        private final EntityPredicate.Composite user;
        private final EntityPredicate.Composite target;

        public Instance(EntityPredicate.Composite andPredicate, EntityPredicate.Composite user, EntityPredicate.Composite target) {
            super(ID, andPredicate);
            this.user = user;
            this.target = target;
        }

        private boolean matches(LootContext user, LootContext target) {
            return this.user.matches(user) && this.target.matches(target);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext serializer) {
            JsonObject json = super.serializeToJson(serializer);
            json.add("user", this.user.toJson(serializer));
            json.add("target", this.target.toJson(serializer));
            return json;
        }
    }
}
