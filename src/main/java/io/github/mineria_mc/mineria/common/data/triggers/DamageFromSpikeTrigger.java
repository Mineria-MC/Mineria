package io.github.mineria_mc.mineria.common.data.triggers;

import com.google.gson.JsonObject;
import io.github.mineria_mc.mineria.Mineria;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.world.level.block.Block;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.util.GsonHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class DamageFromSpikeTrigger extends SimpleCriterionTrigger<DamageFromSpikeTrigger.Instance> {
    private static final ResourceLocation ID = new ResourceLocation(Mineria.MODID, "damage_from_spike");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    protected Instance createInstance(JsonObject json, EntityPredicate.Composite predicate, DeserializationContext parser) {
        Block block = json.has("block") ? ForgeRegistries.BLOCKS.getValue(new ResourceLocation(GsonHelper.getAsString(json, "block"))) : null;
        float damage = json.has("damage") ? GsonHelper.getAsFloat(json, "damage") : -1;
        return new Instance(predicate, block, damage);
    }

    public void trigger(ServerPlayer player, Block block, float damage) {
        this.trigger(player, instance -> instance.matches(block, damage));
    }

    public static class Instance extends AbstractCriterionTriggerInstance {
        @Nullable
        private final Block block;
        private float damage = -1;

        public Instance(EntityPredicate.Composite predicate, @Nullable Block block, float damage) {
            super(ID, predicate);
            this.block = block;
            this.damage = damage;
        }

        private boolean matches(Block block, float damage) {
            return (this.block == null || block.equals(this.block)) && (this.damage == -1 || this.damage == damage);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext serializer) {
            JsonObject json = super.serializeToJson(serializer);
            if (block != null) json.addProperty("block", ForgeRegistries.BLOCKS.getKey(block).toString());
            if (damage != -1) json.addProperty("damage", damage);
            return json;
        }
    }
}
