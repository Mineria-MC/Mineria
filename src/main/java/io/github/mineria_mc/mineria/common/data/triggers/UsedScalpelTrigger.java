package io.github.mineria_mc.mineria.common.data.triggers;

import com.google.gson.JsonObject;
import io.github.mineria_mc.mineria.Mineria;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import org.jetbrains.annotations.NotNull;

public class UsedScalpelTrigger extends SimpleCriterionTrigger<UsedScalpelTrigger.Instance> {
    private static final ResourceLocation ID = new ResourceLocation(Mineria.MODID, "used_scalpel");

    @Override
    public @NotNull ResourceLocation getId() {
        return ID;
    }

    @Override
    protected @NotNull Instance createInstance(@NotNull JsonObject json, @NotNull ContextAwarePredicate andPredicate, @NotNull DeserializationContext parser) {
        ContextAwarePredicate user = EntityPredicate.fromJson(json, "user", parser);
        ContextAwarePredicate target = EntityPredicate.fromJson(json, "target", parser);
        return new Instance(andPredicate, user, target);
    }

    public void trigger(ServerPlayer player, LivingEntity user, LivingEntity target) {
        LootContext userCtx = EntityPredicate.createContext(player, user);
        LootContext targetCtx = EntityPredicate.createContext(player, target);
        this.trigger(player, instance -> instance.matches(userCtx, targetCtx));
    }

    public static class Instance extends AbstractCriterionTriggerInstance {
        public static final Instance ANY = new Instance(ContextAwarePredicate.ANY, ContextAwarePredicate.ANY, ContextAwarePredicate.ANY);

        private final ContextAwarePredicate user;
        private final ContextAwarePredicate target;

        public Instance(ContextAwarePredicate andPredicate, ContextAwarePredicate user, ContextAwarePredicate target) {
            super(ID, andPredicate);
            this.user = user;
            this.target = target;
        }

        private boolean matches(LootContext user, LootContext target) {
            return this.user.matches(user) && this.target.matches(target);
        }

        @Override
        public @NotNull JsonObject serializeToJson(@NotNull SerializationContext serializer) {
            JsonObject json = super.serializeToJson(serializer);
            json.add("user", this.user.toJson(serializer));
            json.add("target", this.target.toJson(serializer));
            return json;
        }
    }
}
