package io.github.mineria_mc.mineria.common.registries;

import java.util.HashMap;
import java.util.Map;

import io.github.mineria_mc.mineria.common.item.ArmorBuilder;
import io.github.mineria_mc.mineria.common.item.CustomWeaponItem;
import io.github.mineria_mc.mineria.common.item.LonsdaleiteSwordItem;
import io.github.mineria_mc.mineria.common.item.MineriaItem;
import io.github.mineria_mc.mineria.common.item.XpOrbItem;
import io.github.mineria_mc.mineria.util.Constants;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Item.Properties;

public class MineriaItemRegistry {
    
    private static final Map<String, Item> ITEMS = new HashMap<String, Item>();

    // Misc
    public static final Item XP_ORB = register("xp_orb", new XpOrbItem(1, new Properties()));
    public static final Item COMPRESSED_XP_ORB = register("compressed_xp_orb", new XpOrbItem(4, new Properties().rarity(Rarity.UNCOMMON)));
    public static final Item SUPER_COMPRESSED_XP_ORB = register("super_compressed_xp_orb", new XpOrbItem(16, new Properties().rarity(Rarity.RARE)));
    public static final Item SUPER_DUPER_COMPRESSED_XP_ORB = register("super_duper_compressed_xp_orb", new XpOrbItem(64, new Properties().rarity(Rarity.EPIC)));
    public static final Item VANADIUM_INGOT = register("vanadium_ingot", new MineriaItem());
    public static final Item VANADIUM_HELMET = register("vanadium_helmet", new ArmorBuilder(MineriaItem.ArmorMaterial.VANADIUM, ArmorItem.Type.HELMET).onArmorTick((stack, world, player) -> player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, (12 * 20), 0, false, false))).build());

    // Lead
    public static final Item LEAD_INGOT = register("lead_ingot", new MineriaItem());
    public static final Item LEAD_SWORD = register("lead_sword", new SwordItem(MineriaItem.ItemTier.LEAD, 3, -2.4f, new Properties()));
    public static final Item LEAD_NUGGET = register("lead_nugget", new MineriaItem());
    public static final Item COMPRESSED_LEAD_INGOT = register("compressed_lead_ingot", new MineriaItem());
    public static final Item COMPRESSED_LEAD_SWORD = register("compressed_lead_sword", new SwordItem(MineriaItem.ItemTier.COMPRESSED_LEAD, 3, -2.4f, new Properties()));

    // Lonsdaleite
    public static final Item LONSDALEITE = register("lonsdaleite", new MineriaItem());
    public static final Item LONSDALEITE_AXE = register("lonsdaleite_axe", new AxeItem(MineriaItem.ItemTier.LONSDALEITE, -2.0f, -3.0f, new Properties()));
    public static final Item LONSDALEITE_DAGGER = register("lonsdaleite_dagger", new CustomWeaponItem(MineriaItem.ItemTier.LONSDALEITE, 7.5f, -1.75f, 15, new Properties()));
    public static final Item LONSDALEITE_DOUBLE_AXE = register("lonsdaleite_double_axe", new AxeItem(MineriaItem.ItemTier.LONSDALEITE, 13.5f - MineriaItem.ItemTier.LONSDALEITE.getAttackDamageBonus(), -3.4f, new Properties()));
    public static final Item LONSDALEITE_PICKAXE = register("lonsdaleite_pickaxe", new PickaxeItem(MineriaItem.ItemTier.LONSDALEITE, 1, -2.8f, new Properties()));
    public static final Item LONSDALEITE_SHOVEL = register("lonsdaleite_shovel", new ShovelItem(MineriaItem.ItemTier.LONSDALEITE, 1.5f, -3.0f, new Properties()));
    public static final Item LONSDALEITE_SWORD = register("lonsdaleite_sword", new LonsdaleiteSwordItem());
    public static final Item LONSDALEITE_HOE = register("lonsdaleite_hoe", new HoeItem(MineriaItem.ItemTier.LONSDALEITE, -7, 0f, new Properties()));
    public static final Item LONSDALEITE_HELMET = register("lonsdaleite_helmet", new ArmorBuilder(MineriaItem.ArmorMaterial.LONSDALEITE, ArmorItem.Type.HELMET).build());
    public static final Item LONSDALEITE_CHESTPLATE = register("lonsdaleite_chestplate", new ArmorBuilder(MineriaItem.ArmorMaterial.LONSDALEITE, ArmorItem.Type.CHESTPLATE).build());
    public static final Item LONSDALEITE_LEGGINGS = register("lonsdaleite_leggings", new ArmorBuilder(MineriaItem.ArmorMaterial.LONSDALEITE, ArmorItem.Type.LEGGINGS).build());
    public static final Item LONSDALEITE_BOOTS = register("lonsdaleite_boots", new ArmorBuilder(MineriaItem.ArmorMaterial.LONSDALEITE, ArmorItem.Type.BOOTS).build());

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
    public static final Item SILVER_HELMET = register("silver_helmet", new ArmorBuilder(MineriaItem.ArmorMaterial.SILVER, ArmorItem.Type.HELMET).build());
    public static final Item SILVER_CHESTPLATE = register("silver_chestplate", new ArmorBuilder(MineriaItem.ArmorMaterial.SILVER, ArmorItem.Type.CHESTPLATE).build());
    public static final Item SILVER_LEGGINGS = register("silver_leggings", new ArmorBuilder(MineriaItem.ArmorMaterial.SILVER, ArmorItem.Type.LEGGINGS).build());
    public static final Item SILVER_BOOTS = register("silver_boots", new ArmorBuilder(MineriaItem.ArmorMaterial.SILVER, ArmorItem.Type.BOOTS).build());

    // Titane
    public static final Item TITANE_INGOT = register("titane_ingot", new MineriaItem());
    public static final Item TITANE_NUGGET = register("titane_nugget", new MineriaItem());
    public static final Item TITANE_AXE = register("titane_axe", new AxeItem(MineriaItem.ItemTier.TITANE, 1.0f, -3.0f, new Properties()));
    public static final Item TITANE_DAGGER = register("titane_dagger", new CustomWeaponItem(MineriaItem.ItemTier.TITANE, 4.5f, -2.0f, 17, new Properties()));
    public static final Item TITANE_DOUBLE_AXE = register("titane_double_axe", new AxeItem(MineriaItem.ItemTier.TITANE, 10.5f - MineriaItem.ItemTier.TITANE.getAttackDamageBonus(), -3.6f, new Properties()));
    public static final Item TITANE_HOE = register("titane_hoe", new HoeItem(MineriaItem.ItemTier.TITANE, -4, 0, new Properties()));
    public static final Item TITANE_PICKAXE = register("titane_pickaxe", new PickaxeItem(MineriaItem.ItemTier.TITANE, 1, -2.8f, new Properties()));
    public static final Item TITANE_SHOVEL = register("titane_shovel", new ShovelItem(MineriaItem.ItemTier.TITANE, 1.5f, -3.0f, new Properties()));
    public static final Item TITANE_SWORD = register("titane_sword", new SwordItem(MineriaItem.ItemTier.TITANE, 3, -2.4f, new Properties()));
    public static final Item TITANE_HELMET = register("titane_helmet", new ArmorBuilder(MineriaItem.ArmorMaterial.TITANE, ArmorItem.Type.HELMET).build());
    public static final Item TITANE_CHESTPLATE = register("titane_chestplate", new ArmorBuilder(MineriaItem.ArmorMaterial.TITANE, ArmorItem.Type.CHESTPLATE).build());
    public static final Item TITANE_LEGGINGS = register("titane_leggings", new ArmorBuilder(MineriaItem.ArmorMaterial.TITANE, ArmorItem.Type.LEGGINGS).build());
    public static final Item TITANE_BOOTS = register("titane_boots", new ArmorBuilder(MineriaItem.ArmorMaterial.TITANE, ArmorItem.Type.BOOTS).build());

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
