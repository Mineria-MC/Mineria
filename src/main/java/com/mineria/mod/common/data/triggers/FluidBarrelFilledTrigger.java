package com.mineria.mod.common.data.triggers;

import com.google.gson.JsonObject;
import com.mineria.mod.Mineria;
import com.mineria.mod.common.data.predicates.FluidBarrelCapacityPredicate;
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

public class FluidBarrelFilledTrigger extends AbstractCriterionTrigger<FluidBarrelFilledTrigger.Instance>
{
    private static final ResourceLocation ID = new ResourceLocation(Mineria.MODID, "fluid_barrel_filled");

    @Override
    public ResourceLocation getId()
    {
        return ID;
    }

    @Override
    protected Instance createInstance(JsonObject json, EntityPredicate.AndPredicate andPredicate, ConditionArrayParser parser)
    {
        Block block = json.has("block") ? ForgeRegistries.BLOCKS.getValue(new ResourceLocation(JSONUtils.getAsString(json, "block"))) : null;
        FluidBarrelCapacityPredicate capacityPredicate = FluidBarrelCapacityPredicate.fromJson(json.get("capacityPredicate"));
        return new Instance(andPredicate, block, capacityPredicate);
    }

    public void trigger(ServerPlayerEntity player, Block block, int capacity, int buckets)
    {
        this.trigger(player, instance -> instance.matches(block, capacity, buckets));
    }

    public static class Instance extends CriterionInstance
    {
        @Nullable
        private final Block block;
        private final FluidBarrelCapacityPredicate capacityPredicate;

        public Instance(EntityPredicate.AndPredicate andPredicate, @Nullable Block block, FluidBarrelCapacityPredicate capacityPredicate)
        {
            super(ID, andPredicate);
            this.block = block;
            this.capacityPredicate = capacityPredicate;
        }

        private boolean matches(Block block, int capacity, int buckets)
        {
            if(this.block != null && !block.is(block))
                return false;
            else
                return capacityPredicate.matches(capacity, buckets);
        }

        @Override
        public JsonObject serializeToJson(ConditionArraySerializer serializer)
        {
            JsonObject json = super.serializeToJson(serializer);
            if(block != null)
                json.addProperty("block", ForgeRegistries.BLOCKS.getKey(block).toString());
            json.add("capacityPredicate", this.capacityPredicate.serializeToJson());
            return json;
        }
    }
}
