package com.mineria.mod.common.data.triggers;

import com.google.gson.JsonObject;
import com.mineria.mod.Mineria;
import com.mineria.mod.common.data.predicates.FluidBarrelCapacityPredicate;
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

public class FluidBarrelFilledTrigger extends SimpleCriterionTrigger<FluidBarrelFilledTrigger.Instance>
{
    private static final ResourceLocation ID = new ResourceLocation(Mineria.MODID, "fluid_barrel_filled");

    @Override
    public ResourceLocation getId()
    {
        return ID;
    }

    @Override
    protected Instance createInstance(JsonObject json, EntityPredicate.Composite andPredicate, DeserializationContext parser)
    {
        Block block = json.has("block") ? ForgeRegistries.BLOCKS.getValue(new ResourceLocation(GsonHelper.getAsString(json, "block"))) : null;
        FluidBarrelCapacityPredicate capacityPredicate = FluidBarrelCapacityPredicate.fromJson(json.get("capacityPredicate"));
        return new Instance(andPredicate, block, capacityPredicate);
    }

    public void trigger(ServerPlayer player, Block block, int capacity, int buckets)
    {
        this.trigger(player, instance -> instance.matches(block, capacity, buckets));
    }

    public static class Instance extends AbstractCriterionTriggerInstance
    {
        @Nullable
        private final Block block;
        private final FluidBarrelCapacityPredicate capacityPredicate;

        public Instance(EntityPredicate.Composite andPredicate, @Nullable Block block, FluidBarrelCapacityPredicate capacityPredicate)
        {
            super(ID, andPredicate);
            this.block = block;
            this.capacityPredicate = capacityPredicate;
        }

        private boolean matches(Block block, int capacity, int buckets)
        {
            if(this.block != null && this.block != block)
                return false;
            else
                return capacityPredicate.matches(capacity, buckets);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext serializer)
        {
            JsonObject json = super.serializeToJson(serializer);
            if(block != null)
                json.addProperty("block", ForgeRegistries.BLOCKS.getKey(block).toString());
            json.add("capacityPredicate", this.capacityPredicate.serializeToJson());
            return json;
        }
    }
}
