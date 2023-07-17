package io.github.mineria_mc.mineria.common.data.triggers;

import com.google.gson.JsonObject;
import io.github.mineria_mc.mineria.Mineria;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class DamageFromSpikeTrigger extends SimpleCriterionTrigger<DamageFromSpikeTrigger.Instance> {
    private static final ResourceLocation ID = new ResourceLocation(Mineria.MODID, "damage_from_spike");

    @Override
    public @NotNull ResourceLocation getId() {
        return ID;
    }

    @Override
    protected @NotNull Instance createInstance(JsonObject json, @NotNull ContextAwarePredicate predicate, @NotNull DeserializationContext parser) {
        Block block = json.has("block") ? ForgeRegistries.BLOCKS.getValue(new ResourceLocation(GsonHelper.getAsString(json, "block"))) : null;
        float damage = json.has("damage") ? GsonHelper.getAsFloat(json, "damage") : -1;
        return new Instance(predicate, block, damage);
    }

    public void trigger(ServerPlayer player, Block block, float damage) {
        this.trigger(player, instance -> instance.matches(block, damage));
    }

    public static class Instance extends AbstractCriterionTriggerInstance {
        public static final Instance ANY = new Instance(ContextAwarePredicate.ANY, null, -1);

        @Nullable
        private final Block block;
        private final float damage;

        public Instance(ContextAwarePredicate predicate, @Nullable Block block, float damage) {
            super(ID, predicate);
            this.block = block;
            this.damage = damage;
        }

        private boolean matches(Block block, float damage) {
            return (this.block == null || block.equals(this.block)) && (this.damage == -1 || this.damage == damage);
        }

        @Override
        public @NotNull JsonObject serializeToJson(@NotNull SerializationContext serializer) {
            JsonObject json = super.serializeToJson(serializer);
            if (block != null) json.addProperty("block", ForgeRegistries.BLOCKS.getKey(block).toString());
            if (damage != -1) json.addProperty("damage", damage);
            return json;
        }
    }
}
