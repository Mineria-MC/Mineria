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

	//Utils
	private static final Item.Properties BASIC = new Item.Properties().group(Mineria.MINERIA_GROUP);

	//Items
	public static final Item GOLD_STICK = register("gold_stick", new MineriaItem());
	public static final Item IRON_STICK = register("iron_stick", new MineriaItem());
	public static final Item FILTER = register("filter", new Item(new Item.Properties().group(Mineria.MINERIA_GROUP).maxStackSize(4)));
	public static final Item MINERIA_XP_ORB = register("mineria_xp_orb", new XPOrbItem());
	public static final Item CUP = register("cup", new Item(new Item.Properties().group(Mineria.MINERIA_GROUP).maxStackSize(16)));
	//public static final Item water_bowl = register("water_bowl", new ItemBase());
	public static final Item VANADIUM_INGOT = register("vanadium_ingot", new MineriaItem());
	public static final Item VANADIUM_HELMET = register("vanadium_helmet", new ArmorBuilder(MineriaItem.ArmorMaterial.VANADIUM, EquipmentSlotType.HEAD).onArmorTick((stack, world, player) -> player.addPotionEffect(new EffectInstance(Effects.NIGHT_VISION, (12 * 20), 0, false, false))).build());
	
	//Copper
	public static final Item COPPER_INGOT = register("copper_ingot", new MineriaItem());
	public static final Item copper_bow = register("copper_bow", new BowBase(new Item.Properties().group(Mineria.MINERIA_GROUP).maxDamage(528)));
	public static final Item copper_stick = register("copper_stick", new MineriaItem());
	public static final Item copper_sword = register("copper_sword", new SwordItem(MineriaItem.ItemTier.COPPER, 3, -2.4F, BASIC));
	
	//Lead
	public static final Item lead_ingot = register("lead_ingot", new MineriaItem());
	public static final Item lead_sword = register("lead_sword", new SwordItem(MineriaItem.ItemTier.LEAD, 3, -2.4F, BASIC));
	public static final Item lead_nugget = register("lead_nugget", new MineriaItem());
	public static final Item compressed_lead_ingot = register("compressed_lead_ingot", new MineriaItem());
	public static final Item compressed_lead_sword = register("compressed_lead_sword", new SwordItem(MineriaItem.ItemTier.COMPRESSED_LEAD, 1, 1.8F, BASIC));
	
	//Lonsdaleite
	public static final Item lonsdaleite = register("lonsdaleite", new MineriaItem());
	public static final Item lonsdaleite_axe = register("lonsdaleite_axe", new AxeItem(MineriaItem.ItemTier.LONSDALEITE, 8.0F, -3.6F, BASIC));
	public static final Item lonsdaleite_dagger = register("lonsdaleite_dagger", new CustomWeaponItem(MineriaItem.ItemTier.LONSDALEITE, 7.5F, -1.75F, BASIC));
	public static final Item lonsdaleite_double_axe = register("lonsdaleite_double_axe", new AxeItem(MineriaItem.ItemTier.LONSDALEITE, 13.5F - MineriaItem.ItemTier.LONSDALEITE.getAttackDamage(), -3.4F, BASIC));
	public static final Item lonsdaleite_pickaxe = register("lonsdaleite_pickaxe", new PickaxeItem(MineriaItem.ItemTier.LONSDALEITE, 1, -2.8F, BASIC));
	public static final Item lonsdaleite_shovel = register("lonsdaleite_shovel", new ShovelItem(MineriaItem.ItemTier.LONSDALEITE, 1.5F, -3.0F, BASIC));
	public static final Item lonsdaleite_sword = register("lonsdaleite_sword", new SwordItem(MineriaItem.ItemTier.LONSDALEITE, 3, -2.4F, BASIC));
	public static final Item lonsdaleite_hoe = register("lonsdaleite_hoe", new HoeItem(MineriaItem.ItemTier.LONSDALEITE, -4, 0F, BASIC));
	public static final Item LONSDALEITE_BOOTS = register("lonsdaleite_boots", new ArmorBuilder(MineriaItem.ArmorMaterial.LONSDALEITE, EquipmentSlotType.FEET).build());
	public static final Item LONSDALEITE_LEGGINGS = register("lonsdaleite_leggings", new ArmorBuilder(MineriaItem.ArmorMaterial.LONSDALEITE, EquipmentSlotType.LEGS).build());
	public static final Item LONSDALEITE_CHESTPLATE = register("lonsdaleite_chestplate", new ArmorBuilder(MineriaItem.ArmorMaterial.LONSDALEITE, EquipmentSlotType.CHEST).build());
	public static final Item LONSDALEITE_HELMET = register("lonsdaleite_helmet", new ArmorBuilder(MineriaItem.ArmorMaterial.LONSDALEITE, EquipmentSlotType.HEAD).build());
	
	//Silver
	public static final Item silver_ingot = register("silver_ingot", new MineriaItem());
	public static final Item silver_apple = register("silver_apple", new MineriaItem());
	public static final Item silver_nugget = register("silver_nugget", new MineriaItem());
	public static final Item silver_stick = register("silver_stick", new MineriaItem());
	public static final Item silver_axe = register("silver_axe", new MineriaItem());
	public static final Item silver_pickaxe = register("silver_pickaxe", new MineriaItem());
	public static final Item silver_shovel = register("silver_shovel", new MineriaItem());
	public static final Item silver_sword = register("silver_sword", new MineriaItem());
	public static final Item silver_hoe = register("silver_hoe", new MineriaItem());
	public static final Item SILVER_BOOTS = register("silver_boots", new ArmorBuilder(MineriaItem.ArmorMaterial.SILVER, EquipmentSlotType.FEET).build());
	public static final Item SILVER_LEGGINGS = register("silver_leggings", new ArmorBuilder(MineriaItem.ArmorMaterial.SILVER, EquipmentSlotType.LEGS).build());
	public static final Item SILVER_CHESTPLATE = register("silver_chestplate", new ArmorBuilder(MineriaItem.ArmorMaterial.SILVER, EquipmentSlotType.CHEST).build());
	public static final Item SILVER_HELMET = register("silver_helmet", new ArmorBuilder(MineriaItem.ArmorMaterial.SILVER, EquipmentSlotType.HEAD).build());
	
	//Titane
	public static final Item titane_ingot = register("titane_ingot", new MineriaItem());
	public static final Item titane_nugget = register("titane_nugget", new MineriaItem());
	public static final Item titane_axe = register("titane_axe", new MineriaItem());
	public static final Item titane_dagger = register("titane_dagger", new MineriaItem());
	public static final Item titane_double_axe = register("titane_double_axe", new MineriaItem());
	public static final Item titane_hoe = register("titane_hoe", new MineriaItem());
	public static final Item titane_pickaxe = register("titane_pickaxe", new MineriaItem());
	public static final Item titane_shovel = register("titane_shovel", new MineriaItem());
	public static final Item titane_sword = register("titane_sword", new MineriaItem());
	public static final Item TITANE_BOOTS = register("titane_boots", new ArmorBuilder(MineriaItem.ArmorMaterial.TITANE, EquipmentSlotType.FEET).build());
	public static final Item TITANE_LEGGINGS = register("titane_leggings", new ArmorBuilder(MineriaItem.ArmorMaterial.TITANE, EquipmentSlotType.LEGS).build());
	public static final Item TITANE_CHESTPLATE = register("titane_chestplate", new ArmorBuilder(MineriaItem.ArmorMaterial.TITANE, EquipmentSlotType.CHEST).build());
	public static final Item TITANE_HELMET = register("titane_helmet", new ArmorBuilder(MineriaItem.ArmorMaterial.TITANE, EquipmentSlotType.HEAD).build());
	
	//CustomSword
	public static final Item titane_sword_with_copper_handle = register("titane_sword_with_copper_handle", new MineriaItem());
	public static final Item titane_sword_with_silver_handle = register("titane_sword_with_silver_handle", new MineriaItem());
	public static final Item titane_sword_with_gold_handle = register("titane_sword_with_gold_handle", new MineriaItem());
	public static final Item titane_sword_with_iron_handle = register("titane_sword_with_iron_handle", new MineriaItem());

	//Drinks
	public static final Item plantain_tea = register("plantain_tea", new DrinkBase(BASIC, () -> new EffectInstance(Effects.STRENGTH, 600, 1, true, true)));
	public static final Item mint_tea = register("mint_tea", new DrinkBase(BASIC, () -> new EffectInstance(Effects.SPEED, 1200, 2, true, true), () -> new EffectInstance(Effects.JUMP_BOOST, 1200, 1, true, true)));
	public static final Item thyme_tea = register("thyme_tea", new DrinkBase(BASIC, () -> new EffectInstance(Effects.REGENERATION, 200, 2, true, true)));
	public static final Item nettle_tea = register("nettle_tea", new DrinkBase(BASIC, () -> new EffectInstance(Effects.ABSORPTION, 900, 0, true, true), () -> new EffectInstance(Effects.REGENERATION, 100, 1, true, true)));
	public static final Item pulmonary_tea = register("pulmonary_tea", new DrinkBase(BASIC, () -> new EffectInstance(Effects.RESISTANCE, 600, 1, true, true)));;

	//Secret

		/*
		mineria_xp_orb = register(new XPOrbItem("mineria_xp_orb"));

		lonsdaleite = register(new ItemBase("lonsdaleite"));
		lonsdaleite_axe = register(new AxeBase("lonsdaleite_axe", ItemsInit.LONSDALEITE, -3.0F, 8.0F));
		lonsdaleite_dagger = register(new CustomWeapon("lonsdaleite_dagger", 7.5F, -1.75F, ItemsInit.LONSDALEITE));
		lonsdaleite_double_axe = register(new DoubleAxeBase("lonsdaleite_double_axe", 13.5F, -3.4F, ItemsInit.LONSDALEITE, Sets.newHashSet(Blocks.PLANKS, Blocks.BOOKSHELF, Blocks.LOG, Blocks.LOG2, Blocks.CHEST, Blocks.PUMPKIN, Blocks.LIT_PUMPKIN, Blocks.MELON_BLOCK, Blocks.LADDER, Blocks.WOODEN_BUTTON, Blocks.WOODEN_PRESSURE_PLATE)));
		lonsdaleite_pickaxe = register(new PickaxeBase("lonsdaleite_pickaxe", ItemsInit.LONSDALEITE));
		lonsdaleite_shovel = register(new ShovelBase("lonsdaleite_shovel", ItemsInit.LONSDALEITE));
		lonsdaleite_sword = register(new SwordBase("lonsdaleite_sword", ItemsInit.LONSDALEITE));
		lonsdaleite_hoe = register(new HoeBase("lonsdaleite_hoe", ItemsInit.LONSDALEITE));
		lonsdaleite_boots = register(new ArmorBuilder(ItemsInit.LONSDALEITEARMOR, EntityEquipmentSlot.FEET).build("lonsdaleite_boots"));
		lonsdaleite_leggings = register(new ArmorBuilder(ItemsInit.LONSDALEITEARMOR, EntityEquipmentSlot.LEGS).build("lonsdaleite_leggings"));
		lonsdaleite_chestplate = register(new ArmorBuilder(ItemsInit.LONSDALEITEARMOR, EntityEquipmentSlot.CHEST).build("lonsdaleite_chestplate"));
		lonsdaleite_helmet = register(new ArmorBuilder(ItemsInit.LONSDALEITEARMOR, EntityEquipmentSlot.HEAD).build("lonsdaleite_helmet"));

		silver_ingot = register(new ItemBase("silver_ingot"));
		silver_apple = register(new FoodEffectBase("silver_apple", 4, 6.9F, false, (stack, world, player) -> {
			if(!world.isRemote)
			{
				player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, (20 * 20), 0, false, true));
				player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, (90 * 20), 0, false, true));
				player.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, (40*20), 1, false, true));
			}
		}));
		silver_nugget = register(new ItemBase("silver_nugget"));
		silver_stick = register(new ItemBase("silver_stick"));
		silver_axe = register(new AxeBase("silver_axe", ItemsInit.SILVER, -3.0F, 8.0F));
		silver_pickaxe = register(new PickaxeBase("silver_pickaxe", ItemsInit.SILVER));
		silver_shovel = register(new ShovelBase("silver_shovel", ItemsInit.SILVER));
		silver_sword = register(new SwordBase("silver_sword", ItemsInit.SILVER));
		silver_hoe = register(new HoeBase("silver_hoe", ItemsInit.SILVER));
		silver_boots = register(new ArmorBuilder(ItemsInit.SILVERARMOR, EntityEquipmentSlot.FEET).build("silver_boots"));
		silver_leggings = register(new ArmorBuilder(ItemsInit.SILVERARMOR, EntityEquipmentSlot.LEGS).build("silver_leggings"));
		silver_chestplate = register(new ArmorBuilder(ItemsInit.SILVERARMOR, EntityEquipmentSlot.CHEST).build("silver_chestplate"));
		silver_helmet = register(new ArmorBuilder(ItemsInit.SILVERARMOR, EntityEquipmentSlot.HEAD).build("silver_helmet"));

		titane_ingot = register(new ItemBase("titane_ingot"));
		titane_nugget = register(new ItemBase("titane_nugget"));
		titane_axe = register(new AxeBase("titane_axe", ItemsInit.TITANE, -3.0F, 8.0F));
		titane_dagger = register(new CustomWeapon("titane_dagger", 4.5F, -2F, ItemsInit.TITANE));
		titane_double_axe = register(new DoubleAxeBase("titane_double_axe", 10.5F, -3.6F, ItemsInit.TITANE, Sets.newHashSet(Blocks.PLANKS, Blocks.BOOKSHELF, Blocks.LOG, Blocks.LOG2, Blocks.CHEST, Blocks.PUMPKIN, Blocks.LIT_PUMPKIN, Blocks.MELON_BLOCK, Blocks.LADDER, Blocks.WOODEN_BUTTON, Blocks.WOODEN_PRESSURE_PLATE)));
		titane_hoe = register(new HoeBase("titane_hoe", ItemsInit.TITANE));
		titane_pickaxe = register(new PickaxeBase("titane_pickaxe", ItemsInit.TITANE));
		titane_shovel = register(new ShovelBase("titane_shovel", ItemsInit.TITANE));
		titane_sword = register(new SwordBase("titane_sword", ItemsInit.TITANE));
		titane_boots = register(new ArmorBuilder(ItemsInit.TITANEARMOR, EntityEquipmentSlot.FEET).build("titane_boots"));
		titane_leggings = register(new ArmorBuilder(ItemsInit.TITANEARMOR, EntityEquipmentSlot.LEGS).build("titane_leggings"));
		titane_chestplate = register(new ArmorBuilder(ItemsInit.TITANEARMOR, EntityEquipmentSlot.CHEST).build("titane_chestplate"));
		titane_helmet = register(new ArmorBuilder(ItemsInit.TITANEARMOR, EntityEquipmentSlot.HEAD).build("titane_helmet"));

		titane_sword_with_copper_handle = register(new CustomWeapon("titane_sword_with_copper_handle", 6F, -2.2F, ItemsInit.TITANE, 4096));
		titane_sword_with_silver_handle = register(new CustomWeapon("titane_sword_with_silver_handle", 3F, -1F, ItemsInit.TITANE, 2048));
		titane_sword_with_gold_handle = register(new CustomWeapon("titane_sword_with_gold_handle", 8.5F, -3F, ItemsInit.TITANE, 2048));
		titane_sword_with_iron_handle = register(new CustomWeapon("titane_sword_with_iron_handle", 8.5F, -2.4F, ItemsInit.TITANE, 1024));
		*/

	private static Item register(String name, Item instance)
	{
		ITEMS.register(name, () -> instance);
		return instance;
	}
}
