package io.github.mineria_mc.mineria.common.registries;

import java.util.HashMap;
import java.util.Map;

import io.github.mineria_mc.mineria.common.item.LonsdaleiteSwordItem;
import io.github.mineria_mc.mineria.common.item.MineriaItem;
import io.github.mineria_mc.mineria.util.Constants;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Item.Properties;

public class MineriaItemRegistry {
    
    private static final Map<String, Item> ITEMS = new HashMap<String, Item>();

    // Lead
    public static final Item LEAD_INGOT = register("lead_ingot", new MineriaItem());
    public static final Item LEAD_SWORD = register("lead_sword", new SwordItem(MineriaItem.ItemTier.LEAD, 3, -2.4f, new Properties()));
    public static final Item LEAD_NUGGET = register("lead_nugget", new MineriaItem());
    public static final Item COMPRESSED_LEAD_INGOT = register("compressed_lead_ingot", new MineriaItem());
    public static final Item COMPRESSED_LEAD_SWORD = register("compressed_lead_sword", new SwordItem(MineriaItem.ItemTier.COMPRESSED_LEAD, 3, -2.4f, new Properties()));

    // Lonsdaleite
    public static final Item LONSDALEITE = register("lonsdaleite", new MineriaItem());
    public static final Item LONSDALEITE_AXE = register("lonsdaleite_axe", new AxeItem(MineriaItem.ItemTier.LONSDALEITE, -2.0f, -3.0f, new Properties()));
    public static final Item LONSDALEITE_DOUBLE_AXE = register("lonsdaleite_double_axe", new AxeItem(MineriaItem.ItemTier.LONSDALEITE, 13.5f - MineriaItem.ItemTier.LONSDALEITE.getAttackDamageBonus(), -3.4f, new Properties()));
    public static final Item LONSDALEITE_PICKAXE = register("lonsdaleite_pickaxe", new PickaxeItem(MineriaItem.ItemTier.LONSDALEITE, 1, -2.8f, new Properties()));
    public static final Item LONSDALEITE_SHOVEL = register("lonsdaleite_shovel", new ShovelItem(MineriaItem.ItemTier.LONSDALEITE, 1.5f, -3.0f, new Properties()));
    public static final Item LONSDALEITE_SWORD = register("lonsdaleite_sword", new LonsdaleiteSwordItem());
    public static final Item LONSDALEITE_HOE = register("lonsdaleite_hoe", new HoeItem(MineriaItem.ItemTier.LONSDALEITE, -7, 0f, new Properties()));
    
    // Silver
    public static final Item SILVER_INGOT = register("silver_ingot", new MineriaItem());
    public static final Item SILVER_APPLE = register("silver_apple", new Item(new Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(6.9f).alwaysEat().fast().effect(
        new MobEffectInstance(MobEffects.REGENERATION, (20 * 20), 0, false, true), 1.5f
    ).effect(
        new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, (90 * 20), 0, false, true), 1.0f
    ).effect(
        new MobEffectInstance(MobEffects.ABSORPTION, (40 * 20), 1, false, true), 1.0f
    ).build()).stacksTo(64)));
    public static final Item SILVER_NUGGET = register("silver_nugget", new MineriaItem());
    public static final Item SILVER_STICK = register("silver_stick", new MineriaItem());
    public static final Item SILVER_AXE = register("silver_axe", new AxeItem(MineriaItem.ItemTier.SILVER, 2.0f, -3.0f, new Properties()));
    public static final Item SILVER_PICKAXE = register("silver_pickaxe", new PickaxeItem(MineriaItem.ItemTier.SILVER, 1, -2.8f, new Properties()));
    public static final Item SILVER_SHOVEL = register("silver_shovel", new ShovelItem(MineriaItem.ItemTier.SILVER, 1.5f, -3.0f, new Properties()));
    public static final Item SILVER_SWORD = register("silver_sword", new SwordItem(MineriaItem.ItemTier.SILVER, 3, -2.4f, new Properties()));
    public static final Item SILVER_HOE = register("silver_hoe", new HoeItem(MineriaItem.ItemTier.SILVER, (int) -2.6f, 0f, new Properties()));

    // Titane
    public static final Item TITANE_INGOT = register("titane_ingot", new MineriaItem());

    public static void register() {
        for(Map.Entry<String, Item> entry : ITEMS.entrySet()) {
            Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Constants.MODID, entry.getKey()), entry.getValue());
        }
    }

    private static Item register(String itemName, Item item) {
        ITEMS.put(itemName, item);
        return item;
    }
}
