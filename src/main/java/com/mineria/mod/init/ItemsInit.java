package com.mineria.mod.init;

import com.google.common.collect.Maps;
import com.mineria.mod.References;
import com.mineria.mod.items.*;
import com.mineria.mod.util.MineriaUtils;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.mineria.mod.init.BlocksInit.ITEMBLOCKS;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = References.MODID)
public final class ItemsInit
{
	private static final List<Item> ITEMS = new ArrayList<>();
	private static final List<Item> UNREGISTERED = new ArrayList<>();
	
	//Tool Materials
	public static final Item.ToolMaterial COPPER_MATERIAL = EnumHelper.addToolMaterial("copper", 2, 187, 3.5F, 1.5F, 4);
	public static final Item.ToolMaterial LEAD_MATERIAL = EnumHelper.addToolMaterial("lead", 2, 294, 6.25F, 2.2F, 12);
	public static final Item.ToolMaterial COMPRESSED_LEAD_MATERIAL = EnumHelper.addToolMaterial("compressed_lead", 2, 576, 6.25F, 3.0F, 12);
	public static final Item.ToolMaterial LONSDALEITE_MATERIAL = EnumHelper.addToolMaterial("lonsdaleite", 3, 3460, 20.0F, 7.0F, 8);
	public static final Item.ToolMaterial SILVER_MATERIAL = EnumHelper.addToolMaterial("silver", 2, 621, 7.0F, 2.6F, 16);
	public static final Item.ToolMaterial TITANE_MATERIAL = EnumHelper.addToolMaterial("titane", 3, 2048, 12.0F, 4.0F, 12);

	//Armor Materials
	public static final ArmorMaterial LONSDALEITE_ARMOR = EnumHelper.addArmorMaterial("lonsdaleite", References.MODID + ":lonsdaleite", 62, new int[]{6, 9, 11, 7}, 8, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 4.0F);
	public static final ArmorMaterial SILVER_ARMOR = EnumHelper.addArmorMaterial("silver", References.MODID + ":silver", 17, new int[]{2, 6, 6, 3}, 16, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 0.5F);
	public static final ArmorMaterial TITANE_ARMOR = EnumHelper.addArmorMaterial("titane", References.MODID + ":titane", 45, new int[]{4, 7, 9, 4}, 12, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 1.0F);
	public static final ArmorMaterial VANADIUM_ARMOR = EnumHelper.addArmorMaterial("vanadium", References.MODID + ":vanadium",37, new int[]{0, 0, 0, 4}, 10, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 0.0F);

	//Random
	public static final Item GOLD_STICK = register("gold_stick", new ItemMineria());
	public static final Item IRON_STICK = register("iron_stick", new ItemMineria());
	public static final Item FILTER = register("filter", new ItemMineria(new ItemMineria.Builder().setMaxStackSize(4)));
	public static final Item XP_ORB = register("xp_orb", new ItemXPOrb(1));
	public static final Item COMPRESSED_XP_ORB = register("compressed_xp_orb", new ItemXPOrb(4));
	public static final Item SUPER_COMPRESSED_XP_ORB = register("super_compressed_xp_orb", new ItemXPOrb(16));
	public static final Item SUPER_DUPER_COMPRESSED_XP_ORB = register("super_duper_compressed_xp_orb", new ItemXPOrb(64));
	public static final Item CUP = register("cup", new ItemMineria(new ItemMineria.Builder().setMaxStackSize(16)));
	public static final Item VANADIUM_INGOT = register("vanadium_ingot", new ItemMineria());
	public static final Item VANADIUM_HELMET = register("vanadium_helmet", new ArmorBuilder(ItemsInit.VANADIUM_ARMOR, EntityEquipmentSlot.HEAD).onArmorTick((world, player, stack) -> player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, (12 * 20), 0, false, false))).build());

	//Copper
	public static final Item COPPER_INGOT = register("copper_ingot", new ItemMineria());
	public static final Item COPPER_BOW = register("copper_bow", new ItemCustomBow());
	public static final Item COPPER_STICK = register("copper_stick", new ItemMineria());
	public static final Item COPPER_SWORD = register("copper_sword", new ItemMineria.Sword(ItemsInit.COPPER_MATERIAL));

	//Lead
	public static final Item LEAD_INGOT = register("lead_ingot", new ItemMineria());
	public static final Item LEAD_SWORD = register("lead_sword", new ItemMineria.Sword(ItemsInit.LEAD_MATERIAL));
	public static final Item LEAD_NUGGET = register("lead_nugget", new ItemMineria());
	public static final Item COMPRESSED_LEAD_INGOT = register("compressed_lead_ingot", new ItemMineria());
	public static final Item COMPRESSED_LEAD_SWORD = register("compressed_lead_sword", new ItemMineria.Sword(ItemsInit.COMPRESSED_LEAD_MATERIAL));

	//Lonsdaleite
	public static final Item LONSDALEITE = register("lonsdaleite", new ItemMineria(new ItemMineria.Builder().addEffect()));
	public static final Item LONSDALEITE_AXE = register("lonsdaleite_axe", new ItemMineria.Axe(ItemsInit.LONSDALEITE_MATERIAL, 8.0F));
	public static final Item LONSDALEITE_DAGGER = register("lonsdaleite_dagger", new ItemCustomWeapon(7.5F, -1.75F, ItemsInit.LONSDALEITE_MATERIAL));
	public static final Item LONSDALEITE_DOUBLE_AXE = register("lonsdaleite_double_axe", new ItemDoubleAxe(13.5F, -3.4F, ItemsInit.LONSDALEITE_MATERIAL));
	public static final Item LONSDALEITE_PICKAXE = register("lonsdaleite_pickaxe", new ItemMineria.Pickaxe(ItemsInit.LONSDALEITE_MATERIAL));
	public static final Item LONSDALEITE_SHOVEL = register("lonsdaleite_shovel", new ItemMineria.Shovel(ItemsInit.LONSDALEITE_MATERIAL));
	public static final Item LONSDALEITE_SWORD = register("lonsdaleite_sword", new ItemMineria.Sword(ItemsInit.LONSDALEITE_MATERIAL));
	public static final Item LONSDALEITE_HOE = register("lonsdaleite_hoe", new ItemMineria.Hoe(ItemsInit.LONSDALEITE_MATERIAL));
	public static final Item LONSDALEITE_BOOTS = register("lonsdaleite_boots", new ArmorBuilder(ItemsInit.LONSDALEITE_ARMOR, EntityEquipmentSlot.FEET).build());
	public static final Item LONSDALEITE_LEGGINGS = register("lonsdaleite_leggings", new ArmorBuilder(ItemsInit.LONSDALEITE_ARMOR, EntityEquipmentSlot.LEGS).build());
	public static final Item LONSDALEITE_CHESTPLATE = register("lonsdaleite_chestplate", new ArmorBuilder(ItemsInit.LONSDALEITE_ARMOR, EntityEquipmentSlot.CHEST).build());
	public static final Item LONSDALEITE_HELMET = register("lonsdaleite_helmet", new ArmorBuilder(ItemsInit.LONSDALEITE_ARMOR, EntityEquipmentSlot.HEAD).build());

	//Silver
	public static final Item SILVER_INGOT = register("silver_ingot", new ItemMineria());
	public static final Item SILVER_APPLE = register("silver_apple", new ItemCustomFood(4, 6.9F, false, true, 16, (stack, player) -> {
		player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, (20 * 20), 0, false, true));
		player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, (90 * 20), 0, false, true));
		player.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, (40*20), 1, false, true));
	}));
	public static final Item SILVER_NUGGET = register("silver_nugget", new ItemMineria());
	public static final Item SILVER_STICK = register("silver_stick", new ItemMineria());
	public static final Item SILVER_AXE = register("silver_axe", new ItemMineria.Axe(ItemsInit.SILVER_MATERIAL, 8.0F));
	public static final Item SILVER_PICKAXE = register("silver_pickaxe", new ItemMineria.Pickaxe(ItemsInit.SILVER_MATERIAL));
	public static final Item SILVER_SHOVEL = register("silver_shovel", new ItemMineria.Shovel(ItemsInit.SILVER_MATERIAL));
	public static final Item SILVER_SWORD = register("silver_sword", new ItemMineria.Sword(ItemsInit.SILVER_MATERIAL));
	public static final Item SILVER_HOE = register("silver_hoe", new ItemMineria.Hoe(ItemsInit.SILVER_MATERIAL));
	public static final Item SILVER_BOOTS = register("silver_boots", new ArmorBuilder(ItemsInit.SILVER_ARMOR, EntityEquipmentSlot.FEET).build());
	public static final Item SILVER_LEGGINGS = register("silver_leggings", new ArmorBuilder(ItemsInit.SILVER_ARMOR, EntityEquipmentSlot.LEGS).build());
	public static final Item SILVER_CHESTPLATE = register("silver_chestplate", new ArmorBuilder(ItemsInit.SILVER_ARMOR, EntityEquipmentSlot.CHEST).build());
	public static final Item SILVER_HELMET = register("silver_helmet", new ArmorBuilder(ItemsInit.SILVER_ARMOR, EntityEquipmentSlot.HEAD).build());

	//Titane
	public static final Item TITANE_INGOT = register("titane_ingot", new ItemMineria());
	public static final Item TITANE_NUGGET = register("titane_nugget", new ItemMineria());
	public static final Item TITANE_AXE = register("titane_axe", new ItemMineria.Axe(ItemsInit.TITANE_MATERIAL, 8.0F));
	public static final Item TITANE_DAGGER = register("titane_dagger", new ItemCustomWeapon(4.5F, -2F, ItemsInit.TITANE_MATERIAL));
	public static final Item TITANE_DOUBLE_AXE = register("titane_double_axe", new ItemDoubleAxe(10.5F, -3.6F, ItemsInit.TITANE_MATERIAL));
	public static final Item TITANE_HOE = register("titane_hoe", new ItemMineria.Hoe(ItemsInit.TITANE_MATERIAL));
	public static final Item TITANE_PICKAXE = register("titane_pickaxe", new ItemMineria.Pickaxe(ItemsInit.TITANE_MATERIAL));
	public static final Item TITANE_SHOVEL = register("titane_shovel", new ItemMineria.Shovel(ItemsInit.TITANE_MATERIAL));
	public static final Item TITANE_SWORD = register("titane_sword", new ItemMineria.Sword(ItemsInit.TITANE_MATERIAL));
	public static final Item TITANE_BOOTS = register("titane_boots", new ArmorBuilder(ItemsInit.TITANE_ARMOR, EntityEquipmentSlot.FEET).build());
	public static final Item TITANE_LEGGINGS = register("titane_leggings", new ArmorBuilder(ItemsInit.TITANE_ARMOR, EntityEquipmentSlot.LEGS).build());
	public static final Item TITANE_CHESTPLATE = register("titane_chestplate", new ArmorBuilder(ItemsInit.TITANE_ARMOR, EntityEquipmentSlot.CHEST).build());
	public static final Item TITANE_HELMET = register("titane_helmet", new ArmorBuilder(ItemsInit.TITANE_ARMOR, EntityEquipmentSlot.HEAD).build());

	//CustomsSword
	public static final Item TITANE_SWORD_WITH_COPPER_HANDLE = register("titane_sword_with_copper_handle", new ItemCustomWeapon(6F, -2.2F, ItemsInit.TITANE_MATERIAL, 4096));
	public static final Item TITANE_SWORD_WITH_SILVER_HANDLE = register("titane_sword_with_silver_handle", new ItemCustomWeapon(3F, -1F, ItemsInit.TITANE_MATERIAL, 2048));
	public static final Item TITANE_SWORD_WITH_GOLD_HANDLE = register("titane_sword_with_gold_handle", new ItemCustomWeapon(8.5F, -3F, ItemsInit.TITANE_MATERIAL, 2048));
	public static final Item TITANE_SWORD_WITH_IRON_HANDLE = register("titane_sword_with_iron_handle", new ItemCustomWeapon(8.5F, -2.4F, ItemsInit.TITANE_MATERIAL, 1024));

	//Drinks
	public static final Item PLANTAIN_TEA = register("plantain_tea", new ItemDrink((stack, player) ->
		player.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 600, 1, true, true))
	));
	public static final Item MINT_TEA = register("mint_tea", new ItemDrink((stack, player) -> {
		player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 1200, 2, true, true));
		player.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 1200, 1, true, true));
	}));
	public static final Item THYME_TEA = register("thyme_tea", new ItemDrink((stack, player) ->
		player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 200, 2, true, true))
	));
	public static final Item NETTLE_TEA = register("nettle_tea", new ItemDrink((stack, player) -> {
		player.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 900, 0, true, true));
		player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 100, 1, true, true));
	}));
	public static final Item PULMONARY_TEA = register("pulmonary_tea", new ItemDrink((stack, player) ->
		player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 600, 1, true, true))
	));

	private static Item register(String name, Item instance)
	{
		UNREGISTERED.add(instance.setRegistryName(name).setUnlocalizedName(name));
		return instance;
	}

	public static void init()
	{
		ITEMS.addAll(UNREGISTERED);
	}

	public static Map<Item.ToolMaterial, Item> getToolRepairItems()
	{
		return MineriaUtils.make(Maps.newHashMap(), map -> {
			map.put(ItemsInit.COPPER_MATERIAL, ItemsInit.COPPER_INGOT);
			map.put(ItemsInit.LEAD_MATERIAL, ItemsInit.LEAD_INGOT);
			map.put(ItemsInit.COMPRESSED_LEAD_MATERIAL, ItemsInit.COMPRESSED_LEAD_INGOT);
			map.put(ItemsInit.LONSDALEITE_MATERIAL, ItemsInit.LONSDALEITE);
			map.put(ItemsInit.SILVER_MATERIAL, ItemsInit.SILVER_INGOT);
			map.put(ItemsInit.TITANE_MATERIAL, ItemsInit.TITANE_INGOT);
		});
	}

	public static Map<ArmorMaterial, Item> getArmorRepairItems()
	{
		return MineriaUtils.make(Maps.newHashMap(), map -> {
			map.put(ItemsInit.LONSDALEITE_ARMOR, ItemsInit.LONSDALEITE);
			map.put(ItemsInit.SILVER_ARMOR, ItemsInit.SILVER_INGOT);
			map.put(ItemsInit.TITANE_ARMOR, ItemsInit.TITANE_INGOT);
			map.put(ItemsInit.VANADIUM_ARMOR, ItemsInit.VANADIUM_INGOT);
		});
	}

	public static Item getItemFromName(String name)
	{
		return UNREGISTERED.stream().filter(item -> item.getRegistryName() != null && item.getRegistryName().getResourcePath().equals(name))
				.findFirst().orElse(Items.AIR);
	}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		ITEMS.forEach(event.getRegistry()::register);
		ITEMBLOCKS.forEach(event.getRegistry()::register);
	}
	
	@SubscribeEvent
	public static void registerRenders(ModelRegistryEvent event)
	{
		ITEMS.forEach(ItemsInit::registerRender);
		ITEMBLOCKS.forEach(ItemsInit::registerRender);
	}
	
	private static void registerRender(Item item)
	{
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}
}
