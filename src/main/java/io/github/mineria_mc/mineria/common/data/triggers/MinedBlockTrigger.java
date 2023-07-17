package io.github.mineria_mc.mineria.common.data.triggers;

import com.google.gson.JsonObject;
import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.data.predicates.BlockStatePredicate;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class MinedBlockTrigger extends SimpleCriterionTrigger<MinedBlockTrigger.Instance> {
    private static final ResourceLocation ID = new ResourceLocation(Mineria.MODID, "mined_block");

    @Override
    public @NotNull ResourceLocation getId() {
        return ID;
    }

    @Override
    protected @NotNull Instance createInstance(JsonObject json, @NotNull ContextAwarePredicate andPredicate, @NotNull DeserializationContext parser) {
        BlockStatePredicate block = BlockStatePredicate.fromJson(json.get("block"));
        LocationPredicate location = LocationPredicate.fromJson(json.get("location"));
        ItemPredicate item = ItemPredicate.fromJson(json.get("item"));
        return new Instance(andPredicate, block, location, item);
    }

    public void trigger(ServerPlayer player, BlockState state, BlockPos pos, ItemStack stack) {
        this.trigger(player, instance -> instance.matches(state, player.serverLevel(), pos, stack));
    }

    public static class Instance extends AbstractCriterionTriggerInstance {
        private final BlockStatePredicate block;
        private final LocationPredicate location;
        private final ItemPredicate item;

        public Instance(ContextAwarePredicate andPredicate, BlockStatePredicate block, LocationPredicate location, ItemPredicate item) {
            super(ID, andPredicate);
            this.block = block;
            this.location = location;
            this.item = item;
        }

        public static Instance minedBlock(Block block) {
            return new Instance(ContextAwarePredicate.ANY, new BlockStatePredicate(null, block, StatePropertiesPredicate.ANY), LocationPredicate.ANY, ItemPredicate.ANY);
        }

        public static Instance minedBlock(TagKey<Block> tag) {
            return new Instance(ContextAwarePredicate.ANY, new BlockStatePredicate(tag, null, StatePropertiesPredicate.ANY), LocationPredicate.ANY, ItemPredicate.ANY);
        }

        private boolean matches(BlockState state, ServerLevel world, BlockPos pos, ItemStack stack) {
            if (!this.block.matches(state))
                return false;
            else if (!this.location.matches(world, pos.getX(), pos.getY(), pos.getZ()))
                return false;
            else
                return this.item.matches(stack);
        }

        @Override
        public @NotNull JsonObject serializeToJson(@NotNull SerializationContext serializer) {
            JsonObject json = super.serializeToJson(serializer);
            json.add("block", this.block.serializeToJson());
            json.add("location", this.location.serializeToJson());
            json.add("item", this.item.serializeToJson());
            return json;
        }
    }
}
