package com.mineria.mod.common.data.triggers;

import com.google.gson.JsonObject;
import com.mineria.mod.Mineria;
import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.block.Block;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.loot.ConditionArraySerializer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class DamageFromSpikeTrigger extends AbstractCriterionTrigger<DamageFromSpikeTrigger.Instance>
{
    private static final ResourceLocation ID = new ResourceLocation(Mineria.MODID, "damage_from_spike");

    @Override
    public ResourceLocation getId()
    {
        return ID;
    }

    @Override
    protected Instance createInstance(JsonObject json, EntityPredicate.AndPredicate predicate, ConditionArrayParser parser)
    {
        Block block = json.has("block") ? ForgeRegistries.BLOCKS.getValue(new ResourceLocation(JSONUtils.getAsString(json, "block"))) : null;
        float damage = json.has("damage") ? JSONUtils.getAsFloat(json, "damage") : -1;
        return new Instance(predicate, block, damage);
    }

    public void trigger(ServerPlayerEntity player, Block block, float damage)
    {
        this.trigger(player, instance -> instance.matches(block, damage));
    }

    public static class Instance extends CriterionInstance
    {
        @Nullable
        private final Block block;
        private float damage = -1;

        public Instance(EntityPredicate.AndPredicate predicate, @Nullable Block block, float damage)
        {
            super(ID, predicate);
            this.block = block;
            this.damage = damage;
        }

        private boolean matches(Block block, float damage)
        {
            return (this.block == null || block.equals(this.block)) && (this.damage == -1 || this.damage == damage);
        }

        @Override
        public JsonObject serializeToJson(ConditionArraySerializer serializer)
        {
            JsonObject json = super.serializeToJson(serializer);
            if(block != null) json.addProperty("block", ForgeRegistries.BLOCKS.getKey(block).toString());
            if(damage != -1) json.addProperty("damage", damage);
            return json;
        }
    }
}
