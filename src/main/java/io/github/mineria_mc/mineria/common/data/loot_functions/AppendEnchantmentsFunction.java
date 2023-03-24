package io.github.mineria_mc.mineria.common.data.loot_functions;

import com.google.gson.*;
import io.github.mineria_mc.mineria.common.init.MineriaLootItemFunctionTypes;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class AppendEnchantmentsFunction extends LootItemConditionalFunction {
    private final Map<Enchantment, NumberProvider> enchantments;

    protected AppendEnchantmentsFunction(LootItemCondition[] conditions, Map<Enchantment, NumberProvider> enchantments) {
        super(conditions);
        this.enchantments = enchantments;
    }

    @Nonnull
    @Override
    public LootItemFunctionType getType() {
        return MineriaLootItemFunctionTypes.APPEND_ENCHANTMENTS.get();
    }

    @Nonnull
    @Override
    protected ItemStack run(@Nonnull ItemStack stack, @Nonnull LootContext context) {
        Object2IntMap<Enchantment> enchantments = new Object2IntArrayMap<>();
        this.enchantments.forEach((enchantment, numberProvider) -> enchantments.put(enchantment, numberProvider.getInt(context)));
        if(stack.is(Items.BOOK)) {
            stack = new ItemStack(Items.ENCHANTED_BOOK);
        }
        if(stack.is(Items.ENCHANTED_BOOK)) {
            ItemStack bookStack = stack;
            enchantments.forEach((enchantment, level) -> EnchantedBookItem.addEnchantment(bookStack, new EnchantmentInstance(enchantment, level)));
            return bookStack;
        }
        Map<Enchantment, Integer> existingEnchants = EnchantmentHelper.getEnchantments(stack);
        enchantments.forEach((enchantment, level) -> {
            level = Math.max(level, 0);
            if (level == 0) {
                existingEnchants.remove(enchantment);
            } else {
                existingEnchants.put(enchantment, level);
            }
        });
        EnchantmentHelper.setEnchantments(existingEnchants, stack);
        return stack;
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<AppendEnchantmentsFunction> {
        @Override
        public void serialize(@Nonnull JsonObject json, @Nonnull AppendEnchantmentsFunction function, @Nonnull JsonSerializationContext ctx) {
            super.serialize(json, function, ctx);
            JsonObject enchantments = new JsonObject();
            for (Map.Entry<Enchantment, NumberProvider> enchantmentEntry : function.enchantments.entrySet()) {
                ResourceLocation enchantmentId = ForgeRegistries.ENCHANTMENTS.getKey(enchantmentEntry.getKey());
                if(enchantmentId == null) {
                    continue;
                }
                enchantments.add(enchantmentId.toString(), ctx.serialize(enchantmentEntry.getValue()));
            }
            json.add("enchantments", enchantments);
        }

        @Nonnull
        @Override
        public AppendEnchantmentsFunction deserialize(@Nonnull JsonObject json, @Nonnull JsonDeserializationContext ctx, @Nonnull LootItemCondition[] conditions) {
            Map<Enchantment, NumberProvider> enchantments = new HashMap<>();
            if(json.has("enchantments")) {
                JsonObject obj = GsonHelper.getAsJsonObject(json, "enchantments");

                for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
                    Enchantment enchantment = ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation(entry.getKey()));
                    if(enchantment == null) {
                        throw new JsonSyntaxException("Unknown enchantment id: " + entry.getKey());
                    }
                    enchantments.put(enchantment, ctx.deserialize(entry.getValue(), NumberProvider.class));
                }
            }
            return new AppendEnchantmentsFunction(conditions, enchantments);
        }
    }
}
