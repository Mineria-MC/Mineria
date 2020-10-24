package com.mineria.mod.init;

import com.google.common.collect.Sets;
import com.mineria.mod.References;
import com.mineria.mod.items.*;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = References.MODID)
public class ItemsInit
{
	public static final List<Item> itemList = new ArrayList<>();

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
	public static final Item.ToolMaterial COPPER = EnumHelper.addToolMaterial("copper", 2, 187, 3.5F, 1.5F, 4);
	public static final Item.ToolMaterial LEAD = EnumHelper.addToolMaterial("lead", 2, 294, 6.25F, 2.2F, 12);
	public static final Item.ToolMaterial COMPRESSED_LEAD = EnumHelper.addToolMaterial("compressed_lead", 2, 576, 6.25F, 3.0F, 12);
	public static final Item.ToolMaterial LONSDALEITE = EnumHelper.addToolMaterial("lonsdaleite", 3, 3460, 20.0F, 7.0F, 8);
	public static final Item.ToolMaterial SILVER = EnumHelper.addToolMaterial("silver", 2, 621, 7.0F, 2.6F, 16);
	public static final Item.ToolMaterial TITANE = Objects.requireNonNull(EnumHelper.addToolMaterial("titane", 3, 2048, 12.0F, 4.0F, 12));

	//Armor Materials
	public static final ArmorMaterial LONSDALEITEARMOR = Objects.requireNonNull(EnumHelper.addArmorMaterial("lonsdaleite_armor", References.MODID + ":lonsdaleite", 62, new int[]{6, 9, 11, 7}, 8, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 4.0F));
	public static final ArmorMaterial SILVERARMOR = EnumHelper.addArmorMaterial("silver_armor", References.MODID + ":silver", 17, new int[]{2, 6, 6, 3}, 16, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 0.5F);
	public static final ArmorMaterial TITANEARMOR = EnumHelper.addArmorMaterial("titane_armor", References.MODID + ":titane", 45, new int[]{4, 7, 9, 4}, 12, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 1.0F);
	public static final ArmorMaterial VANADIUM = EnumHelper.addArmorMaterial("vanadium", References.MODID + ":vanadium", 37, new int[]{0, 0, 0, 4}, 10, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 0.0F);
	
	public static void init()
	{
		//Items
		gold_stick = register(new ItemBase("gold_stick"));
		iron_stick = register(new ItemBase("iron_stick"));
		filter = register(new ItemFilter("filter"));
		mineria_xp_orb = register(new XPOrbItem("mineria_xp_orb"));
		cup = register(new CupItem("cup"));
		water_bowl = (new WaterBowl("water_bowl"));
		vanadium_ingot = register(new ItemBase("vanadium_ingot"));
		vanadium_helmet = register(new VanadiumHelmet("vanadium_helmet", ItemsInit.VANADIUM, 1, EntityEquipmentSlot.HEAD));
		
		//Copper
		copper_ingot = register(new ItemBase("copper_ingot"));
		copper_bow = register(new BowBase("copper_bow"));
		copper_stick = register(new ItemBase("copper_stick"));
		copper_sword = register(new SwordBase("copper_sword", ItemsInit.COPPER));
		
		//Lead
		lead_ingot = register(new ItemBase("lead_ingot"));
		lead_sword = register(new SwordBase("lead_sword", ItemsInit.LEAD));
		lead_nugget = register(new ItemBase("lead_nugget"));
		compressed_lead_ingot = register(new ItemBase("compressed_lead_ingot"));
		compressed_lead_sword = register(new SwordBase("compressed_lead_sword", ItemsInit.COMPRESSED_LEAD));
		
		//Lonsdaleite
		lonsdaleite = register(new ItemBase("lonsdaleite"));
		lonsdaleite_axe = register(new AxeBase("lonsdaleite_axe", ItemsInit.LONSDALEITE, -3.0F, 8.0F));
		lonsdaleite_dagger = register(new CustomWeapon("lonsdaleite_dagger", 7.5F, -1.75F, ItemsInit.LONSDALEITE));
		lonsdaleite_double_axe = register(new DoubleAxeBase("lonsdaleite_double_axe", 13.5F, -3.4F, ItemsInit.LONSDALEITE, Sets.newHashSet(Blocks.PLANKS, Blocks.BOOKSHELF, Blocks.LOG, Blocks.LOG2, Blocks.CHEST, Blocks.PUMPKIN, Blocks.LIT_PUMPKIN, Blocks.MELON_BLOCK, Blocks.LADDER, Blocks.WOODEN_BUTTON, Blocks.WOODEN_PRESSURE_PLATE)));
		lonsdaleite_pickaxe = register(new PickaxeBase("lonsdaleite_pickaxe", ItemsInit.LONSDALEITE));
		lonsdaleite_shovel = register(new ShovelBase("lonsdaleite_shovel", ItemsInit.LONSDALEITE));
		lonsdaleite_sword = register(new SwordBase("lonsdaleite_sword", ItemsInit.LONSDALEITE));
		lonsdaleite_hoe = register(new HoeBase("lonsdaleite_hoe", ItemsInit.LONSDALEITE));
		lonsdaleite_boots = register(new ArmorBase("lonsdaleite_boots", ItemsInit.LONSDALEITEARMOR, 1, EntityEquipmentSlot.FEET));
		lonsdaleite_leggings = register(new ArmorBase("lonsdaleite_leggings", ItemsInit.LONSDALEITEARMOR, 2, EntityEquipmentSlot.LEGS));
		lonsdaleite_chestplate = register(new ArmorBase("lonsdaleite_chestplate", ItemsInit.LONSDALEITEARMOR, 1, EntityEquipmentSlot.CHEST));
		lonsdaleite_helmet = register(new ArmorBase("lonsdaleite_helmet", ItemsInit.LONSDALEITEARMOR, 1, EntityEquipmentSlot.HEAD));
		
		//Silver
		silver_ingot = register(new ItemBase("silver_ingot"));
		silver_apple = register(new FoodEffectBase("silver_apple", 4, 6.9F, false, new PotionEffect(MobEffects.REGENERATION, (20*20), 0, false, true), new PotionEffect(MobEffects.RESISTANCE, (90*20), 0, false, true), new PotionEffect(MobEffects.ABSORPTION, (40*20), 1, false, true)));
		silver_nugget = register(new ItemBase("silver_nugget"));
		silver_stick = register(new ItemBase("silver_stick"));
		silver_axe = register(new AxeBase("silver_axe", ItemsInit.SILVER, -3.0F, 8.0F));
		silver_pickaxe = register(new PickaxeBase("silver_pickaxe", ItemsInit.SILVER));
		silver_shovel = register(new ShovelBase("silver_shovel", ItemsInit.SILVER));
		silver_sword = register(new SwordBase("silver_sword", ItemsInit.SILVER));
		silver_hoe = register(new HoeBase("silver_hoe", ItemsInit.SILVER));
		silver_boots = register(new ArmorBase("silver_boots", ItemsInit.SILVERARMOR, 1, EntityEquipmentSlot.FEET));
		silver_leggings = register(new ArmorBase("silver_leggings", ItemsInit.SILVERARMOR, 2, EntityEquipmentSlot.LEGS));
		silver_chestplate = register(new ArmorBase("silver_chestplate", ItemsInit.SILVERARMOR, 1, EntityEquipmentSlot.CHEST));
		silver_helmet = register(new ArmorBase("silver_helmet", ItemsInit.SILVERARMOR, 1, EntityEquipmentSlot.HEAD));
		
		//Titane
		titane_ingot = register(new ItemBase("titane_ingot"));
		titane_nugget = register(new ItemBase("titane_nugget"));
		titane_axe = register(new AxeBase("titane_axe", ItemsInit.TITANE, -3.0F, 8.0F));
		titane_dagger = register(new CustomWeapon("titane_dagger", 4.5F, -2F, ItemsInit.TITANE));
		titane_double_axe = register(new DoubleAxeBase("titane_double_axe", 10.5F, -3.6F, ItemsInit.TITANE, Sets.newHashSet(Blocks.PLANKS, Blocks.BOOKSHELF, Blocks.LOG, Blocks.LOG2, Blocks.CHEST, Blocks.PUMPKIN, Blocks.LIT_PUMPKIN, Blocks.MELON_BLOCK, Blocks.LADDER, Blocks.WOODEN_BUTTON, Blocks.WOODEN_PRESSURE_PLATE)));
		titane_hoe = register(new HoeBase("titane_hoe", ItemsInit.TITANE));
		titane_pickaxe = register(new PickaxeBase("titane_pickaxe", ItemsInit.TITANE));
		titane_shovel = register(new ShovelBase("titane_shovel", ItemsInit.TITANE));
		titane_sword = register(new SwordBase("titane_sword", ItemsInit.TITANE));
		titane_boots = register(new ArmorBase("titane_boots", ItemsInit.TITANEARMOR, 1, EntityEquipmentSlot.FEET));
		titane_leggings = register(new ArmorBase("titane_leggings", ItemsInit.TITANEARMOR, 2, EntityEquipmentSlot.LEGS));
		titane_chestplate = register(new ArmorBase("titane_chestplate", ItemsInit.TITANEARMOR, 1, EntityEquipmentSlot.CHEST));
		titane_helmet = register(new ArmorBase("titane_helmet", ItemsInit.TITANEARMOR, 1, EntityEquipmentSlot.HEAD));
		
		//CustomsSword
		titane_sword_with_copper_handle = register(new CustomWeapon("titane_sword_with_copper_handle", 6F, -2.2F, ItemsInit.TITANE, 4096));
		titane_sword_with_silver_handle = register(new CustomWeapon("titane_sword_with_silver_handle", 3F, -1F, ItemsInit.TITANE, 2048));
		titane_sword_with_gold_handle = register(new CustomWeapon("titane_sword_with_gold_handle", 8.5F, -3F, ItemsInit.TITANE, 2048));
		titane_sword_with_iron_handle = register(new CustomWeapon("titane_sword_with_iron_handle", 8.5F, -2.4F, ItemsInit.TITANE, 1024));

		//Drinks
		plantain_tea = register(new DrinkBase("plantain_tea", new PotionEffect(MobEffects.STRENGTH, 600, 1, true, true)));
		mint_tea = register(new DrinkBase("mint_tea", new PotionEffect(MobEffects.SPEED, 1200, 2, true, true), new PotionEffect(MobEffects.JUMP_BOOST, 1200, 1, true, true)));
		thyme_tea = register(new DrinkBase("thyme_tea", new PotionEffect(MobEffects.REGENERATION, 200, 2, true, true)));
		nettle_tea = register(new DrinkBase("nettle_tea", new PotionEffect(MobEffects.ABSORPTION, 900, 0, true, true), new PotionEffect(MobEffects.REGENERATION, 100, 1, true, true)));
		pulmonary_tea = register(new DrinkBase("pulmonary_tea", new PotionEffect(MobEffects.RESISTANCE, 600, 1, true, true)));

		//Secret
	}

	private static Item register(Item instance)
	{
		itemList.add(instance);
		return instance;
	}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		itemList.forEach(item -> event.getRegistry().register(item));
	}
	
	@SubscribeEvent
	public static void registerRenders(ModelRegistryEvent event)
	{
		itemList.forEach(ItemsInit::registerRender);
	}
	
	private static void registerRender(Item item)
	{
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(Objects.requireNonNull(item.getRegistryName()), "inventory"));
	}
}
