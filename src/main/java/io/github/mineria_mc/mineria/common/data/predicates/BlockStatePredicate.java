package io.github.mineria_mc.mineria.common.data.predicates;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Objects;

public class BlockStatePredicate {
    public static final BlockStatePredicate ANY = new BlockStatePredicate(null, null, StatePropertiesPredicate.ANY);
    @Nullable
    private final TagKey<Block> tag;
    @Nullable
    private final Block block;
    private final StatePropertiesPredicate properties;

    public BlockStatePredicate(@Nullable TagKey<Block> tag, @Nullable Block block, StatePropertiesPredicate state) {
        this.tag = tag;
        this.block = block;
        this.properties = state;
    }

    public boolean matches(BlockState state) {
        if (this == ANY) {
            return true;
        } else {
            Block block = state.getBlock();
            if (this.tag != null && !state.is(tag))
                return false;
            else if (this.block != null && block != this.block)
                return false;
            else
                return this.properties.matches(state);
        }
    }

    public static BlockStatePredicate fromJson(@Nullable JsonElement jsonElement) {
        if (jsonElement != null && !jsonElement.isJsonNull()) {
            JsonObject json = GsonHelper.convertToJsonObject(jsonElement, "block");
            Block block = null;
            if (json.has("block"))
                block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(GsonHelper.getAsString(json, "block")));

            TagKey<Block> tag = null;
            if (json.has("tag")) {
                ResourceLocation tagId = new ResourceLocation(GsonHelper.getAsString(json, "tag"));
                tag = TagKey.create(Registries.BLOCK, tagId);
            }

            StatePropertiesPredicate state = StatePropertiesPredicate.fromJson(json.get("state"));
            return new BlockStatePredicate(tag, block, state);
        } else {
            return ANY;
        }
    }

    public JsonElement serializeToJson() {
        if (this == ANY) {
            return JsonNull.INSTANCE;
        } else {
            JsonObject json = new JsonObject();
            if (this.block != null) {
                json.addProperty("block", Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(this.block), "Missing key for block: " + block).toString());
            }

            if (this.tag != null) {
                json.addProperty("tag", tag.location().toString());
            }

            json.add("state", this.properties.serializeToJson());
            return json;
        }
    }
}
