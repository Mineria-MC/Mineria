package io.github.mineria_mc.mineria.common.data.predicates;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.*;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.JsonOps;
import net.minecraft.Util;
import net.minecraft.core.NonNullList;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ShapedRecipePredicate {
    public static final ShapedRecipePredicate ANY = new ShapedRecipePredicate(new HashMap<>(), new String[0], ItemStack.EMPTY);
    private static final Logger LOGGER = LogUtils.getLogger();

    private final Map<String, Ingredient> keyToIngredientMap;
    private final String[] pattern;
    private final NonNullList<Ingredient> recipeItems;
    private final int width;
    private final int height;
    private final ItemStack result;

    public ShapedRecipePredicate(Map<String, Ingredient> keyToIngredientMap, String[] pattern, ItemStack result) {
        this.keyToIngredientMap = keyToIngredientMap;
        this.pattern = pattern;
        this.height = pattern.length;
        this.width = height == 0 ? 0 : pattern[0].length();
        this.recipeItems = dissolvePattern(pattern, keyToIngredientMap, width, height);
        this.result = result;
    }

    public boolean matches(CraftingContainer inv) {
        if (this == ANY) {
            return true;
        } else {
            for (int column = 0; column <= inv.getWidth() - this.width; ++column) {
                for (int width = 0; width <= inv.getHeight() - this.height; ++width) {
                    if (this.matches(inv, column, width, true))
                        return true;

                    if (this.matches(inv, column, width, false))
                        return true;
                }
            }

            return false;
        }
    }

    private boolean matches(CraftingContainer inv, int column, int width, boolean strict) {
        for (int oColumn = 0; oColumn < inv.getWidth(); ++oColumn) {
            for (int oWidth = 0; oWidth < inv.getHeight(); ++oWidth) {
                int hDifference = oColumn - column;
                int vDifference = oWidth - width;
                Ingredient ingredient = Ingredient.EMPTY;

                if (hDifference >= 0 && vDifference >= 0 && hDifference < this.width && vDifference < this.height) {
                    if (strict)
                        ingredient = this.recipeItems.get(this.width - hDifference - 1 + vDifference * this.width);
                    else
                        ingredient = this.recipeItems.get(hDifference + vDifference * this.width);
                }

                if (!ingredient.test(inv.getItem(oColumn + oWidth * inv.getWidth())))
                    return false;
            }
        }

        return true;
    }

    public static ShapedRecipePredicate fromJson(@Nullable JsonElement jsonElement) {
        if (jsonElement != null && !jsonElement.isJsonNull()) {
            JsonObject json = GsonHelper.convertToJsonObject(jsonElement, "recipe");

            Map<String, Ingredient> keyToIngredient = keyFromJson(GsonHelper.getAsJsonObject(json, "key"));
            String[] pattern = shrink(patternFromJson(GsonHelper.getAsJsonArray(json, "pattern")));
            ItemStack result = itemFromJson(GsonHelper.getAsJsonObject(json, "result"));
            return new ShapedRecipePredicate(keyToIngredient, pattern, result);
        } else
            return ANY;
    }

    public JsonElement serializeToJson() {
        if (this == ANY) {
            return JsonNull.INSTANCE;
        } else {
            JsonObject json = new JsonObject();
            JsonObject keyToIngredientObj = new JsonObject();
            this.keyToIngredientMap.forEach((key, ingredient) -> keyToIngredientObj.add(key, ingredient.toJson()));
            json.add("key", keyToIngredientObj);

            JsonArray pattern = new JsonArray();
            for (String keyRow : this.pattern)
                pattern.add(keyRow);
            json.add("pattern", pattern);

            json.add("result", ItemStack.CODEC.encodeStart(JsonOps.INSTANCE, this.result).result().map(JsonObject.class::cast).orElse(Util.make(new JsonObject(), jsonObject -> jsonObject.addProperty("id", "minecraft:air"))));
            return json;
        }
    }

    private static Map<String, Ingredient> keyFromJson(JsonObject json) {
        Map<String, Ingredient> keyToIngredient = Maps.newHashMap();

        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            if (entry.getKey().length() != 1)
                throw new JsonSyntaxException("Invalid key entry: '" + entry.getKey() + "' is an invalid symbol (must be 1 character only).");

            if (" ".equals(entry.getKey()))
                throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");

            keyToIngredient.put(entry.getKey(), Ingredient.fromJson(entry.getValue()));
        }

        keyToIngredient.put(" ", Ingredient.EMPTY);
        return keyToIngredient;
    }

    private static String[] patternFromJson(JsonArray pattern) {
        String[] result = new String[pattern.size()];
        final int maxHeight = getMaxHeight() == -1 ? 3 : getMaxHeight();
        final int maxWidth = getMaxWidth() == -1 ? 3 : getMaxWidth();

        if (result.length > maxHeight)
            throw new JsonSyntaxException("Invalid pattern: too many rows, " + maxHeight + " is maximum");
        else if (result.length == 0)
            throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
        else {
            for (int row = 0; row < result.length; ++row) {
                String keyRow = GsonHelper.convertToString(pattern.get(row), "pattern[" + row + "]");

                if (keyRow.length() > maxWidth)
                    throw new JsonSyntaxException("Invalid pattern: too many columns, " + maxWidth + " is maximum");

                if (row > 0 && result[0].length() != keyRow.length())
                    throw new JsonSyntaxException("Invalid pattern: each row must be the same width");

                result[row] = keyRow;
            }

            return result;
        }
    }

    @VisibleForTesting
    static String[] shrink(String... pattern) {
        int startIndex = Integer.MAX_VALUE;
        int length = 0;
        int currentSkippingRow = 0;
        int skippingRowCount = 0;

        for (int row = 0; row < pattern.length; ++row) {
            String keyRow = pattern[row];
            startIndex = Math.min(startIndex, firstNonSpace(keyRow));
            int endIndex = lastNonSpace(keyRow);
            length = Math.max(length, endIndex);
            if (endIndex < 0) {
                if (currentSkippingRow == row) {
                    ++currentSkippingRow;
                }

                ++skippingRowCount;
            } else {
                skippingRowCount = 0;
            }
        }

        if (pattern.length == skippingRowCount) {
            return new String[0];
        } else {
            String[] result = new String[pattern.length - skippingRowCount - currentSkippingRow];

            for (int row = 0; row < result.length; ++row) {
                result[row] = pattern[row + currentSkippingRow].substring(startIndex, length + 1);
            }

            return result;
        }
    }

    private static int firstNonSpace(String keyRow) {
        int i;
        for (i = 0; i < keyRow.length() && keyRow.charAt(i) == ' '; ++i) {
        }

        return i;
    }

    private static int lastNonSpace(String keyRow) {
        int i;
        for (i = keyRow.length() - 1; i >= 0 && keyRow.charAt(i) == ' '; --i) {
        }

        return i;
    }

    private static NonNullList<Ingredient> dissolvePattern(String[] pattern, Map<String, Ingredient> keyToIngredient, int width, int height) {
        NonNullList<Ingredient> result = NonNullList.withSize(width * height, Ingredient.EMPTY);
        Set<String> keys = Sets.newHashSet(keyToIngredient.keySet());
        keys.remove(" ");

        for (int row = 0; row < pattern.length; ++row) {
            for (int column = 0; column < pattern[row].length(); ++column) {
                String key = pattern[row].substring(column, column + 1);
                Ingredient ingredient = keyToIngredient.get(key);
                if (ingredient == null)
                    throw new JsonSyntaxException("Pattern references symbol '" + key + "' but it's not defined in the key");

                keys.remove(key);
                result.set(column + width * row, ingredient);
            }
        }

        if (!keys.isEmpty())
            throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + keys);
        else
            return result;
    }

    public static ItemStack itemFromJson(JsonObject json) {
        String itemId = GsonHelper.getAsString(json, "id");
        return ItemStack.CODEC.parse(JsonOps.INSTANCE, json).resultOrPartial(LOGGER::error).orElseThrow(() -> new JsonSyntaxException("Failed to parse ItemStack with id '" + itemId + "'"));
    }

    private static Field MAX_HEIGHT;

    private static int getMaxHeight() {
        if (MAX_HEIGHT == null) {
            MAX_HEIGHT = ObfuscationReflectionHelper.findField(ShapedRecipe.class, "MAX_HEIGHT");
        }
        try {
            return (int) MAX_HEIGHT.get(null);
        } catch (IllegalAccessException e) {
            return -1;
        }
    }

    private static Field MAX_WIDTH;

    private static int getMaxWidth() {
        if (MAX_WIDTH == null) {
            MAX_WIDTH = ObfuscationReflectionHelper.findField(ShapedRecipe.class, "MAX_WIDTH");
        }
        try {
            return (int) MAX_WIDTH.get(null);
        } catch (IllegalAccessException e) {
            return -1;
        }
    }

    public static Builder builder(String... pattern) {
        return new Builder(pattern);
    }

    public static class Builder {
        private final String[] pattern;
        private final Map<String, Ingredient> keyToIngredient = new HashMap<>();

        public Builder(String[] pattern) {
            String[] correctedPattern = Arrays.copyOf(pattern, 3);
            for (int i = pattern.length; i < 3; i++) {
                correctedPattern[i] = "";
            }
            this.pattern = correctedPattern;
        }

        public Builder key(String key, Ingredient ingredient) {
            keyToIngredient.put(key, ingredient);
            return this;
        }

        public ShapedRecipePredicate result(RegistryObject<? extends ItemLike> obj) {
            return obj.map(ItemStack::new).map(this::result).orElse(ANY);
        }

        public ShapedRecipePredicate result(ItemLike item) {
            return result(new ItemStack(item));
        }

        public ShapedRecipePredicate result(ItemStack stack) {
            return new ShapedRecipePredicate(keyToIngredient, pattern, stack);
        }
    }
}
