package com.mineria.mod.common.init;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.effects.CustomEffectInstance;
import com.mineria.mod.common.effects.PoisonSource;
import com.mineria.mod.common.effects.instances.BowelSoundEffectInstance;
import com.mineria.mod.common.effects.instances.PoisonEffectInstance;
import com.mineria.mod.common.items.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@SuppressWarnings("unused")
public class MineriaItems
{
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Mineria.MODID);

//	public static final Item SPECIAL_ITEM = register("special_item", new SpecialItem());
	public static final Item APOTHECARIUM = register("apothecarium", new ApothecariumItem());

	// Ingredients
	public static final Item GOLD_STICK = register("gold_stick", new MineriaItem());
	public static final Item IRON_STICK = register("iron_stick", new MineriaItem());
	public static final Item FILTER = register("filter", new Item(defaultGroup().stacksTo(32)));
	public static final Item CUP = register("cup", new Item(defaultGroup().stacksTo(16)));
	public static final Item CHARCOAL_DUST = register("charcoal_dust", new Item(apothecaryGroup()));
	public static final Item CINNAMON_DUST = register("cinnamon_dust", new Item(apothecaryGroup()));
	public static final Item GUM_ARABIC_JAR = register("gum_arabic_jar", new Item(apothecaryGroup()));
	public static final Item ORANGE_BLOSSOM = register("orange-blossom", new Item(apothecaryGroup()));
	public static final Item DISTILLED_ORANGE_BLOSSOM_WATER = register("distilled_orange-blossom_water", new DrinkItem(apothecaryGroup().food(new FoodProperties.Builder().saturationMod(0.1F).nutrition(2).alwaysEat().build()), new DrinkItem.Properties().container(Items.GLASS_BOTTLE)));
	public static final Item SYRUP = register("syrup", new DrinkItem(apothecaryGroup().food(new FoodProperties.Builder().saturationMod(0.1F).nutrition(4).alwaysEat().build()), new DrinkItem.Properties().container(Items.GLASS_BOTTLE)));
	public static final Item MANDRAKE_ROOT = register("mandrake_root", new Item(apothecaryGroup()));
	public static final Item PULSATILLA_CHINENSIS_ROOT = register("pulsatilla_chinensis_root", new Item(apothecaryGroup()));
	public static final Item SAUSSUREA_COSTUS_ROOT = register("saussurea_costus_root", new Item(apothecaryGroup()));
	public static final Item GINGER = register("ginger", new Item(apothecaryGroup()));
	public static final Item DRUID_HEART = register("druid_heart", new Item(apothecaryGroup().rarity(Rarity.UNCOMMON).food(new FoodProperties.Builder().alwaysEat()
			.effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 255), 1)
			.effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, 255), 1).build())));
	public static final Item MISTLETOE = registerCompostable("mistletoe", new Item(apothecaryGroup()), 0.8F);

	// Fruits
	public static final Item BLACK_ELDERBERRY = registerCompostable("black_elderberry", new Item(apothecaryGroup().food(new FoodProperties.Builder().nutrition(4).build())), 0.65F);
	public static final Item ELDERBERRY = registerCompostable("elderberry", new SpecialFoodItem(apothecaryGroup().food(new FoodProperties.Builder().nutrition(0).saturationMod(0).alwaysEat().build()), (stack, world, living) -> PoisonSource.ELDERBERRY.poison(living)), 0.65F);
	public static final Item GOJI = registerCompostable("goji", new Item(apothecaryGroup().food(new FoodProperties.Builder().nutrition(2).effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 400, 0), 1).build())), 0.8F);
	public static final Item FIVE_FLAVOR_FRUIT = registerCompostable("five_flavor_fruit", new SpecialFoodItem(apothecaryGroup().food(new FoodProperties.Builder().nutrition(1).build()), (stack, world, living) -> {
		if(living.hasEffect(MineriaEffects.BOWEL_SOUNDS.get()))
		{
			BowelSoundEffectInstance instance = (BowelSoundEffectInstance) living.getEffect(MineriaEffects.BOWEL_SOUNDS.get());

			instance.decreaseVolume(1F, 0.5F);
			instance.increaseDelay(50, 300);
			living.addEffect(instance);
		}

		MobEffectInstance nausea = living.getEffect(MobEffects.CONFUSION);
		if(nausea != null && nausea.getAmplifier() == 0)
		{
			living.removeEffect(MobEffects.CONFUSION);
		}
	}), 0.7F);
	public static final Item YEW_BERRIES = registerCompostable("yew_berries", new SpecialFoodItem(apothecaryGroup().food(new FoodProperties.Builder().fast().build()), (stack, world, living) -> PoisonSource.YEW.poison(living)), 0.65F);

	// Weapons & Tools
	public static final Item TITANE_SWORD_WITH_COPPER_HANDLE = register("titane_sword_with_copper_handle", new CustomWeaponItem(MineriaItem.ItemTier.TITANE, 6F, -2.2F, defaultGroup().durability(4096)));
	public static final Item TITANE_SWORD_WITH_SILVER_HANDLE = register("titane_sword_with_silver_handle", new CustomWeaponItem(MineriaItem.ItemTier.TITANE, 3F, -1F, defaultGroup().durability(2048)));
	public static final Item TITANE_SWORD_WITH_GOLD_HANDLE = register("titane_sword_with_gold_handle", new CustomWeaponItem(MineriaItem.ItemTier.TITANE, 8.5F, -3F, 10, defaultGroup().durability(2048)));
	public static final Item TITANE_SWORD_WITH_IRON_HANDLE = register("titane_sword_with_iron_handle", new CustomWeaponItem(MineriaItem.ItemTier.TITANE, 8.5F, -2.4F, defaultGroup().durability(1024)));
	public static final Item BILLHOOK = register("billhook", new BillhookItem(defaultGroup().durability(240)));
	public static final Item BAMBOO_BLOWGUN = register("bamboo_blowgun", new BlowgunItem());
	public static final Item BLOWGUN_REFILL = register("blowgun_refill", new BlowgunRefillItem());
	public static final Item KUNAI = register("kunai", new KunaiItem());
	public static final Item SCALPEL = register("scalpel", new ScalpelItem());

	// Misc
	public static final Item XP_ORB = register("xp_orb", new XPOrbItem(1, defaultGroup()));
	public static final Item COMPRESSED_XP_ORB = register("compressed_xp_orb", new XPOrbItem(4, defaultGroup().rarity(Rarity.UNCOMMON)));
	public static final Item SUPER_COMPRESSED_XP_ORB = register("super_compressed_xp_orb", new XPOrbItem(16, defaultGroup().rarity(Rarity.RARE)));
	public static final Item SUPER_DUPER_COMPRESSED_XP_ORB = register("super_duper_compressed_xp_orb", new XPOrbItem(64, defaultGroup().rarity(Rarity.EPIC)));
	public static final Item VANADIUM_INGOT = register("vanadium_ingot", new MineriaItem());
	public static final Item VANADIUM_HELMET = register("vanadium_helmet", new ArmorBuilder(MineriaItem.ArmorMaterial.VANADIUM, EquipmentSlot.HEAD).onArmorTick((stack, world, player) -> player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, (12 * 20), 0, false, false))).build());
	public static final Item MINERIA_POTION = register("mineria_potion", new MineriaPotionItem(apothecaryGroup().stacksTo(1)));
	public static final Item MINERIA_SPLASH_POTION = register("mineria_splash_potion", new MineriaThrowablePotionItem(apothecaryGroup().stacksTo(1), false));
	public static final Item MINERIA_LINGERING_POTION = register("mineria_lingering_potion", new MineriaThrowablePotionItem(apothecaryGroup().stacksTo(1), true));
	public static final Item MYSTERY_DISC = register("mystery_disc", new MysteryDiscItem());
	public static final Item MUSIC_DISC_PIPPIN_THE_HUNCHBACK = register("music_disc_pippin_the_hunchback", new RecordItem(0, MineriaSounds.MUSIC_PIPPIN_THE_HUNCHBACK, defaultGroup().stacksTo(1).rarity(Rarity.RARE)));
	public static final Item JAR = register("jar", new JarItem());
	public static final Item MAGIC_POTION = register("magic_potion", new MagicPotionItem());
	public static final Item WIZARD_HAT = register("wizard_hat", new WizardHatItem());
	
	// Copper
	public static final Item COPPER_INGOT = register("copper_ingot", new MineriaItem());
	public static final Item COPPER_BOW = register("copper_bow", new MineriaBow(4.0F, 1.15F, 1, defaultGroup().durability(528)));
	public static final Item COPPER_STICK = register("copper_stick", new MineriaItem());
	public static final Item COPPER_SWORD = register("copper_sword", new SwordItem(MineriaItem.ItemTier.COPPER, 3, -2.4F, defaultGroup()));
	
	// Lead
	public static final Item LEAD_INGOT = register("lead_ingot", new MineriaItem());
	public static final Item LEAD_SWORD = register("lead_sword", new SwordItem(MineriaItem.ItemTier.LEAD, 3, -2.4F, defaultGroup()));
	public static final Item LEAD_NUGGET = register("lead_nugget", new MineriaItem());
	public static final Item COMPRESSED_LEAD_INGOT = register("compressed_lead_ingot", new MineriaItem());
	public static final Item COMPRESSED_LEAD_SWORD = register("compressed_lead_sword", new SwordItem(MineriaItem.ItemTier.COMPRESSED_LEAD, 3, -2.4F, defaultGroup()));
	
	// Lonsdaleite
	public static final Item LONSDALEITE = register("lonsdaleite", new MineriaItem());
	public static final Item LONSDALEITE_AXE = register("lonsdaleite_axe", new AxeItem(MineriaItem.ItemTier.LONSDALEITE, -2.0F, -3.0F, defaultGroup()));
	public static final Item LONSDALEITE_DAGGER = register("lonsdaleite_dagger", new CustomWeaponItem(MineriaItem.ItemTier.LONSDALEITE, 7.5F, -1.75F, 15, defaultGroup()));
	public static final Item LONSDALEITE_DOUBLE_AXE = register("lonsdaleite_double_axe", new AxeItem(MineriaItem.ItemTier.LONSDALEITE, 13.5F - MineriaItem.ItemTier.LONSDALEITE.getAttackDamageBonus(), -3.4F, defaultGroup()));
	public static final Item LONSDALEITE_PICKAXE = register("lonsdaleite_pickaxe", new PickaxeItem(MineriaItem.ItemTier.LONSDALEITE, 1, -2.8F, defaultGroup()));
	public static final Item LONSDALEITE_SHOVEL = register("lonsdaleite_shovel", new ShovelItem(MineriaItem.ItemTier.LONSDALEITE, 1.5F, -3.0F, defaultGroup()));
	public static final Item LONSDALEITE_SWORD = register("lonsdaleite_sword", new SwordItem(MineriaItem.ItemTier.LONSDALEITE, 3, -2.4F, defaultGroup()));
	public static final Item LONSDALEITE_HOE = register("lonsdaleite_hoe", new HoeItem(MineriaItem.ItemTier.LONSDALEITE, -7, 0F, defaultGroup()));
	public static final Item LONSDALEITE_BOOTS = register("lonsdaleite_boots", new ArmorBuilder(MineriaItem.ArmorMaterial.LONSDALEITE, EquipmentSlot.FEET).build());
	public static final Item LONSDALEITE_LEGGINGS = register("lonsdaleite_leggings", new ArmorBuilder(MineriaItem.ArmorMaterial.LONSDALEITE, EquipmentSlot.LEGS).build());
	public static final Item LONSDALEITE_CHESTPLATE = register("lonsdaleite_chestplate", new ArmorBuilder(MineriaItem.ArmorMaterial.LONSDALEITE, EquipmentSlot.CHEST).build());
	public static final Item LONSDALEITE_HELMET = register("lonsdaleite_helmet", new ArmorBuilder(MineriaItem.ArmorMaterial.LONSDALEITE, EquipmentSlot.HEAD).build());
	
	// Silver
	public static final Item SILVER_INGOT = register("silver_ingot", new MineriaItem());
	public static final Item SILVER_APPLE = register("silver_apple", new Item(defaultGroup().food(new FoodProperties.Builder().nutrition(4).saturationMod(6.9F).alwaysEat().fast().effect(
			() -> new MobEffectInstance(MobEffects.REGENERATION, (20 * 20), 0, false, true), 1.0F
	).effect(
			() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, (90 * 20), 0, false, true), 1.0F
	).effect(
			() -> new MobEffectInstance(MobEffects.ABSORPTION, (40*20), 1, false, true), 1.0F
	).build()).stacksTo(64)));
	public static final Item SILVER_NUGGET = register("silver_nugget", new MineriaItem());
	public static final Item SILVER_STICK = register("silver_stick", new MineriaItem());
	public static final Item SILVER_AXE = register("silver_axe", new AxeItem(MineriaItem.ItemTier.SILVER, 2.0F, -3.0F, defaultGroup()));
	public static final Item SILVER_PICKAXE = register("silver_pickaxe", new PickaxeItem(MineriaItem.ItemTier.SILVER, 1, -2.8F, defaultGroup()));
	public static final Item SILVER_SHOVEL = register("silver_shovel", new ShovelItem(MineriaItem.ItemTier.SILVER, 1.5F, -3.0F, defaultGroup()));
	public static final Item SILVER_SWORD = register("silver_sword", new SwordItem(MineriaItem.ItemTier.SILVER, 3, -2.4F, defaultGroup()));
	public static final Item SILVER_HOE = register("silver_hoe", new HoeItem(MineriaItem.ItemTier.SILVER, (int)-2.6F, 0F, defaultGroup()));
	public static final Item SILVER_BOOTS = register("silver_boots", new ArmorBuilder(MineriaItem.ArmorMaterial.SILVER, EquipmentSlot.FEET).build());
	public static final Item SILVER_LEGGINGS = register("silver_leggings", new ArmorBuilder(MineriaItem.ArmorMaterial.SILVER, EquipmentSlot.LEGS).build());
	public static final Item SILVER_CHESTPLATE = register("silver_chestplate", new ArmorBuilder(MineriaItem.ArmorMaterial.SILVER, EquipmentSlot.CHEST).build());
	public static final Item SILVER_HELMET = register("silver_helmet", new ArmorBuilder(MineriaItem.ArmorMaterial.SILVER, EquipmentSlot.HEAD).build());
	
	// Titane
	public static final Item TITANE_INGOT = register("titane_ingot", new MineriaItem());
	public static final Item TITANE_NUGGET = register("titane_nugget", new MineriaItem());
	public static final Item TITANE_AXE = register("titane_axe", new AxeItem(MineriaItem.ItemTier.TITANE, 1.0F, -3.0F, defaultGroup()));
	public static final Item TITANE_DAGGER = register("titane_dagger", new CustomWeaponItem(MineriaItem.ItemTier.TITANE, 4.5F, -2F, 17, defaultGroup()));
	public static final Item TITANE_DOUBLE_AXE = register("titane_double_axe", new AxeItem(MineriaItem.ItemTier.TITANE, 10.5F - MineriaItem.ItemTier.TITANE.getAttackDamageBonus(), -3.6F, defaultGroup()));
	public static final Item TITANE_HOE = register("titane_hoe", new HoeItem(MineriaItem.ItemTier.TITANE, -4, 0F, defaultGroup()));
	public static final Item TITANE_PICKAXE = register("titane_pickaxe", new PickaxeItem(MineriaItem.ItemTier.TITANE, 1, -2.8F, defaultGroup()));
	public static final Item TITANE_SHOVEL = register("titane_shovel", new ShovelItem(MineriaItem.ItemTier.TITANE, 1.5F, -3.0F, defaultGroup()));
	public static final Item TITANE_SWORD = register("titane_sword", new SwordItem(MineriaItem.ItemTier.TITANE, 3, -2.4F, defaultGroup()));
	public static final Item TITANE_BOOTS = register("titane_boots", new ArmorBuilder(MineriaItem.ArmorMaterial.TITANE, EquipmentSlot.FEET).build());
	public static final Item TITANE_LEGGINGS = register("titane_leggings", new ArmorBuilder(MineriaItem.ArmorMaterial.TITANE, EquipmentSlot.LEGS).build());
	public static final Item TITANE_CHESTPLATE = register("titane_chestplate", new ArmorBuilder(MineriaItem.ArmorMaterial.TITANE, EquipmentSlot.CHEST).build());
	public static final Item TITANE_HELMET = register("titane_helmet", new ArmorBuilder(MineriaItem.ArmorMaterial.TITANE, EquipmentSlot.HEAD).build());

	// Drinks
	public static final Item PLANTAIN_TEA = register("plantain_tea", new DrinkItem(apothecaryGroup().stacksTo(1).food(new FoodProperties.Builder().saturationMod(0).nutrition(0).alwaysEat().effect(
			() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 1800, 1, false, true), 1
	).build())));
	public static final Item MINT_TEA = register("mint_tea", new DrinkItem(apothecaryGroup().stacksTo(1).food(new FoodProperties.Builder().saturationMod(0).nutrition(0).alwaysEat().effect(
			() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 2400, 2, false, true), 1
	).effect(
			() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 2400, 2, false, true), 1
	).build())));
	public static final Item THYME_TEA = register("thyme_tea", new DrinkItem(apothecaryGroup().stacksTo(1).food(new FoodProperties.Builder().saturationMod(0).nutrition(0).alwaysEat().effect(
			() -> new MobEffectInstance(MobEffects.REGENERATION, 200, 4, false, true), 1
	).build())));
	public static final Item NETTLE_TEA = register("nettle_tea", new DrinkItem(apothecaryGroup().stacksTo(1).food(new FoodProperties.Builder().saturationMod(0).nutrition(0).alwaysEat().effect(
			() -> new MobEffectInstance(MobEffects.ABSORPTION, 2400, 0, false, true), 1
	).effect(
			() -> new MobEffectInstance(MobEffects.REGENERATION, 100, 1, false, true), 1
	).build())));
	public static final Item PULMONARY_TEA = register("pulmonary_tea", new DrinkItem(apothecaryGroup().stacksTo(1).food(new FoodProperties.Builder().saturationMod(0).nutrition(0).alwaysEat().effect(
			() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 1800, 1, false, true), 1
	).build())));
	public static final Item RHUBARB_TEA = register("rhubarb_tea", new DrinkItem(apothecaryGroup().stacksTo(1).food(new FoodProperties.Builder().saturationMod(0).nutrition(0).alwaysEat().build()), new DrinkItem.Properties().doNotImmediateCureEffects().onFoodEaten((stack, world, living) -> {
		living.addEffect(new BowelSoundEffectInstance(20 * 60 * 2, 0, MineriaItems.RHUBARB_TEA));
	})));
	public static final Item SENNA_TEA = register("senna_tea", new DrinkItem(apothecaryGroup().stacksTo(1).food(new FoodProperties.Builder().saturationMod(0).nutrition(0).alwaysEat().build()), new DrinkItem.Properties().doNotImmediateCureEffects().onFoodEaten((stack, world, living) -> {
		living.addEffect(new BowelSoundEffectInstance(20 * 60 * 3, 0, MineriaItems.SENNA_TEA));
	})));
	public static final Item CATHOLICON = register("catholicon", new DrinkItem(apothecaryGroup().stacksTo(1).food(new FoodProperties.Builder().saturationMod(0).nutrition(0).alwaysEat().build()), new DrinkItem.Properties().container(Items.GLASS_BOTTLE).doNotImmediateCureEffects().onFoodEaten((stack, world, living) -> {
		living.addEffect(new BowelSoundEffectInstance(20 * 60 * 4, 0, 0, MineriaItems.CATHOLICON));
	})));
	public static final Item BLACK_ELDERBERRY_TEA = register("black_elderberry_tea", new DrinkItem(apothecaryGroup().stacksTo(1).food(new FoodProperties.Builder().saturationMod(0).nutrition(0).alwaysEat().effect(
			() -> new MobEffectInstance(MobEffects.REGENERATION, 200, 1, false, true), 1
	).build())));
	public static final Item ELDERBERRY_TEA = register("elderberry_tea", new DrinkItem(apothecaryGroup().stacksTo(1).food(new FoodProperties.Builder().saturationMod(0).nutrition(0).alwaysEat().build()), new DrinkItem.Properties().onFoodEaten((stack, world, living) -> PoisonSource.ELDERBERRY.poison(living))));
	public static final Item STRYCHNOS_TOXIFERA_TEA = register("strychnos_toxifera_tea", new DrinkItem(apothecaryGroup().stacksTo(1).food(new FoodProperties.Builder().saturationMod(0).nutrition(0).alwaysEat().build()), new DrinkItem.Properties().onFoodEaten((stack, world, living) -> PoisonSource.STRYCHNOS_TOXIFERA.poison(living))));
	public static final Item STRYCHNOS_NUX_VOMICA_TEA = register("strychnos_nux-vomica_tea", new DrinkItem(apothecaryGroup().stacksTo(1).food(new FoodProperties.Builder().saturationMod(0).nutrition(0).alwaysEat().build()), new DrinkItem.Properties().onFoodEaten((stack, world, living) -> PoisonSource.STRYCHNOS_NUX_VOMICA.poison(living))));
	public static final Item BELLADONNA_TEA = register("belladonna_tea", new DrinkItem(apothecaryGroup().stacksTo(1).food(new FoodProperties.Builder().saturationMod(0).nutrition(0).alwaysEat().build()), new DrinkItem.Properties().onFoodEaten((stack, world, livingEntity) -> PoisonSource.BELLADONNA.poison(livingEntity))));
	public static final Item MANDRAKE_TEA = register("mandrake_tea", new DrinkItem(apothecaryGroup().stacksTo(1).food(new FoodProperties.Builder().saturationMod(0).nutrition(0).alwaysEat().build()), new DrinkItem.Properties().onFoodEaten((stack, world, living) -> PoisonSource.MANDRAKE.poison(living))));
	public static final Item MANDRAKE_ROOT_TEA = register("mandrake_root_tea", new DrinkItem(apothecaryGroup().stacksTo(1).food(new FoodProperties.Builder().saturationMod(0).nutrition(0).alwaysEat().build())));
	public static final Item GOJI_TEA = register("goji_tea", new DrinkItem(apothecaryGroup().stacksTo(1).food(new FoodProperties.Builder().saturationMod(0).nutrition(0).alwaysEat().effect(
			() -> new MobEffectInstance(MobEffects.REGENERATION, 400, 1, false, true), 1
	).effect(
			() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 0, false, true), 1
	).effect(
			() -> new MobEffectInstance(MobEffects.ABSORPTION, 600, 0, false, true), 1
	).build())));
	public static final Item SAUSSUREA_COSTUS_ROOT_TEA = register("saussurea_costus_root_tea", new DrinkItem(apothecaryGroup().stacksTo(1).food(new FoodProperties.Builder().saturationMod(0).nutrition(0).alwaysEat().effect(
			() -> new MobEffectInstance(MobEffects.HEALTH_BOOST, 60 * 20 * 2, 0, false, true), 1
	).build()), new DrinkItem.Properties().onFoodEaten((stack, world, living) -> {
		if(living.hasEffect(MineriaEffects.BOWEL_SOUNDS.get()))
		{
			BowelSoundEffectInstance instance = (BowelSoundEffectInstance) living.getEffect(MineriaEffects.BOWEL_SOUNDS.get());

			if(instance.getDuration() < 60 * 20)
			{
				living.removeEffect(MineriaEffects.BOWEL_SOUNDS.get());
			}
			else
			{
				instance.decreaseVolume(1.5F, 0.5F);
				instance.increaseDelay(100, 300);
				living.addEffect(instance);
			}
		}
		if(living.hasEffect(MobEffects.CONFUSION))
		{
			MobEffectInstance confusionEffect = living.getEffect(MobEffects.CONFUSION);

			if(confusionEffect instanceof CustomEffectInstance && ((CustomEffectInstance) confusionEffect).getActiveParentEffect(living).filter(PoisonEffectInstance.class::isInstance).isPresent())
			{
				if(confusionEffect.getAmplifier() == 0)
					((CustomEffectInstance) confusionEffect).setDuration(confusionEffect.getDuration() / 4);
				else if(confusionEffect.getAmplifier() == 1)
				{
					((CustomEffectInstance) confusionEffect).setDuration(confusionEffect.getDuration() / 2);
					((CustomEffectInstance) confusionEffect).setAmplifier(0);
				} else
					((CustomEffectInstance) confusionEffect).setAmplifier(confusionEffect.getAmplifier() - 2);
			}
			else
				living.removeEffect(MobEffects.CONFUSION);
		}
	})));
	public static final Item FIVE_FLAVOR_FRUIT_TEA = register("five_flavor_fruit_tea", new DrinkItem(apothecaryGroup().stacksTo(1).food(new FoodProperties.Builder().saturationMod(0).nutrition(0).alwaysEat().effect(
			() -> new MobEffectInstance(MobEffects.NIGHT_VISION, 20 * 60 * 2, 0, false, true), 1
	).effect(
			() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20 * 90, 2, false, true), 1
	).build())));
	public static final Item PULSATILLA_CHINENSIS_ROOT_TEA = register("pulsatilla_chinensis_root_tea", new DrinkItem(apothecaryGroup().stacksTo(1).food(new FoodProperties.Builder().saturationMod(0).nutrition(0).alwaysEat().effect(
			() -> new MobEffectInstance(MobEffects.REGENERATION, 20 * 10, 3, false, true), 1
	).build()), new DrinkItem.Properties().onFoodEaten((stack, world, living) -> {
		living.removeEffect(MineriaEffects.BOWEL_SOUNDS.get());
		living.removeEffect(MobEffects.CONFUSION);
	})));
	public static final Item JULEP = register("julep", new DrinkItem(apothecaryGroup().stacksTo(1).food(new FoodProperties.Builder().saturationMod(0.2F).nutrition(6).alwaysEat().build()), new DrinkItem.Properties().container(Items.GLASS_BOTTLE)));
	public static final Item CHARCOAL_ANTI_POISON = register("charcoal_anti_poison", new DrinkItem(apothecaryGroup().stacksTo(1).food(new FoodProperties.Builder().saturationMod(0).nutrition(0).alwaysEat().build()), new DrinkItem.Properties().container(Items.GLASS_BOTTLE).onFoodEaten((stack, world, living) -> DrinkItem.lockLaxativeDrinks(living))));
	public static final Item MILK_ANTI_POISON = register("milk_anti_poison", new DrinkItem(apothecaryGroup().stacksTo(1).food(new FoodProperties.Builder().saturationMod(0).nutrition(0).alwaysEat().build()), new DrinkItem.Properties().container(Items.GLASS_BOTTLE).onFoodEaten((stack, world, living) -> DrinkItem.unlockLaxativeDrinks(living))));
	public static final Item NAUSEOUS_ANTI_POISON = register("nauseous_anti_poison", new DrinkItem(apothecaryGroup().stacksTo(1).food(new FoodProperties.Builder().saturationMod(0).nutrition(0).alwaysEat().effect(
			() -> new MobEffectInstance(MobEffects.CONFUSION, 200, 3, false, true), 1
	).build()), new DrinkItem.Properties().container(Items.GLASS_BOTTLE)));
	public static final Item ANTI_POISON = register("anti_poison", new DrinkItem(apothecaryGroup().stacksTo(1).food(new FoodProperties.Builder().saturationMod(0).nutrition(0).alwaysEat().build()), new DrinkItem.Properties().container(Items.GLASS_BOTTLE)));
	public static final Item MIRACLE_ANTI_POISON = register("miracle_anti_poison", new DrinkItem(apothecaryGroup().stacksTo(1).food(new FoodProperties.Builder().saturationMod(0).nutrition(0).alwaysEat().build()), new DrinkItem.Properties().container(Items.GLASS_BOTTLE)));

	// Spawn eggs
	public static final Item GOLDEN_SILVERFISH_SPAWN_EGG = register("golden_silverfish_spawn_egg", new ForgeSpawnEggItem(MineriaEntities.GOLDEN_SILVERFISH, 12888340, 12852517, defaultGroup()));
	public static final Item WIZARD_SPAWN_EGG = register("wizard_spawn_egg", new ForgeSpawnEggItem(MineriaEntities.WIZARD, 2697513, 5349438, defaultGroup()));
	public static final Item DRUID_SPAWN_EGG = register("druid_spawn_egg", new ForgeSpawnEggItem(MineriaEntities.DRUID, 13684436, 13681525, defaultGroup()));
	public static final Item OVATE_SPAWN_EGG = register("ovate_spawn_egg", new ForgeSpawnEggItem(MineriaEntities.OVATE, 4741693, 13681525, defaultGroup()));
	public static final Item BARD_SPAWN_EGG = register("bard_spawn_egg", new ForgeSpawnEggItem(MineriaEntities.BARD, 5133681, 13681525, defaultGroup()));
	public static final Item FIRE_GOLEM_SPAWN_EGG = register("fire_golem_spawn_egg", new ForgeSpawnEggItem(MineriaEntities.FIRE_GOLEM, 2302755, 13265690, defaultGroup()));
	public static final Item DIRT_GOLEM_SPAWN_EGG = register("dirt_golem_spawn_egg", new ForgeSpawnEggItem(MineriaEntities.DIRT_GOLEM, 7224341, 12158300, defaultGroup()));
	public static final Item AIR_SPIRIT_SPAWN_EGG = register("air_spirit_spawn_egg", new ForgeSpawnEggItem(MineriaEntities.AIR_SPIRIT, 15396842, 10925746, defaultGroup()));
	public static final Item WATER_SPIRIT_SPAWN_EGG = register("water_spirit_spawn_egg", new ForgeSpawnEggItem(MineriaEntities.WATER_SPIRIT, 2834428, 11258367, defaultGroup()));
	public static final Item DRUIDIC_WOLF_SPAWN_EGG = register("druidic_wolf_spawn_egg", new ForgeSpawnEggItem(MineriaEntities.DRUIDIC_WOLF, 0xC1BDBD, 0xB00808, defaultGroup()));
	public static final Item BROWN_BEAR_SPAWN_EGG = register("brown_bear_spawn_egg", new ForgeSpawnEggItem(MineriaEntities.BROWN_BEAR, 0x563B30, 0x372620, defaultGroup()));
	public static final Item BUDDHIST_SPAWN_EGG = register("buddhist_spawn_egg", new ForgeSpawnEggItem(MineriaEntities.BUDDHIST, 16097063, 12401429, defaultGroup()));
	public static final Item ASIATIC_HERBALIST_SPAWN_EGG = register("asiatic_herbalist_spawn_egg", new ForgeSpawnEggItem(MineriaEntities.ASIATIC_HERBALIST, 15377433, 13816530, defaultGroup()));

	// Easter Eggs TODOLTR
	/*private static final Item MRLULU_SWORD = registerOn("mrlulu_sword", new SwordItem(ItemTier.DIAMOND, 3, -2.4F, devProperties()), 26, 6);
	private static final Item MATHYS_CRAFT_SWORD = registerOn("mathys_craft_sword", new SwordItem(ItemTier.DIAMOND, 3, -2.4F, devProperties()), 10, 11);*/

	private static Item register(String name, Item instance)
	{
		ITEMS.register(name, () -> instance);
		return instance;
	}

	private static Item registerCompostable(String name, Item instance, float compostValue)
	{
		register(name, instance);
		ComposterBlock.COMPOSTABLES.put(instance, compostValue);
		return instance;
	}

	/*private static Item registerOn(String name, Item instance, int day, int month)
	{
		if(MineriaUtils.currentDateMatches(month, day) && MineriaConfig.getValueFromFile(ModConfig.Type.COMMON, MineriaConfig.COMMON.enableDynamicEasterEggs)) register(name, instance);
		return instance;
	}*/

	private static Item.Properties defaultGroup()
	{
		return new Item.Properties().tab(Mineria.MINERIA_GROUP);
	}

	private static Item.Properties apothecaryGroup()
	{
		return new Item.Properties().tab(Mineria.APOTHECARY_GROUP);
	}

	public static final class Tags
	{
		public static final Tag.Named<Item> PLANTS = ItemTags.bind("mineria:plants");
		public static final Tag.Named<Item> LAXATIVE_DRINKS = ItemTags.bind("mineria:laxative_drinks");
		public static final Tag.Named<Item> ANTI_POISONS = ItemTags.bind("mineria:anti_poisons");
		public static final Tag.Named<Item> POISONOUS_TEAS = ItemTags.bind("mineria:poisonous_teas");
		public static final Tag.Named<Item> TEAS = ItemTags.bind("mineria:teas");
		public static final Tag.Named<Item> ALLOWED_BLOCKS_RITUAL_TABLE = ItemTags.bind("mineria:allowed_blocks_ritual_table");
	}
}
