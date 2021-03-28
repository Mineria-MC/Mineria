package com.mineria.mod.init;

import com.mineria.mod.Mineria;
import com.mineria.mod.References;
import com.mineria.mod.items.*;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@SuppressWarnings("unused")
public class ItemsInit
{
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, References.MODID);

	//Items
	public static final Item GOLD_STICK = register("gold_stick", new MineriaItem());
	public static final Item IRON_STICK = register("iron_stick", new MineriaItem());
	public static final Item FILTER = register("filter", new Item(defaultProperties().maxStackSize(4)));
	public static final Item XP_ORB = register("xp_orb", new XPOrbItem(1, defaultProperties()));
	public static final Item COMPRESSED_XP_ORB = register("compressed_xp_orb", new XPOrbItem(4, defaultProperties().rarity(Rarity.UNCOMMON)));
	public static final Item SUPER_COMPRESSED_XP_ORB = register("super_compressed_xp_orb", new XPOrbItem(16, defaultProperties().rarity(Rarity.RARE)));
	public static final Item SUPER_DUPER_COMPRESSED_XP_ORB = register("super_duper_compressed_xp_orb", new XPOrbItem(64, defaultProperties().rarity(Rarity.EPIC)));
	public static final Item CUP = register("cup", new Item(defaultProperties().maxStackSize(16)));
	public static final Item VANADIUM_INGOT = register("vanadium_ingot", new MineriaItem());
	public static final Item VANADIUM_HELMET = register("vanadium_helmet", new ArmorBuilder(MineriaItem.ArmorMaterial.VANADIUM, EquipmentSlotType.HEAD).onArmorTick((stack, world, player) -> player.addPotionEffect(new EffectInstance(Effects.NIGHT_VISION, (12 * 20), 0, false, false))).build());
	
	//Copper
	public static final Item COPPER_INGOT = register("copper_ingot", new MineriaItem());
	public static final Item COPPER_BOW = register("copper_bow", new MineriaBow(4.0F, 1.15F, 1, defaultProperties().maxDamage(528)));
	public static final Item COPPER_STICK = register("copper_stick", new MineriaItem());
	public static final Item COPPER_SWORD = register("copper_sword", new SwordItem(MineriaItem.ItemTier.COPPER, 3, -2.4F, defaultProperties()));
	
	//Lead
	public static final Item LEAD_INGOT = register("lead_ingot", new MineriaItem());
	public static final Item LEAD_SWORD = register("lead_sword", new SwordItem(MineriaItem.ItemTier.LEAD, 3, -2.4F, defaultProperties()));
	public static final Item LEAD_NUGGET = register("lead_nugget", new MineriaItem());
	public static final Item COMPRESSED_LEAD_INGOT = register("compressed_lead_ingot", new MineriaItem());
	public static final Item COMPRESSED_LEAD_SWORD = register("compressed_lead_sword", new SwordItem(MineriaItem.ItemTier.COMPRESSED_LEAD, 3, -2.4F, defaultProperties()));
	
	//Lonsdaleite
	public static final Item LONSDALEITE = register("lonsdaleite", new MineriaItem());
	public static final Item LONSDALEITE_AXE = register("lonsdaleite_axe", new AxeItem(MineriaItem.ItemTier.LONSDALEITE, -2.0F, -3.0F, defaultProperties()));
	public static final Item LONSDALEITE_DAGGER = register("lonsdaleite_dagger", new CustomWeaponItem(MineriaItem.ItemTier.LONSDALEITE, 7.5F, -1.75F, defaultProperties()));
	public static final Item LONSDALEITE_DOUBLE_AXE = register("lonsdaleite_double_axe", new AxeItem(MineriaItem.ItemTier.LONSDALEITE, 13.5F - MineriaItem.ItemTier.LONSDALEITE.getAttackDamage(), -3.4F, defaultProperties()));
	public static final Item LONSDALEITE_PICKAXE = register("lonsdaleite_pickaxe", new PickaxeItem(MineriaItem.ItemTier.LONSDALEITE, 1, -2.8F, defaultProperties()));
	public static final Item LONSDALEITE_SHOVEL = register("lonsdaleite_shovel", new ShovelItem(MineriaItem.ItemTier.LONSDALEITE, 1.5F, -3.0F, defaultProperties()));
	public static final Item LONSDALEITE_SWORD = register("lonsdaleite_sword", new SwordItem(MineriaItem.ItemTier.LONSDALEITE, 3, -2.4F, defaultProperties()));
	public static final Item LONSDALEITE_HOE = register("lonsdaleite_hoe", new HoeItem(MineriaItem.ItemTier.LONSDALEITE, -7, 0F, defaultProperties()));
	public static final Item LONSDALEITE_BOOTS = register("lonsdaleite_boots", new ArmorBuilder(MineriaItem.ArmorMaterial.LONSDALEITE, EquipmentSlotType.FEET).build());
	public static final Item LONSDALEITE_LEGGINGS = register("lonsdaleite_leggings", new ArmorBuilder(MineriaItem.ArmorMaterial.LONSDALEITE, EquipmentSlotType.LEGS).build());
	public static final Item LONSDALEITE_CHESTPLATE = register("lonsdaleite_chestplate", new ArmorBuilder(MineriaItem.ArmorMaterial.LONSDALEITE, EquipmentSlotType.CHEST).build());
	public static final Item LONSDALEITE_HELMET = register("lonsdaleite_helmet", new ArmorBuilder(MineriaItem.ArmorMaterial.LONSDALEITE, EquipmentSlotType.HEAD).build());
	
	//Silver
	public static final Item SILVER_INGOT = register("silver_ingot", new MineriaItem());
	public static final Item SILVER_APPLE = register("silver_apple", new Item(defaultProperties().food(new Food.Builder().hunger(4).saturation(6.9F).setAlwaysEdible().fastToEat().effect(
			() -> new EffectInstance(Effects.REGENERATION, (20 * 20), 0, false, true), 1.0F
	).effect(
			() -> new EffectInstance(Effects.RESISTANCE, (90 * 20), 0, false, true), 1.0F
	).effect(
			() -> new EffectInstance(Effects.ABSORPTION, (40*20), 1, false, true), 1.0F
	).build()).maxStackSize(64)));
	public static final Item SILVER_NUGGET = register("silver_nugget", new MineriaItem());
	public static final Item SILVER_STICK = register("silver_stick", new MineriaItem());
	public static final Item SILVER_AXE = register("silver_axe", new AxeItem(MineriaItem.ItemTier.SILVER, 2.0F, -3.0F, defaultProperties()));
	public static final Item SILVER_PICKAXE = register("silver_pickaxe", new PickaxeItem(MineriaItem.ItemTier.SILVER, 1, -2.8F, defaultProperties()));
	public static final Item SILVER_SHOVEL = register("silver_shovel", new ShovelItem(MineriaItem.ItemTier.SILVER, 1.5F, -3.0F, defaultProperties()));
	public static final Item SILVER_SWORD = register("silver_sword", new SwordItem(MineriaItem.ItemTier.SILVER, 3, -2.4F, defaultProperties()));
	public static final Item SILVER_HOE = register("silver_hoe", new HoeItem(MineriaItem.ItemTier.SILVER, (int)-2.6F, 0F, defaultProperties()));
	public static final Item SILVER_BOOTS = register("silver_boots", new ArmorBuilder(MineriaItem.ArmorMaterial.SILVER, EquipmentSlotType.FEET).build());
	public static final Item SILVER_LEGGINGS = register("silver_leggings", new ArmorBuilder(MineriaItem.ArmorMaterial.SILVER, EquipmentSlotType.LEGS).build());
	public static final Item SILVER_CHESTPLATE = register("silver_chestplate", new ArmorBuilder(MineriaItem.ArmorMaterial.SILVER, EquipmentSlotType.CHEST).build());
	public static final Item SILVER_HELMET = register("silver_helmet", new ArmorBuilder(MineriaItem.ArmorMaterial.SILVER, EquipmentSlotType.HEAD).build());
	
	//Titane
	public static final Item TITANE_INGOT = register("titane_ingot", new MineriaItem());
	public static final Item TITANE_NUGGET = register("titane_nugget", new MineriaItem());
	public static final Item TITANE_AXE = register("titane_axe", new AxeItem(MineriaItem.ItemTier.TITANE, 1.0F, -3.0F, defaultProperties()));
	public static final Item TITANE_DAGGER = register("titane_dagger", new CustomWeaponItem(MineriaItem.ItemTier.TITANE, 4.5F, -2F, defaultProperties()));
	public static final Item TITANE_DOUBLE_AXE = register("titane_double_axe", new AxeItem(MineriaItem.ItemTier.TITANE, 10.5F - MineriaItem.ItemTier.TITANE.getAttackDamage(), -3.6F, defaultProperties()));
	public static final Item TITANE_HOE = register("titane_hoe", new HoeItem(MineriaItem.ItemTier.TITANE, -4, 0F, defaultProperties()));
	public static final Item TITANE_PICKAXE = register("titane_pickaxe", new PickaxeItem(MineriaItem.ItemTier.TITANE, 1, -2.8F, defaultProperties()));
	public static final Item TITANE_SHOVEL = register("titane_shovel", new ShovelItem(MineriaItem.ItemTier.TITANE, 1.5F, -3.0F, defaultProperties()));
	public static final Item TITANE_SWORD = register("titane_sword", new SwordItem(MineriaItem.ItemTier.TITANE, 3, -2.4F, defaultProperties()));
	public static final Item TITANE_BOOTS = register("titane_boots", new ArmorBuilder(MineriaItem.ArmorMaterial.TITANE, EquipmentSlotType.FEET).build());
	public static final Item TITANE_LEGGINGS = register("titane_leggings", new ArmorBuilder(MineriaItem.ArmorMaterial.TITANE, EquipmentSlotType.LEGS).build());
	public static final Item TITANE_CHESTPLATE = register("titane_chestplate", new ArmorBuilder(MineriaItem.ArmorMaterial.TITANE, EquipmentSlotType.CHEST).build());
	public static final Item TITANE_HELMET = register("titane_helmet", new ArmorBuilder(MineriaItem.ArmorMaterial.TITANE, EquipmentSlotType.HEAD).build());
	
	//CustomSword
	public static final Item TITANE_SWORD_WITH_COPPER_HANDLE = register("titane_sword_with_copper_handle", new CustomWeaponItem(MineriaItem.ItemTier.TITANE, 6F, -2.2F, defaultProperties().maxDamage(4096)));
	public static final Item TITANE_SWORD_WITH_SILVER_HANDLE = register("titane_sword_with_silver_handle", new CustomWeaponItem(MineriaItem.ItemTier.TITANE, 3F, -1F, defaultProperties().maxDamage(2048)));
	public static final Item TITANE_SWORD_WITH_GOLD_HANDLE = register("titane_sword_with_gold_handle", new CustomWeaponItem(MineriaItem.ItemTier.TITANE, 8.5F, -3F, defaultProperties().maxDamage(2048)));
	public static final Item TITANE_SWORD_WITH_IRON_HANDLE = register("titane_sword_with_iron_handle", new CustomWeaponItem(MineriaItem.ItemTier.TITANE, 8.5F, -2.4F, defaultProperties().maxDamage(1024)));

	//Drinks
	public static final Item PLANTAIN_TEA = register("plantain_tea", new DrinkItem(defaultProperties().maxStackSize(1).food(new Food.Builder().saturation(0).hunger(0).setAlwaysEdible().effect(
			() -> new EffectInstance(Effects.STRENGTH, 600, 1, true, true), 1.0F
	).build())));
	public static final Item MINT_TEA = register("mint_tea", new DrinkItem(defaultProperties().maxStackSize(1).food(new Food.Builder().saturation(0).hunger(0).setAlwaysEdible().effect(
			() -> new EffectInstance(Effects.SPEED, 1200, 2, true, true), 1.0F
	).effect(
			() -> new EffectInstance(Effects.JUMP_BOOST, 1200, 1, true, true), 1.0F
	).build())));
	public static final Item THYME_TEA = register("thyme_tea", new DrinkItem(defaultProperties().maxStackSize(1).food(new Food.Builder().saturation(0).hunger(0).setAlwaysEdible().effect(
			() -> new EffectInstance(Effects.REGENERATION, 200, 2, true, true), 1.0F
	).build())));
	public static final Item NETTLE_TEA = register("nettle_tea", new DrinkItem(defaultProperties().maxStackSize(1).food(new Food.Builder().saturation(0).hunger(0).setAlwaysEdible().effect(
			() -> new EffectInstance(Effects.ABSORPTION, 900, 0, true, true), 1.0F
	).effect(
			() -> new EffectInstance(Effects.REGENERATION, 100, 1, true, true), 1.0F
	).build())));
	public static final Item PULMONARY_TEA = register("pulmonary_tea", new DrinkItem(defaultProperties().maxStackSize(1).food(new Food.Builder().saturation(0).hunger(0).setAlwaysEdible().effect(
			() -> new EffectInstance(Effects.RESISTANCE, 600, 1, true, true), 1.0F
	).build())));

	public static final Item GOLDEN_SILVERFISH_SPAWN_EGG = register("golden_silverfish_spawn_egg", new MineriaSpawnEggItem(EntityInit.GOLDEN_SILVERFISH, 12888340, 12852517, defaultProperties()));

	private static Item register(String name, Item instance)
	{
		ITEMS.register(name, () -> instance);
		return instance;
	}

	private static Item.Properties defaultProperties()
	{
		return new Item.Properties().group(Mineria.MINERIA_GROUP);
	}
}
