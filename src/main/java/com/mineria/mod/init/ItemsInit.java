package com.mineria.mod.init;

import com.google.common.collect.Sets;

import com.mineria.mod.References;
import com.mineria.mod.items.*;
import com.sun.rmi.rmid.ExecOptionPermission;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = References.MODID)
public class ItemsInit
{
	//Items
	public static Item gold_stick;
	public static Item iron_stick;
	public static Item filter;
	public static Item mineria_xp_orb;
	public static Item cup;
	public static Item water_bowl;
	public static Item vanadium_ingot;
	public static Item vanadium_helmet;
	
	//Copper
	public static Item copper_ingot;
	public static Item copper_bow;
	public static Item copper_stick;
	public static Item copper_sword;
	
	//Lead
	public static Item lead_ingot;
	public static Item lead_sword;
	public static Item lead_nugget;
	public static Item compressed_lead_ingot;
	public static Item compressed_lead_sword;
	
	//Lonsdaleite
	public static Item lonsdaleite;
	public static Item lonsdaleite_axe;
	public static Item lonsdaleite_dagger;
	public static Item lonsdaleite_double_axe;
	public static Item lonsdaleite_pickaxe;
	public static Item lonsdaleite_shovel;
	public static Item lonsdaleite_sword;
	public static Item lonsdaleite_hoe;
	public static Item lonsdaleite_boots;
	public static Item lonsdaleite_leggings;
	public static Item lonsdaleite_chestplate;
	public static Item lonsdaleite_helmet;
	
	//Silver
	public static Item silver_ingot;
	public static Item silver_apple;
	public static Item silver_nugget;
	public static Item silver_stick;
	public static Item silver_axe;
	public static Item silver_pickaxe;
	public static Item silver_shovel;
	public static Item silver_sword;
	public static Item silver_hoe;
	public static Item silver_boots;
	public static Item silver_leggings;
	public static Item silver_chestplate;
	public static Item silver_helmet;
	
	//Titane
	public static Item titane_ingot;
	public static Item titane_nugget;
	public static Item titane_axe;
	public static Item titane_dagger;
	public static Item titane_double_axe;
	public static Item titane_hoe;
	public static Item titane_pickaxe;
	public static Item titane_shovel;
	public static Item titane_sword;
	public static Item titane_boots;
	public static Item titane_leggings;
	public static Item titane_chestplate;
	public static Item titane_helmet;
	
	//CustomSword
	public static Item titane_sword_with_copper_handle;
	public static Item titane_sword_with_silver_handle;
	public static Item titane_sword_with_gold_handle;
	public static Item titane_sword_with_iron_handle;

	//Drinks
	public static Item plantain_tea;
	public static Item mint_tea;
	public static Item thyme_tea;
	public static Item nettle_tea;
	public static Item pulmonary_tea;

	//Secret
	
	//Tool Materials
	public static final Item.ToolMaterial COPPER = EnumHelper.addToolMaterial("COPPER", 2, 187, 3.5F, 1.5F, 4);
	public static final Item.ToolMaterial LEAD = EnumHelper.addToolMaterial("LEAD", 2, 294, 6.25F, 2.2F, 12);
	public static final Item.ToolMaterial COMPRESSED_LEAD = EnumHelper.addToolMaterial("COMPRESSED_LEAD", 2, 576, 6.25F, 3.0F, 12);
	public static final Item.ToolMaterial LONSDALEITE = EnumHelper.addToolMaterial("LONSDALEITE", 3, 3460, 20.0F, 7.0F, 8);
	public static final Item.ToolMaterial SILVER = EnumHelper.addToolMaterial("SILVER", 2, 621, 7.0F, 2.6F, 16);
	public static final Item.ToolMaterial TITANE = EnumHelper.addToolMaterial("TITANE", 3, 2048, 12.0F, 4.0F, 12);

	//Armor Materials
	public static final ArmorMaterial LONSDALEITEARMOR = EnumHelper.addArmorMaterial("LONSDALEITEARMOR", References.MODID + ":lonsdaleite", 62, new int[]{6, 9, 11, 7}, 8, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 4.0F);
	public static final ArmorMaterial SILVERARMOR = EnumHelper.addArmorMaterial("SILVERARMOR", References.MODID + ":silver", 17, new int[]{2, 6, 6, 3}, 16, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 0.5F);
	public static final ArmorMaterial TITANEARMOR = EnumHelper.addArmorMaterial("TITANEARMOR", References.MODID + ":titane", 45, new int[]{4, 7, 9, 4}, 12, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 1.0F);
	public static final ArmorMaterial VANADIUM = EnumHelper.addArmorMaterial("VANADIUM", References.MODID + ":vanadium", 37, new int[]{0, 0, 0, 4}, 10, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 0.0F);
	
	public static void init()
	{
		//Items
		gold_stick = new ItemBase("gold_stick");
		iron_stick = new ItemBase("iron_stick");
		filter = new ItemFilter("filter");
		mineria_xp_orb = new XPOrbItem("mineria_xp_orb");
		cup = new CupItem("cup");
		water_bowl = new WaterBowl("water_bowl");
		vanadium_ingot = new ItemBase("vanadium_ingot");
		vanadium_helmet = new VanadiumHelmet("vanadium_helmet", ItemsInit.VANADIUM, 1, EntityEquipmentSlot.HEAD);
		
		//Copper
		copper_ingot = new ItemBase("copper_ingot");
		copper_bow = new BowBase("copper_bow");
		copper_stick = new ItemBase("copper_stick");
		copper_sword = new SwordBase("copper_sword", ItemsInit.COPPER);
		
		//Lead
		lead_ingot = new ItemBase("lead_ingot");
		lead_sword = new SwordBase("lead_sword", ItemsInit.LEAD);
		lead_nugget = new ItemBase("lead_nugget");
		compressed_lead_ingot = new ItemBase("compressed_lead_ingot");
		compressed_lead_sword = new SwordBase("compressed_lead_sword", ItemsInit.COMPRESSED_LEAD);
		
		//Lonsdaleite
		lonsdaleite = new ItemBase("lonsdaleite");
		lonsdaleite_axe = new AxeBase("lonsdaleite_axe", ItemsInit.LONSDALEITE, -3.0F, 8.0F);
		lonsdaleite_dagger = new CustomWeapon("lonsdaleite_dagger", 7.5F, -1.75F, ItemsInit.LONSDALEITE);
		lonsdaleite_double_axe = new DoubleAxeBase("lonsdaleite_double_axe", 13.5F, -3.4F, ItemsInit.LONSDALEITE, Sets.newHashSet(Blocks.PLANKS, Blocks.BOOKSHELF, Blocks.LOG, Blocks.LOG2, Blocks.CHEST, Blocks.PUMPKIN, Blocks.LIT_PUMPKIN, Blocks.MELON_BLOCK, Blocks.LADDER, Blocks.WOODEN_BUTTON, Blocks.WOODEN_PRESSURE_PLATE));
		lonsdaleite_pickaxe = new PickaxeBase("lonsdaleite_pickaxe", ItemsInit.LONSDALEITE);
		lonsdaleite_shovel = new ShovelBase("lonsdaleite_shovel", ItemsInit.LONSDALEITE);
		lonsdaleite_sword = new SwordBase("lonsdaleite_sword", ItemsInit.LONSDALEITE);
		lonsdaleite_hoe = new HoeBase("lonsdaleite_hoe", ItemsInit.LONSDALEITE);
		lonsdaleite_boots = new ArmorBase("lonsdaleite_boots", ItemsInit.LONSDALEITEARMOR, 1, EntityEquipmentSlot.FEET);
		lonsdaleite_leggings = new ArmorBase("lonsdaleite_leggings", ItemsInit.LONSDALEITEARMOR, 2, EntityEquipmentSlot.LEGS);
		lonsdaleite_chestplate = new ArmorBase("lonsdaleite_chestplate", ItemsInit.LONSDALEITEARMOR, 1, EntityEquipmentSlot.CHEST);
		lonsdaleite_helmet = new ArmorBase("lonsdaleite_helmet", ItemsInit.LONSDALEITEARMOR, 1, EntityEquipmentSlot.HEAD);
		
		//Silver
		silver_ingot = new ItemBase("silver_ingot");
		silver_apple = new FoodEffectBase("silver_apple", 4, 6.9F, false, new PotionEffect(MobEffects.REGENERATION, (20*20), 0, false, true), new PotionEffect(MobEffects.RESISTANCE, (90*20), 0, false, true), new PotionEffect(MobEffects.ABSORPTION, (40*20), 1, false, true));
		silver_nugget = new ItemBase("silver_nugget");
		silver_stick = new ItemBase("silver_stick");
		silver_axe = new AxeBase("silver_axe", ItemsInit.SILVER, -3.0F, 8.0F);
		silver_pickaxe = new PickaxeBase("silver_pickaxe", ItemsInit.SILVER);
		silver_shovel = new ShovelBase("silver_shovel", ItemsInit.SILVER);
		silver_sword = new SwordBase("silver_sword", ItemsInit.SILVER);
		silver_hoe = new HoeBase("silver_hoe", ItemsInit.SILVER);
		silver_boots = new ArmorBase("silver_boots", ItemsInit.SILVERARMOR, 1, EntityEquipmentSlot.FEET);
		silver_leggings = new ArmorBase("silver_leggings", ItemsInit.SILVERARMOR, 2, EntityEquipmentSlot.LEGS);
		silver_chestplate = new ArmorBase("silver_chestplate", ItemsInit.SILVERARMOR, 1, EntityEquipmentSlot.CHEST);
		silver_helmet = new ArmorBase("silver_helmet", ItemsInit.SILVERARMOR, 1, EntityEquipmentSlot.HEAD);
		
		//Titane
		titane_ingot = new ItemBase("titane_ingot");
		titane_nugget = new ItemBase("titane_nugget");
		titane_axe = new AxeBase("titane_axe", ItemsInit.TITANE, -3.0F, 8.0F);
		titane_dagger = new CustomWeapon("titane_dagger", 4.5F, -2F, ItemsInit.TITANE);
		titane_double_axe = new DoubleAxeBase("titane_double_axe", 10.5F, -3.6F, ItemsInit.TITANE, Sets.newHashSet(Blocks.PLANKS, Blocks.BOOKSHELF, Blocks.LOG, Blocks.LOG2, Blocks.CHEST, Blocks.PUMPKIN, Blocks.LIT_PUMPKIN, Blocks.MELON_BLOCK, Blocks.LADDER, Blocks.WOODEN_BUTTON, Blocks.WOODEN_PRESSURE_PLATE));
		titane_hoe = new HoeBase("titane_hoe", ItemsInit.TITANE);
		titane_pickaxe = new PickaxeBase("titane_pickaxe", ItemsInit.TITANE);
		titane_shovel = new ShovelBase("titane_shovel", ItemsInit.TITANE);
		titane_sword = new SwordBase("titane_sword", ItemsInit.TITANE);
		titane_boots = new ArmorBase("titane_boots", ItemsInit.TITANEARMOR, 1, EntityEquipmentSlot.FEET);
		titane_leggings = new ArmorBase("titane_leggings", ItemsInit.TITANEARMOR, 2, EntityEquipmentSlot.LEGS);
		titane_chestplate = new ArmorBase("titane_chestplate", ItemsInit.TITANEARMOR, 1, EntityEquipmentSlot.CHEST);
		titane_helmet = new ArmorBase("titane_helmet", ItemsInit.TITANEARMOR, 1, EntityEquipmentSlot.HEAD);
		
		//CustomsSword
		titane_sword_with_copper_handle = new CustomWeapon("titane_sword_with_copper_handle", 6F, -2.2F, ItemsInit.TITANE, 4096);
		titane_sword_with_silver_handle = new CustomWeapon("titane_sword_with_silver_handle", 3F, -1F, ItemsInit.TITANE, 2048);
		titane_sword_with_gold_handle = new CustomWeapon("titane_sword_with_gold_handle", 8.5F, -3F, ItemsInit.TITANE, 2048);
		titane_sword_with_iron_handle = new CustomWeapon("titane_sword_with_iron_handle", 8.5F, -2.4F, ItemsInit.TITANE, 1024);

		//Drinks
		plantain_tea = new DrinkBase("plantain_tea", new PotionEffect(MobEffects.STRENGTH, 600, 1, true, true));
		mint_tea = new DrinkBase("mint_tea", new PotionEffect(MobEffects.SPEED, 1200, 2, true, true), new PotionEffect(MobEffects.JUMP_BOOST, 1200, 1, true, true));
		thyme_tea = new DrinkBase("thyme_tea", new PotionEffect(MobEffects.REGENERATION, 200, 2, true, true));
		nettle_tea = new DrinkBase("nettle_tea", new PotionEffect(MobEffects.ABSORPTION, 900, 0, true, true), new PotionEffect(MobEffects.REGENERATION, 100, 1, true, true));
		pulmonary_tea = new DrinkBase("pulmonary_tea", new PotionEffect(MobEffects.RESISTANCE, 600, 1, true, true));

		//Secret
	}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		//Items
		event.getRegistry().register(gold_stick);
		event.getRegistry().register(iron_stick);
		event.getRegistry().register(filter);
		event.getRegistry().register(mineria_xp_orb);
		event.getRegistry().register(cup);
		event.getRegistry().register(vanadium_ingot);
		event.getRegistry().register(vanadium_helmet);
		
		//Copper
		event.getRegistry().register(copper_ingot);
		event.getRegistry().register(copper_bow);
		event.getRegistry().register(copper_stick);
		event.getRegistry().register(copper_sword);
				
		//Lead
		event.getRegistry().register(lead_ingot);
		event.getRegistry().register(lead_sword);
		event.getRegistry().register(lead_nugget);
		event.getRegistry().register(compressed_lead_ingot);
		event.getRegistry().register(compressed_lead_sword);
				
		//Lonsdaleite
		event.getRegistry().register(lonsdaleite);
		event.getRegistry().register(lonsdaleite_axe);
		event.getRegistry().register(lonsdaleite_dagger);
		event.getRegistry().register(lonsdaleite_double_axe);
		event.getRegistry().register(lonsdaleite_pickaxe);
		event.getRegistry().register(lonsdaleite_shovel);
		event.getRegistry().register(lonsdaleite_sword);
		event.getRegistry().register(lonsdaleite_hoe);
		event.getRegistry().register(lonsdaleite_boots);
		event.getRegistry().register(lonsdaleite_leggings);
		event.getRegistry().register(lonsdaleite_chestplate);
		event.getRegistry().register(lonsdaleite_helmet);
		
		//Silver
		event.getRegistry().register(silver_ingot);
		event.getRegistry().register(silver_apple);
		event.getRegistry().register(silver_nugget);
		event.getRegistry().register(silver_stick);
		event.getRegistry().register(silver_axe);
		event.getRegistry().register(silver_pickaxe);
		event.getRegistry().register(silver_shovel);
		event.getRegistry().register(silver_sword);
		event.getRegistry().register(silver_hoe);
		event.getRegistry().register(silver_boots);
		event.getRegistry().register(silver_leggings);
		event.getRegistry().register(silver_chestplate);
		event.getRegistry().register(silver_helmet);
		
		//Titane
		event.getRegistry().register(titane_ingot);
		event.getRegistry().register(titane_nugget);
		event.getRegistry().register(titane_axe);
		event.getRegistry().register(titane_dagger);
		event.getRegistry().register(titane_double_axe);
		event.getRegistry().register(titane_hoe);
		event.getRegistry().register(titane_pickaxe);
		event.getRegistry().register(titane_shovel);
		event.getRegistry().register(titane_sword);
		event.getRegistry().register(titane_boots);
		event.getRegistry().register(titane_leggings);
		event.getRegistry().register(titane_chestplate);
		event.getRegistry().register(titane_helmet);
		
		//CustomsSword
		event.getRegistry().register(titane_sword_with_copper_handle);
		event.getRegistry().register(titane_sword_with_silver_handle);
		event.getRegistry().register(titane_sword_with_gold_handle);
		event.getRegistry().register(titane_sword_with_iron_handle);

		//Drinks
		event.getRegistry().register(plantain_tea);
		event.getRegistry().register(mint_tea);
		event.getRegistry().register(thyme_tea);
		event.getRegistry().register(nettle_tea);
		event.getRegistry().register(pulmonary_tea);

		//Secret
	}
	
	@SubscribeEvent
	public static void registerRenders(ModelRegistryEvent event)
	{
		//Items
		registerRender(gold_stick);
		registerRender(iron_stick);
		registerRender(filter);
		registerRender(mineria_xp_orb);
		registerRender(cup);
		registerRender(vanadium_ingot);
		registerRender(vanadium_helmet);
		
		//Copper
		registerRender(copper_ingot);
		registerRender(copper_bow);
		registerRender(copper_stick);
		registerRender(copper_sword);
		
		//Lead
		registerRender(lead_ingot);
		registerRender(lead_sword);
		registerRender(lead_nugget);
		registerRender(compressed_lead_ingot);
		registerRender(compressed_lead_sword);
		
		//Lonsdaleite
		registerRender(lonsdaleite);
		registerRender(lonsdaleite_axe);
		registerRender(lonsdaleite_dagger);
		registerRender(lonsdaleite_double_axe);
		registerRender(lonsdaleite_pickaxe);
		registerRender(lonsdaleite_shovel);
		registerRender(lonsdaleite_sword);
		registerRender(lonsdaleite_hoe);
		registerRender(lonsdaleite_boots);
		registerRender(lonsdaleite_leggings);
		registerRender(lonsdaleite_chestplate);
		registerRender(lonsdaleite_helmet);
		
		//Silver
		registerRender(silver_ingot);
		registerRender(silver_apple);
		registerRender(silver_nugget);
		registerRender(silver_stick);
		registerRender(silver_axe);
		registerRender(silver_pickaxe);
		registerRender(silver_shovel);
		registerRender(silver_sword);
		registerRender(silver_hoe);
		registerRender(silver_boots);
		registerRender(silver_leggings);
		registerRender(silver_chestplate);
		registerRender(silver_helmet);
		
		//Titane
		registerRender(titane_ingot);
		registerRender(titane_nugget);
		registerRender(titane_axe);
		registerRender(titane_dagger);
		registerRender(titane_double_axe);
		registerRender(titane_hoe);
		registerRender(titane_pickaxe);
		registerRender(titane_shovel);
		registerRender(titane_sword);
		registerRender(titane_boots);
		registerRender(titane_leggings);
		registerRender(titane_chestplate);
		registerRender(titane_helmet);
		
		//CustomsSword
		registerRender(titane_sword_with_copper_handle);
		registerRender(titane_sword_with_silver_handle);
		registerRender(titane_sword_with_gold_handle);
		registerRender(titane_sword_with_iron_handle);

		//Drinks
		registerRender(plantain_tea);
		registerRender(mint_tea);
		registerRender(thyme_tea);
		registerRender(nettle_tea);
		registerRender(pulmonary_tea);

		//Secret
	}
	
	private static void registerRender(Item item)
	{
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}
}
