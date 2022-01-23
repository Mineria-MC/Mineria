package com.mineria.mod.common.data.triggers;

import com.google.gson.JsonObject;
import com.mineria.mod.Mineria;
import com.mineria.mod.common.data.predicates.BlockStatePredicate;
import net.minecraft.advancements.criterion.*;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.loot.ConditionArraySerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public class MinedBlockTrigger extends AbstractCriterionTrigger<MinedBlockTrigger.Instance>
{
    private static final ResourceLocation ID = new ResourceLocation(Mineria.MODID, "mined_block");

    @Override
    public ResourceLocation getId()
    {
        return ID;
    }

    @Override
    protected Instance createInstance(JsonObject json, EntityPredicate.AndPredicate andPredicate, ConditionArrayParser parser)
    {
        BlockStatePredicate block = BlockStatePredicate.fromJson(json.get("block"));
        LocationPredicate location = LocationPredicate.fromJson(json.get("location"));
        ItemPredicate item = ItemPredicate.fromJson(json.get("item"));
        return new Instance(andPredicate, block, location, item);
    }

    public void trigger(ServerPlayerEntity player, BlockState state, BlockPos pos, ItemStack stack)
    {
        this.trigger(player, instance -> instance.matches(state, player.getLevel(), pos, stack));
    }

    public static class Instance extends CriterionInstance
    {
        private final BlockStatePredicate block;
        private final LocationPredicate location;
        private final ItemPredicate item;

        public Instance(EntityPredicate.AndPredicate andPredicate, BlockStatePredicate block, LocationPredicate location, ItemPredicate item)
        {
            super(ID, andPredicate);
            this.block = block;
            this.location = location;
            this.item = item;
        }

        private boolean matches(BlockState state, ServerWorld world, BlockPos pos, ItemStack stack)
        {
            if(!this.block.matches(state))
                return false;
            else if(!this.location.matches(world, pos.getX(), pos.getY(), pos.getZ()))
                return false;
            else
                return this.item.matches(stack);
        }

        @Override
        public JsonObject serializeToJson(ConditionArraySerializer serializer)
        {
            JsonObject json = super.serializeToJson(serializer);
            json.add("block", this.block.serializeToJson());
            json.add("location", this.location.serializeToJson());
            json.add("item", this.item.serializeToJson());
            return json;
        }
    }
}
