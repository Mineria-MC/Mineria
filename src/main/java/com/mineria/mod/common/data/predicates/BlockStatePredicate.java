package com.mineria.mod.common.data.predicates;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tags.ITag;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class BlockStatePredicate
{
    public static final BlockStatePredicate ANY = new BlockStatePredicate(null, null, StatePropertiesPredicate.ANY);
    @Nullable
    private final ITag<Block> tag;
    @Nullable
    private final Block block;
    private final StatePropertiesPredicate properties;

    public BlockStatePredicate(@Nullable ITag<Block> tag, @Nullable Block block, StatePropertiesPredicate state)
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
            JsonObject json = JSONUtils.convertToJsonObject(jsonElement, "block");
            Block block = null;
            if (json.has("block"))
                block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(JSONUtils.getAsString(json, "block")));

            ITag<Block> tag = null;
            if (json.has("tag"))
            {
                ResourceLocation tagId = new ResourceLocation(JSONUtils.getAsString(json, "tag"));
                tag = TagCollectionManager.getInstance().getBlocks().getTag(tagId);
                if (tag == null)
                    throw new JsonSyntaxException("Unknown block tag '" + tagId + "'");
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
                json.addProperty("tag", TagCollectionManager.getInstance().getBlocks().getIdOrThrow(this.tag).toString());

            json.add("state", this.properties.serializeToJson());
            return json;
        }
    }
}
