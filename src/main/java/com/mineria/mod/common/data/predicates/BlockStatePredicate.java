package com.mineria.mod.common.data.predicates;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.tags.Tag;
import net.minecraft.tags.SerializationTags;
import net.minecraft.util.GsonHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class BlockStatePredicate
{
    public static final BlockStatePredicate ANY = new BlockStatePredicate(null, null, StatePropertiesPredicate.ANY);
    @Nullable
    private final Tag<Block> tag;
    @Nullable
    private final Block block;
    private final StatePropertiesPredicate properties;

    public BlockStatePredicate(@Nullable Tag<Block> tag, @Nullable Block block, StatePropertiesPredicate state)
    {
        this.tag = tag;
        this.block = block;
        this.properties = state;
    }

    public boolean matches(BlockState state)
    {
        if (this == ANY)
        {
            return true;
        } else
        {
            Block block = state.getBlock();
            if (this.tag != null && !this.tag.contains(block))
                return false;
            else if (this.block != null && block != this.block)
                return false;
            else
                return this.properties.matches(state);
        }
    }

    public static BlockStatePredicate fromJson(@Nullable JsonElement jsonElement)
    {
        if (jsonElement != null && !jsonElement.isJsonNull())
        {
            JsonObject json = GsonHelper.convertToJsonObject(jsonElement, "block");
            Block block = null;
            if (json.has("block"))
                block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(GsonHelper.getAsString(json, "block")));

            Tag<Block> tag = null;
            if (json.has("tag"))
            {
                ResourceLocation tagId = new ResourceLocation(GsonHelper.getAsString(json, "tag"));
                tag = SerializationTags.getInstance().getTagOrThrow(Registry.BLOCK_REGISTRY, tagId, (id) -> new JsonSyntaxException("Unknown block tag '" + id + "'"));
            }

            StatePropertiesPredicate state = StatePropertiesPredicate.fromJson(json.get("state"));
            return new BlockStatePredicate(tag, block, state);
        } else
        {
            return ANY;
        }
    }

    public JsonElement serializeToJson()
    {
        if (this == ANY)
        {
            return JsonNull.INSTANCE;
        } else
        {
            JsonObject json = new JsonObject();
            if (this.block != null)
                json.addProperty("block", ForgeRegistries.BLOCKS.getKey(this.block).toString());

            if (this.tag != null)
                json.addProperty("tag", SerializationTags.getInstance().getIdOrThrow(Registry.BLOCK_REGISTRY, this.tag, () -> new IllegalStateException("Unknown block tag")).toString());

            json.add("state", this.properties.serializeToJson());
            return json;
        }
    }
}