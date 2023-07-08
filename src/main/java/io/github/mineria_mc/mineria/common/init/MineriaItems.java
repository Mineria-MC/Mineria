package io.github.mineria_mc.mineria.common.init;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.effects.instances.BowelSoundMobEffectInstance;
import io.github.mineria_mc.mineria.common.effects.instances.ModdedMobEffectInstance;
import io.github.mineria_mc.mineria.common.effects.instances.PoisoningHiddenEffectInstance;
import io.github.mineria_mc.mineria.common.effects.util.PoisonSource;
import io.github.mineria_mc.mineria.common.items.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.function.Supplier;

public class MineriaItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Mineria.MODID);
    static final Queue<Runnable> POST_INIT_QUEUE = new ArrayDeque<>();

    //	public static final RegistryObject<Item> SPECIAL_ITEM = ITEMS.register("special_item", () -> new SpecialItem());
    public static final RegistryObject<Item> APOTHECARIUM = ITEMS.register("apothecarium", ApothecariumItem::new);

    // Ingredients
    public static final RegistryObject<Item> GOLD_STICK = ITEMS.register("gold_stick", MineriaItem::new);
    public static final RegistryObject<Item> IRON_STICK = ITEMS.register("iron_stick", MineriaItem::new);
    public static final RegistryObject<Item> FILTER = ITEMS.register("filter", () -> new Item(new Properties().stacksTo(32)));
    public static final RegistryObject<Item> CUP = ITEMS.register("cup", () -> new Item(new Properties().stacksTo(16)));
    public static final RegistryObject<Item> CHARCOAL_DUST = ITEMS.register("charcoal_dust", () -> new Item(new Properties()));
    public static final RegistryObject<Item> CINNAMON_DUST = ITEMS.register("cinnamon_dust", () -> new Item(new Properties()));
    public static final RegistryObject<Item> GUM_ARABIC_JAR = ITEMS.register("gum_arabic_jar", () -> new Item(new Properties()));
    public static final RegistryObject<Item> ORANGE_BLOSSOM = ITEMS.register("orange-blossom", () -> new Item(new Properties()));
    public static final RegistryObject<Item> DISTILLED_ORANGE_BLOSSOM_WATER = ITEMS.register("distilled_orange-blossom_water", () -> new DrinkItem(new Properties().food(new FoodProperties.Builder().saturationMod(0.1F).nutrition(2).alwaysEat().build()), new DrinkItem.Properties().container(Items.GLASS_BOTTLE)));
    public static final RegistryObject<Item> SYRUP = ITEMS.register("syrup", () -> new DrinkItem(new Properties().food(new FoodProperties.Builder().saturationMod(0.1F).nutrition(4).alwaysEat().build()), new DrinkItem.Properties().container(Items.GLASS_BOTTLE)));
    public static final RegistryObject<Item> MANDRAKE_ROOT = ITEMS.register("mandrake_root", () -> new Item(new Properties()));
    public static final RegistryObject<Item> PULSATILLA_CHINENSIS_ROOT = ITEMS.register("pulsatilla_chinensis_root", () -> new Item(new Properties()));
    public static final RegistryObject<Item> SAUSSUREA_COSTUS_ROOT = ITEMS.register("saussurea_costus_root", () -> new Item(new Properties()));
    public static final RegistryObject<Item> GINGER = ITEMS.register("ginger", () -> new Item(new Properties()));
    public static final RegistryObject<Item> DRUID_HEART = ITEMS.register("druid_heart", () -> new Item(new Properties().rarity(Rarity.UNCOMMON).food(new FoodProperties.Builder().alwaysEat()
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 255), 1)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, 255), 1).build())));
    public static final RegistryObject<Item> MISTLETOE = registerCompostable("mistletoe", () -> new Item(new Properties()), 0.8F);

    // Fruits
    public static final RegistryObject<Item> BLACK_ELDERBERRY = registerCompostable("black_elderberry", () -> new Item(new Properties().food(new FoodProperties.Builder().nutrition(4).build())), 0.65F);
    public static final RegistryObject<Item> ELDERBERRY = registerCompostable("elderberry", () -> new CallbackFoodItem(new Properties().food(new FoodProperties.Builder().nutrition(0).saturationMod(0).alwaysEat().build()), (stack, world, living) -> PoisonSource.ELDERBERRY.applyPoisoning(living)), 0.65F);
    public static final RegistryObject<Item> GOJI = registerCompostable("goji", () -> new Item(new Properties().food(new FoodProperties.Builder().nutrition(2).effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 400, 0), 1).build())), 0.8F);
    public static final RegistryObject<Item> FIVE_FLAVOR_FRUIT = registerCompostable("five_flavor_fruit", () -> new CallbackFoodItem(new Properties().food(new FoodProperties.Builder().nutrition(1).build()), (stack, world, living) -> {
        if (living.hasEffect(MineriaEffects.BOWEL_SOUNDS.get())) {
            BowelSoundMobEffectInstance instance = ModdedMobEffectInstance.getEffectSafe(living, MineriaEffects.BOWEL_SOUNDS.get());

            if (instance != null) {
                instance.decreaseVolume(1F, 0.5F);
                instance.increaseDelay(50, 300);
                living.addEffect(instance);
            }
        }

        MobEffectInstance nausea = living.getEffect(MobEffects.CONFUSION);
        if (nausea != null && nausea.getAmplifier() == 0) {
            living.removeEffect(MobEffects.CONFUSION);
        }
    }), 0.7F);
    public static final RegistryObject<Item> YEW_BERRIES = registerCompostable("yew_berries", () -> new CallbackFoodItem(new Properties().food(new FoodProperties.Builder().fast().build()), (stack, world, living) -> PoisonSource.YEW.applyPoisoning(living)), 0.65F);

    // Weapons & Tools
    public static final RegistryObject<Item> TITANE_SWORD_WITH_COPPER_HANDLE = ITEMS.register("titane_sword_with_copper_handle", () -> new CustomWeaponItem(MineriaItem.ItemTier.TITANE, 6F, -2.2F, new Properties().durability(4096)));
    public static final RegistryObject<Item> TITANE_SWORD_WITH_SILVER_HANDLE = ITEMS.register("titane_sword_with_silver_handle", () -> new CustomWeaponItem(MineriaItem.ItemTier.TITANE, 3F, -1F, new Properties().durability(2048)));
    public static final RegistryObject<Item> TITANE_SWORD_WITH_GOLD_HANDLE = ITEMS.register("titane_sword_with_gold_handle", () -> new CustomWeaponItem(MineriaItem.ItemTier.TITANE, 8.5F, -3F, 10, new Properties().durability(2048)));
    public static final RegistryObject<Item> TITANE_SWORD_WITH_IRON_HANDLE = ITEMS.register("titane_sword_with_iron_handle", () -> new CustomWeaponItem(MineriaItem.ItemTier.TITANE, 8.5F, -2.4F, new Properties().durability(1024)));
    public static final RegistryObject<Item> BILLHOOK = ITEMS.register("billhook", () -> new BillhookItem(new Properties().durability(240)));
    public static final RegistryObject<Item> BAMBOO_BLOWGUN = ITEMS.register("bamboo_blowgun", BlowgunItem::new);
    public static final RegistryObject<Item> BLOWGUN_REFILL = ITEMS.register("blowgun_refill", BlowgunRefillItem::new);
    public static final RegistryObject<Item> KUNAI = ITEMS.register("kunai", KunaiItem::new);
    public static final RegistryObject<Item> SCALPEL = ITEMS.register("scalpel", ScalpelItem::new);

    // Misc
    public static final RegistryObject<Item> XP_ORB = ITEMS.register("xp_orb", () -> new XPOrbItem(1, new Properties()));
    public static final RegistryObject<Item> COMPRESSED_XP_ORB = ITEMS.register("compressed_xp_orb", () -> new XPOrbItem(4, new Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> SUPER_COMPRESSED_XP_ORB = ITEMS.register("super_compressed_xp_orb", () -> new XPOrbItem(16, new Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> SUPER_DUPER_COMPRESSED_XP_ORB = ITEMS.register("super_duper_compressed_xp_orb", () -> new XPOrbItem(64, new Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> VANADIUM_INGOT = ITEMS.register("vanadium_ingot", MineriaItem::new);
    public static final RegistryObject<Item> VANADIUM_HELMET = ITEMS.register("vanadium_helmet", () -> new ArmorBuilder(MineriaItem.ArmorMaterial.VANADIUM, ArmorItem.Type.HELMET).onArmorTick((stack, world, player) -> player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, (12 * 20), 0, false, false))).build());
    public static final RegistryObject<Item> MINERIA_POTION = ITEMS.register("mineria_potion", () -> new MineriaPotionItem(new Properties().stacksTo(1)));
    public static final RegistryObject<Item> MINERIA_SPLASH_POTION = ITEMS.register("mineria_splash_potion", () -> new MineriaThrowablePotionItem(new Properties().stacksTo(1), false));
    public static final RegistryObject<Item> MINERIA_LINGERING_POTION = ITEMS.register("mineria_lingering_potion", () -> new MineriaThrowablePotionItem(new Properties().stacksTo(1), true));
    public static final RegistryObject<Item> MYSTERY_DISC = ITEMS.register("mystery_disc", MysteryDiscItem::new);
    public static final RegistryObject<Item> MUSIC_DISC_PIPPIN_THE_HUNCHBACK = ITEMS.register("music_disc_pippin_the_hunchback", () -> new RecordItem(0, MineriaSounds.MUSIC_PIPPIN_THE_HUNCHBACK, new Properties().stacksTo(1).rarity(Rarity.RARE), 193 * 20));
    public static final RegistryObject<Item> JAR = ITEMS.register("jar", JarItem::new);
    public static final RegistryObject<Item> MAGIC_POTION = ITEMS.register("magic_potion", MagicPotionItem::new);
    public static final RegistryObject<Item> WIZARD_HAT = ITEMS.register("wizard_hat", WizardHatItem::new);

    // Barrel upgrades
    public static final RegistryObject<Item> BARREL_INVENTORY_UPGRADE = ITEMS.register("barrel_inventory_upgrade", () -> new DiamondBarrelUpgradeItem(true));
    public static final RegistryObject<Item> BARREL_FLUID_UPGRADE = ITEMS.register("barrel_fluid_upgrade", () -> new DiamondBarrelUpgradeItem(false));
    public static final RegistryObject<Item> BARREL_STORAGE_UPGRADE_1 = ITEMS.register("barrel_storage_upgrade_1", () -> new DiamondBarrelUpgradeItem(false));
    public static final RegistryObject<Item> BARREL_STORAGE_UPGRADE_2 = ITEMS.register("barrel_storage_upgrade_2", () -> new DiamondBarrelUpgradeItem(false));
    public static final RegistryObject<Item> BARREL_STORAGE_UPGRADE_3 = ITEMS.register("barrel_storage_upgrade_3", () -> new DiamondBarrelUpgradeItem(false));
    public static final RegistryObject<Item> BARREL_PUMPING_UPGRADE = ITEMS.register("barrel_pumping_upgrade", () -> new DiamondBarrelUpgradeItem(true));
    public static final RegistryObject<Item> BARREL_NETHERITE_UPGRADE = ITEMS.register("barrel_netherite_upgrade", () -> new DiamondBarrelUpgradeItem(false));

    // Copper
    public static final RegistryObject<Item> COPPER_PLATE = ITEMS.register("copper_plate", MineriaItem::new);
    public static final RegistryObject<Item> COPPER_BOW = ITEMS.register("copper_bow", () -> new MineriaBow(4.0F, 1.15F, 1, new Properties().durability(528)));
    public static final RegistryObject<Item> COPPER_STICK = ITEMS.register("copper_stick", MineriaItem::new);
    public static final RegistryObject<Item> COPPER_SWORD = ITEMS.register("copper_sword", () -> new SwordItem(MineriaItem.ItemTier.COPPER, 3, -2.4F, new Properties()));

    // Lead
    public static final RegistryObject<Item> LEAD_INGOT = ITEMS.register("lead_ingot", MineriaItem::new);
    public static final RegistryObject<Item> LEAD_SWORD = ITEMS.register("lead_sword", () -> new SwordItem(MineriaItem.ItemTier.LEAD, 3, -2.4F, new Properties()));
    public static final RegistryObject<Item> LEAD_NUGGET = ITEMS.register("lead_nugget", MineriaItem::new);
    public static final RegistryObject<Item> COMPRESSED_LEAD_INGOT = ITEMS.register("compressed_lead_ingot", MineriaItem::new);
    public static final RegistryObject<Item> COMPRESSED_LEAD_SWORD = ITEMS.register("compressed_lead_sword", () -> new SwordItem(MineriaItem.ItemTier.COMPRESSED_LEAD, 3, -2.4F, new Properties()));

    // Lonsdaleite
    public static final RegistryObject<Item> LONSDALEITE = ITEMS.register("lonsdaleite", MineriaItem::new);
    public static final RegistryObject<Item> LONSDALEITE_AXE = ITEMS.register("lonsdaleite_axe", () -> new AxeItem(MineriaItem.ItemTier.LONSDALEITE, -2.0F, -3.0F, new Properties()));
    public static final RegistryObject<Item> LONSDALEITE_DAGGER = ITEMS.register("lonsdaleite_dagger", () -> new CustomWeaponItem(MineriaItem.ItemTier.LONSDALEITE, 7.5F, -1.75F, 15, new Properties()));
    public static final RegistryObject<Item> LONSDALEITE_DOUBLE_AXE = ITEMS.register("lonsdaleite_double_axe", () -> new AxeItem(MineriaItem.ItemTier.LONSDALEITE, 13.5F - MineriaItem.ItemTier.LONSDALEITE.getAttackDamageBonus(), -3.4F, new Properties()));
    public static final RegistryObject<Item> LONSDALEITE_PICKAXE = ITEMS.register("lonsdaleite_pickaxe", () -> new PickaxeItem(MineriaItem.ItemTier.LONSDALEITE, 1, -2.8F, new Properties()));
    public static final RegistryObject<Item> LONSDALEITE_SHOVEL = ITEMS.register("lonsdaleite_shovel", () -> new ShovelItem(MineriaItem.ItemTier.LONSDALEITE, 1.5F, -3.0F, new Properties()));
    public static final RegistryObject<Item> LONSDALEITE_SWORD = ITEMS.register("lonsdaleite_sword", LonsdaleiteSwordItem::new);
    public static final RegistryObject<Item> LONSDALEITE_HOE = ITEMS.register("lonsdaleite_hoe", () -> new HoeItem(MineriaItem.ItemTier.LONSDALEITE, -7, 0F, new Properties()));
    public static final RegistryObject<Item> LONSDALEITE_BOOTS = ITEMS.register("lonsdaleite_boots", () -> new ArmorBuilder(MineriaItem.ArmorMaterial.LONSDALEITE, ArmorItem.Type.BOOTS).build());
    public static final RegistryObject<Item> LONSDALEITE_LEGGINGS = ITEMS.register("lonsdaleite_leggings", () -> new ArmorBuilder(MineriaItem.ArmorMaterial.LONSDALEITE, ArmorItem.Type.LEGGINGS).build());
    public static final RegistryObject<Item> LONSDALEITE_CHESTPLATE = ITEMS.register("lonsdaleite_chestplate", () -> new ArmorBuilder(MineriaItem.ArmorMaterial.LONSDALEITE, ArmorItem.Type.CHESTPLATE).build());
    public static final RegistryObject<Item> LONSDALEITE_HELMET = ITEMS.register("lonsdaleite_helmet", () -> new ArmorBuilder(MineriaItem.ArmorMaterial.LONSDALEITE, ArmorItem.Type.HELMET).build());

    // Silver
    public static final RegistryObject<Item> SILVER_INGOT = ITEMS.register("silver_ingot", MineriaItem::new);
    public static final RegistryObject<Item> SILVER_APPLE = ITEMS.register("silver_apple", () -> new Item(new Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(6.9F).alwaysEat().fast().effect(
            () -> new MobEffectInstance(MobEffects.REGENERATION, (20 * 20), 0, false, true), 1.0F
    ).effect(
            () -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, (90 * 20), 0, false, true), 1.0F
    ).effect(
            () -> new MobEffectInstance(MobEffects.ABSORPTION, (40 * 20), 1, false, true), 1.0F
    ).build()).stacksTo(64)));
    public static final RegistryObject<Item> SILVER_NUGGET = ITEMS.register("silver_nugget", MineriaItem::new);
    public static final RegistryObject<Item> SILVER_STICK = ITEMS.register("silver_stick", MineriaItem::new);
    public static final RegistryObject<Item> SILVER_AXE = ITEMS.register("silver_axe", () -> new AxeItem(MineriaItem.ItemTier.SILVER, 2.0F, -3.0F, new Properties()));
    public static final RegistryObject<Item> SILVER_PICKAXE = ITEMS.register("silver_pickaxe", () -> new PickaxeItem(MineriaItem.ItemTier.SILVER, 1, -2.8F, new Properties()));
    public static final RegistryObject<Item> SILVER_SHOVEL = ITEMS.register("silver_shovel", () -> new ShovelItem(MineriaItem.ItemTier.SILVER, 1.5F, -3.0F, new Properties()));
    public static final RegistryObject<Item> SILVER_SWORD = ITEMS.register("silver_sword", () -> new SwordItem(MineriaItem.ItemTier.SILVER, 3, -2.4F, new Properties()));
    public static final RegistryObject<Item> SILVER_HOE = ITEMS.register("silver_hoe", () -> new HoeItem(MineriaItem.ItemTier.SILVER, (int) -2.6F, 0F, new Properties()));
    public static final RegistryObject<Item> SILVER_BOOTS = ITEMS.register("silver_boots", () -> new ArmorBuilder(MineriaItem.ArmorMaterial.SILVER, ArmorItem.Type.BOOTS).build());
    public static final RegistryObject<Item> SILVER_LEGGINGS = ITEMS.register("silver_leggings", () -> new ArmorBuilder(MineriaItem.ArmorMaterial.SILVER, ArmorItem.Type.LEGGINGS).build());
    public static final RegistryObject<Item> SILVER_CHESTPLATE = ITEMS.register("silver_chestplate", () -> new ArmorBuilder(MineriaItem.ArmorMaterial.SILVER, ArmorItem.Type.CHESTPLATE).build());
    public static final RegistryObject<Item> SILVER_HELMET = ITEMS.register("silver_helmet", () -> new ArmorBuilder(MineriaItem.ArmorMaterial.SILVER, ArmorItem.Type.HELMET).build());

    // Titane
    public static final RegistryObject<Item> TITANE_INGOT = ITEMS.register("titane_ingot", MineriaItem::new);
    public static final RegistryObject<Item> TITANE_NUGGET = ITEMS.register("titane_nugget", MineriaItem::new);
    public static final RegistryObject<Item> TITANE_AXE = ITEMS.register("titane_axe", () -> new AxeItem(MineriaItem.ItemTier.TITANE, 1.0F, -3.0F, new Properties()));
    public static final RegistryObject<Item> TITANE_DAGGER = ITEMS.register("titane_dagger", () -> new CustomWeaponItem(MineriaItem.ItemTier.TITANE, 4.5F, -2F, 17, new Properties()));
    public static final RegistryObject<Item> TITANE_DOUBLE_AXE = ITEMS.register("titane_double_axe", () -> new AxeItem(MineriaItem.ItemTier.TITANE, 10.5F - MineriaItem.ItemTier.TITANE.getAttackDamageBonus(), -3.6F, new Properties()));
    public static final RegistryObject<Item> TITANE_HOE = ITEMS.register("titane_hoe", () -> new HoeItem(MineriaItem.ItemTier.TITANE, -4, 0F, new Properties()));
    public static final RegistryObject<Item> TITANE_PICKAXE = ITEMS.register("titane_pickaxe", () -> new PickaxeItem(MineriaItem.ItemTier.TITANE, 1, -2.8F, new Properties()));
    public static final RegistryObject<Item> TITANE_SHOVEL = ITEMS.register("titane_shovel", () -> new ShovelItem(MineriaItem.ItemTier.TITANE, 1.5F, -3.0F, new Properties()));
    public static final RegistryObject<Item> TITANE_SWORD = ITEMS.register("titane_sword", () -> new SwordItem(MineriaItem.ItemTier.TITANE, 3, -2.4F, new Properties()));
    public static final RegistryObject<Item> TITANE_BOOTS = ITEMS.register("titane_boots", () -> new ArmorBuilder(MineriaItem.ArmorMaterial.TITANE, ArmorItem.Type.BOOTS).build());
    public static final RegistryObject<Item> TITANE_LEGGINGS = ITEMS.register("titane_leggings", () -> new ArmorBuilder(MineriaItem.ArmorMaterial.TITANE, ArmorItem.Type.LEGGINGS).build());
    public static final RegistryObject<Item> TITANE_CHESTPLATE = ITEMS.register("titane_chestplate", () -> new ArmorBuilder(MineriaItem.ArmorMaterial.TITANE, ArmorItem.Type.CHESTPLATE).build());
    public static final RegistryObject<Item> TITANE_HELMET = ITEMS.register("titane_helmet", () -> new ArmorBuilder(MineriaItem.ArmorMaterial.TITANE, ArmorItem.Type.HELMET).build());

    // Drinks
    public static final RegistryObject<Item> PLANTAIN_TEA = ITEMS.register("plantain_tea", () -> new DrinkItem(new Properties().stacksTo(1).food(new FoodProperties.Builder().saturationMod(0).nutrition(0).alwaysEat().effect(
            () -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 1800, 1, false, true), 1
    ).build())));
    public static final RegistryObject<Item> MINT_TEA = ITEMS.register("mint_tea", () -> new DrinkItem(new Properties().stacksTo(1).food(new FoodProperties.Builder().saturationMod(0).nutrition(0).alwaysEat().effect(
            () -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 2400, 2, false, true), 1
    ).effect(
            () -> new MobEffectInstance(MobEffects.JUMP, 2400, 2, false, true), 1
    ).build())));
    public static final RegistryObject<Item> THYME_TEA = ITEMS.register("thyme_tea", () -> new DrinkItem(new Properties().stacksTo(1).food(new FoodProperties.Builder().saturationMod(0).nutrition(0).alwaysEat().effect(
            () -> new MobEffectInstance(MobEffects.REGENERATION, 200, 4, false, true), 1
    ).build())));
    public static final RegistryObject<Item> NETTLE_TEA = ITEMS.register("nettle_tea", () -> new DrinkItem(new Properties().stacksTo(1).food(new FoodProperties.Builder().saturationMod(0).nutrition(0).alwaysEat().effect(
            () -> new MobEffectInstance(MobEffects.ABSORPTION, 2400, 0, false, true), 1
    ).effect(
            () -> new MobEffectInstance(MobEffects.REGENERATION, 100, 1, false, true), 1
    ).build())));
    public static final RegistryObject<Item> PULMONARY_TEA = ITEMS.register("pulmonary_tea", () -> new DrinkItem(new Properties().stacksTo(1).food(new FoodProperties.Builder().saturationMod(0).nutrition(0).alwaysEat().effect(
            () -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 1800, 1, false, true), 1
    ).build())));
    public static final RegistryObject<Item> RHUBARB_TEA = ITEMS.register("rhubarb_tea", () -> new DrinkItem(new Properties().stacksTo(1).food(new FoodProperties.Builder().saturationMod(0).nutrition(0).alwaysEat().build()), new DrinkItem.Properties().doNotImmediateCureEffects().onFoodEaten((stack, world, living) -> {
        living.addEffect(new BowelSoundMobEffectInstance(20 * 60 * 2, 0, MineriaItems.RHUBARB_TEA.get()));
    })));
    public static final RegistryObject<Item> SENNA_TEA = ITEMS.register("senna_tea", () -> new DrinkItem(new Properties().stacksTo(1).food(new FoodProperties.Builder().saturationMod(0).nutrition(0).alwaysEat().build()), new DrinkItem.Properties().doNotImmediateCureEffects().onFoodEaten((stack, world, living) -> {
        living.addEffect(new BowelSoundMobEffectInstance(20 * 60 * 3, 0, MineriaItems.SENNA_TEA.get()));
    })));
    public static final RegistryObject<Item> CATHOLICON = ITEMS.register("catholicon", () -> new DrinkItem(new Properties().stacksTo(1).food(new FoodProperties.Builder().saturationMod(0).nutrition(0).alwaysEat().build()), new DrinkItem.Properties().container(Items.GLASS_BOTTLE).doNotImmediateCureEffects().onFoodEaten((stack, world, living) -> {
        living.addEffect(new BowelSoundMobEffectInstance(20 * 60 * 4, 0, 0, MineriaItems.CATHOLICON.get()));
    })));
    public static final RegistryObject<Item> BLACK_ELDERBERRY_TEA = ITEMS.register("black_elderberry_tea", () -> new DrinkItem(new Properties().stacksTo(1).food(new FoodProperties.Builder().saturationMod(0).nutrition(0).alwaysEat().effect(
            () -> new MobEffectInstance(MobEffects.REGENERATION, 200, 1, false, true), 1
    ).build())));
    public static final RegistryObject<Item> ELDERBERRY_TEA = ITEMS.register("elderberry_tea", () -> new DrinkItem(new Properties().stacksTo(1).food(new FoodProperties.Builder().saturationMod(0).nutrition(0).alwaysEat().build()), new DrinkItem.Properties().onFoodEaten((stack, world, living) -> PoisonSource.ELDERBERRY.applyPoisoning(living))));
    public static final RegistryObject<Item> STRYCHNOS_TOXIFERA_TEA = ITEMS.register("strychnos_toxifera_tea", () -> new DrinkItem(new Properties().stacksTo(1).food(new FoodProperties.Builder().saturationMod(0).nutrition(0).alwaysEat().build()), new DrinkItem.Properties().onFoodEaten((stack, world, living) -> PoisonSource.STRYCHNOS_TOXIFERA.applyPoisoning(living))));
    public static final RegistryObject<Item> STRYCHNOS_NUX_VOMICA_TEA = ITEMS.register("strychnos_nux-vomica_tea", () -> new DrinkItem(new Properties().stacksTo(1).food(new FoodProperties.Builder().saturationMod(0).nutrition(0).alwaysEat().build()), new DrinkItem.Properties().onFoodEaten((stack, world, living) -> PoisonSource.STRYCHNOS_NUX_VOMICA.applyPoisoning(living))));
    public static final RegistryObject<Item> BELLADONNA_TEA = ITEMS.register("belladonna_tea", () -> new DrinkItem(new Properties().stacksTo(1).food(new FoodProperties.Builder().saturationMod(0).nutrition(0).alwaysEat().build()), new DrinkItem.Properties().onFoodEaten((stack, world, livingEntity) -> PoisonSource.BELLADONNA.applyPoisoning(livingEntity))));
    public static final RegistryObject<Item> MANDRAKE_TEA = ITEMS.register("mandrake_tea", () -> new DrinkItem(new Properties().stacksTo(1).food(new FoodProperties.Builder().saturationMod(0).nutrition(0).alwaysEat().build()), new DrinkItem.Properties().onFoodEaten((stack, world, living) -> PoisonSource.MANDRAKE.applyPoisoning(living))));
    public static final RegistryObject<Item> MANDRAKE_ROOT_TEA = ITEMS.register("mandrake_root_tea", () -> new DrinkItem(new Properties().stacksTo(1).food(new FoodProperties.Builder().saturationMod(0).nutrition(0).alwaysEat().build())));
    public static final RegistryObject<Item> GOJI_TEA = ITEMS.register("goji_tea", () -> new DrinkItem(new Properties().stacksTo(1).food(new FoodProperties.Builder().saturationMod(0).nutrition(0).alwaysEat().effect(
            () -> new MobEffectInstance(MobEffects.REGENERATION, 400, 1, false, true), 1
    ).effect(
            () -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 0, false, true), 1
    ).effect(
            () -> new MobEffectInstance(MobEffects.ABSORPTION, 600, 0, false, true), 1
    ).build())));
    public static final RegistryObject<Item> SAUSSUREA_COSTUS_ROOT_TEA = ITEMS.register("saussurea_costus_root_tea", () -> new DrinkItem(new Properties().stacksTo(1).food(new FoodProperties.Builder().saturationMod(0).nutrition(0).alwaysEat().effect(
            () -> new MobEffectInstance(MobEffects.HEALTH_BOOST, 60 * 20 * 2, 0, false, true), 1
    ).build()), new DrinkItem.Properties().onFoodEaten((stack, world, living) -> {
        if (living.hasEffect(MineriaEffects.BOWEL_SOUNDS.get())) {
            BowelSoundMobEffectInstance instance = ModdedMobEffectInstance.getEffectSafe(living, MineriaEffects.BOWEL_SOUNDS.get());

            if(instance != null) {
                if (instance.getDuration() < 60 * 20) {
                    living.removeEffect(MineriaEffects.BOWEL_SOUNDS.get());
                } else {
                    instance.decreaseVolume(1.5F, 0.5F);
                    instance.increaseDelay(100, 300);
                    living.addEffect(instance);
                }
            }
        }
        if (living.hasEffect(MobEffects.CONFUSION)) {
            MobEffectInstance confusionEffect = living.getEffect(MobEffects.CONFUSION);

            if (confusionEffect instanceof PoisoningHiddenEffectInstance instance) {
                if (instance.getAmplifier() == 0)
                    instance.setDuration(instance.getDuration() / 4);
                else if (instance.getAmplifier() == 1) {
                    instance.setDuration(instance.getDuration() / 2);
                    instance.setAmplifier(0);
                } else
                    instance.setAmplifier(instance.getAmplifier() - 2);
            } else {
                living.removeEffect(MobEffects.CONFUSION);
            }
        }
    })));
    public static final RegistryObject<Item> FIVE_FLAVOR_FRUIT_TEA = ITEMS.register("five_flavor_fruit_tea", () -> new DrinkItem(new Properties().stacksTo(1).food(new FoodProperties.Builder().saturationMod(0).nutrition(0).alwaysEat().effect(
            () -> new MobEffectInstance(MobEffects.NIGHT_VISION, 20 * 60 * 2, 0, false, true), 1
    ).effect(
            () -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20 * 90, 2, false, true), 1
    ).build())));
    public static final RegistryObject<Item> PULSATILLA_CHINENSIS_ROOT_TEA = ITEMS.register("pulsatilla_chinensis_root_tea", () -> new DrinkItem(new Properties().stacksTo(1).food(new FoodProperties.Builder().saturationMod(0).nutrition(0).alwaysEat().effect(
            () -> new MobEffectInstance(MobEffects.REGENERATION, 20 * 10, 3, false, true), 1
    ).build()), new DrinkItem.Properties().onFoodEaten((stack, world, living) -> {
        living.removeEffect(MineriaEffects.BOWEL_SOUNDS.get());
        living.removeEffect(MobEffects.CONFUSION);
    })));
    public static final RegistryObject<Item> JULEP = ITEMS.register("julep", () -> new DrinkItem(new Properties().stacksTo(1).food(new FoodProperties.Builder().saturationMod(0.2F).nutrition(6).alwaysEat().build()), new DrinkItem.Properties().container(Items.GLASS_BOTTLE)));
    public static final RegistryObject<Item> CHARCOAL_ANTI_POISON = ITEMS.register("charcoal_anti_poison", () -> new DrinkItem(new Properties().stacksTo(1).food(new FoodProperties.Builder().saturationMod(0).nutrition(0).alwaysEat().build()), new DrinkItem.Properties().container(Items.GLASS_BOTTLE).onFoodEaten((stack, world, living) -> DrinkItem.lockLaxativeDrinks(living))));
    public static final RegistryObject<Item> MILK_ANTI_POISON = ITEMS.register("milk_anti_poison", () -> new DrinkItem(new Properties().stacksTo(1).food(new FoodProperties.Builder().saturationMod(0).nutrition(0).alwaysEat().build()), new DrinkItem.Properties().container(Items.GLASS_BOTTLE).onFoodEaten((stack, world, living) -> DrinkItem.unlockLaxativeDrinks(living))));
    public static final RegistryObject<Item> NAUSEOUS_ANTI_POISON = ITEMS.register("nauseous_anti_poison", () -> new DrinkItem(new Properties().stacksTo(1).food(new FoodProperties.Builder().saturationMod(0).nutrition(0).alwaysEat().effect(
            () -> new MobEffectInstance(MobEffects.CONFUSION, 200, 3, false, true), 1
    ).build()), new DrinkItem.Properties().container(Items.GLASS_BOTTLE)));
    public static final RegistryObject<Item> ANTI_POISON = ITEMS.register("anti_poison", () -> new DrinkItem(new Properties().stacksTo(1).food(new FoodProperties.Builder().saturationMod(0).nutrition(0).alwaysEat().build()), new DrinkItem.Properties().container(Items.GLASS_BOTTLE)));
    public static final RegistryObject<Item> MIRACLE_ANTI_POISON = ITEMS.register("miracle_anti_poison", () -> new DrinkItem(new Properties().stacksTo(1).food(new FoodProperties.Builder().saturationMod(0).nutrition(0).alwaysEat().build()), new DrinkItem.Properties().container(Items.GLASS_BOTTLE)));

    // Spawn eggs
    public static final RegistryObject<Item> GOLDEN_SILVERFISH_SPAWN_EGG = ITEMS.register("golden_silverfish_spawn_egg", () -> new ForgeSpawnEggItem(MineriaEntities.GOLDEN_SILVERFISH, 12888340, 12852517, new Properties()));
    public static final RegistryObject<Item> WIZARD_SPAWN_EGG = ITEMS.register("wizard_spawn_egg", () -> new ForgeSpawnEggItem(MineriaEntities.WIZARD, 2697513, 5349438, new Properties()));
    public static final RegistryObject<Item> DRUID_SPAWN_EGG = ITEMS.register("druid_spawn_egg", () -> new ForgeSpawnEggItem(MineriaEntities.DRUID, 13684436, 13681525, new Properties()));
    public static final RegistryObject<Item> OVATE_SPAWN_EGG = ITEMS.register("ovate_spawn_egg", () -> new ForgeSpawnEggItem(MineriaEntities.OVATE, 4741693, 13681525, new Properties()));
    public static final RegistryObject<Item> BARD_SPAWN_EGG = ITEMS.register("bard_spawn_egg", () -> new ForgeSpawnEggItem(MineriaEntities.BARD, 5133681, 13681525, new Properties()));
    public static final RegistryObject<Item> FIRE_GOLEM_SPAWN_EGG = ITEMS.register("fire_golem_spawn_egg", () -> new ForgeSpawnEggItem(MineriaEntities.FIRE_GOLEM, 2302755, 13265690, new Properties()));
    public static final RegistryObject<Item> DIRT_GOLEM_SPAWN_EGG = ITEMS.register("dirt_golem_spawn_egg", () -> new ForgeSpawnEggItem(MineriaEntities.DIRT_GOLEM, 7224341, 12158300, new Properties()));
    public static final RegistryObject<Item> AIR_SPIRIT_SPAWN_EGG = ITEMS.register("air_spirit_spawn_egg", () -> new ForgeSpawnEggItem(MineriaEntities.AIR_SPIRIT, 15396842, 10925746, new Properties()));
    public static final RegistryObject<Item> WATER_SPIRIT_SPAWN_EGG = ITEMS.register("water_spirit_spawn_egg", () -> new ForgeSpawnEggItem(MineriaEntities.WATER_SPIRIT, 2834428, 11258367, new Properties()));
    public static final RegistryObject<Item> DRUIDIC_WOLF_SPAWN_EGG = ITEMS.register("druidic_wolf_spawn_egg", () -> new ForgeSpawnEggItem(MineriaEntities.DRUIDIC_WOLF, 0xC1BDBD, 0xB00808, new Properties()));
    public static final RegistryObject<Item> BROWN_BEAR_SPAWN_EGG = ITEMS.register("brown_bear_spawn_egg", () -> new ForgeSpawnEggItem(MineriaEntities.BROWN_BEAR, 0x563B30, 0x372620, new Properties()));
    public static final RegistryObject<Item> BUDDHIST_SPAWN_EGG = ITEMS.register("buddhist_spawn_egg", () -> new ForgeSpawnEggItem(MineriaEntities.BUDDHIST, 16097063, 12401429, new Properties()));
    public static final RegistryObject<Item> ASIATIC_HERBALIST_SPAWN_EGG = ITEMS.register("asiatic_herbalist_spawn_egg", () -> new ForgeSpawnEggItem(MineriaEntities.ASIATIC_HERBALIST, 15377433, 13816530, new Properties()));

    private static RegistryObject<Item> registerCompostable(String name, Supplier<Item> instance, float compostValue) {
        RegistryObject<Item> item = ITEMS.register(name, instance);
        POST_INIT_QUEUE.add(() -> ComposterBlock.COMPOSTABLES.put(item.get(), compostValue));
        return item;
    }

    public static void postInit() {
        POST_INIT_QUEUE.forEach(Runnable::run);
    }

    public static final class Tags {
        public static final TagKey<Item> LEAD_ORES = ITEMS.createTagKey("lead_ores");
        public static final TagKey<Item> SILVER_ORES = ITEMS.createTagKey("silver_ores");
        public static final TagKey<Item> TITANE_ORES = ITEMS.createTagKey("titane_ores");
        public static final TagKey<Item> LONSDALEITE_ORES = ITEMS.createTagKey("lonsdaleite_ores");
        public static final TagKey<Item> PLANTS = ITEMS.createTagKey("plants");
        public static final TagKey<Item> LAXATIVE_DRINKS = ITEMS.createTagKey("laxative_drinks");
        public static final TagKey<Item> ANTI_POISONS = ITEMS.createTagKey("anti_poisons");
        public static final TagKey<Item> POISONOUS_TEAS = ITEMS.createTagKey("poisonous_teas");
        public static final TagKey<Item> TEAS = ITEMS.createTagKey("teas");
        public static final TagKey<Item> ALLOWED_BLOCKS_RITUAL_TABLE = ITEMS.createTagKey("allowed_blocks_ritual_table");

        public static final TagKey<Item> TOOLS_DAGGERS = ITEMS.createTagKey(new ResourceLocation("forge", "tools/daggers"));
        public static final TagKey<Item> TOOLS_DOUBLE_AXES = ITEMS.createTagKey(new ResourceLocation("forge", "tools/double_axes"));
    }
}
