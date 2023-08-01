package io.github.mineria_mc.mineria.util;

import io.github.mineria_mc.mineria.common.effects.potions.MineriaPotion;
import io.github.mineria_mc.mineria.common.init.MineriaCreativeModeTabs;
import io.github.mineria_mc.mineria.common.init.MineriaPotions;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.RegistryObject;

import static io.github.mineria_mc.mineria.common.init.MineriaBlocks.*;
import static io.github.mineria_mc.mineria.common.init.MineriaItems.*;

public final class MineriaCreativeTabsContents {
    public static final CreativeModeTab.DisplayItemsGenerator MINERIA_TAB_ITEMS = (display, output) -> {
        acceptAll(output,
                // Crafting Materials
                GOLD_STICK,
                IRON_STICK,
                FILTER,
                CUP,

                // Copper
                COPPER_PLATE,
                COPPER_STICK,
                COPPER_SWORD,
                COPPER_BOW,

                // Lead
                LEAD_ORE,
                DEEPSLATE_LEAD_ORE,
                LEAD_INGOT,
                LEAD_BLOCK,
                LEAD_NUGGET,
                LEAD_SWORD,
                COMPRESSED_LEAD_INGOT,
                COMPRESSED_LEAD_BLOCK,
                COMPRESSED_LEAD_SWORD,
                LEAD_SPIKE,
                COMPRESSED_LEAD_SPIKE,

                // Silver
                SILVER_ORE,
                DEEPSLATE_SILVER_ORE,
                SILVER_INGOT,
                SILVER_BLOCK,
                SILVER_NUGGET,
                SILVER_STICK,
                SILVER_SWORD,
                SILVER_PICKAXE,
                SILVER_AXE,
                SILVER_SHOVEL,
                SILVER_HOE,
                SILVER_HELMET,
                SILVER_CHESTPLATE,
                SILVER_LEGGINGS,
                SILVER_BOOTS,
                SILVER_APPLE,

                // Titane & Vanadium
                TITANE_ORE,
                DEEPSLATE_TITANE_ORE,
                TITANE_INGOT,
                TITANE_BLOCK,
                TITANE_NUGGET,
                TITANE_SWORD,
                TITANE_PICKAXE,
                TITANE_AXE,
                TITANE_SHOVEL,
                TITANE_HOE,
                TITANE_DAGGER,
                TITANE_DOUBLE_AXE,
                TITANE_HELMET,
                TITANE_CHESTPLATE,
                TITANE_LEGGINGS,
                TITANE_BOOTS,
                TITANE_SWORD_WITH_COPPER_HANDLE,
                TITANE_SWORD_WITH_SILVER_HANDLE,
                TITANE_SWORD_WITH_GOLD_HANDLE,
                TITANE_SWORD_WITH_IRON_HANDLE,
                VANADIUM_INGOT,
                VANADIUM_HELMET,

                // Lonsdaleite
                LONSDALEITE_ORE,
                DEEPSLATE_LONSDALEITE_ORE,
                LONSDALEITE,
                LONSDALEITE_BLOCK,
                LONSDALEITE_SWORD,
                LONSDALEITE_PICKAXE,
                LONSDALEITE_AXE,
                LONSDALEITE_SHOVEL,
                LONSDALEITE_HOE,
                LONSDALEITE_DAGGER,
                LONSDALEITE_DOUBLE_AXE,
                LONSDALEITE_HELMET,
                LONSDALEITE_CHESTPLATE,
                LONSDALEITE_LEGGINGS,
                LONSDALEITE_BOOTS,

                // Machines
                TITANE_EXTRACTOR,
                EXTRACTOR,
                XP_BLOCK,

                // Random Weapons
                ItemStackProvider.defaultInstance(KUNAI),
                BAMBOO_BLOWGUN,
                BLOWGUN_REFILL,
                SCALPEL,

                // Random plants
                SPRUCE_YEW_SAPLING,
                SAKURA_SAPLING,
                SPRUCE_YEW_LEAVES,
                SAKURA_LEAVES,
                GIROLLE,
                HORN_OF_PLENTY,
                PUFFBALL,

                // Random Items
                XP_ORB,
                COMPRESSED_XP_ORB,
                SUPER_COMPRESSED_XP_ORB,
                SUPER_DUPER_COMPRESSED_XP_ORB,
                MYSTERY_DISC,
                MUSIC_DISC_PIPPIN_THE_HUNCHBACK,
                FUGU,
                FUGU_BUCKET,
                RED_TUNA,
                COOKED_RED_TUNA,
                RED_TUNA_BUCKET,
                RICE_PLANTS,
                RICE_GRAINS,

                // Random Blocks
                BLUE_GLOWSTONE,
                MINERAL_SAND,
                XP_WALL,
                RITUAL_TABLE,
                FIRE_ELEMENTARY_STONE,
                WATER_ELEMENTARY_STONE,
                AIR_ELEMENTARY_STONE,
                GROUND_ELEMENTARY_STONE,
                GOLDEN_SILVERFISH_NETHERRACK,
                display.hasPermissions() ? DEBUG_BLOCK : null,

                // Water Barrels
                ItemStackProvider.defaultInstance(WATER_BARREL),
                ItemStackProvider.defaultInstance(INFINITE_WATER_BARREL),
                ItemStackProvider.defaultInstance(COPPER_WATER_BARREL),
                ItemStackProvider.defaultInstance(IRON_FLUID_BARREL),
                ItemStackProvider.defaultInstance(GOLDEN_WATER_BARREL),
                ItemStackProvider.defaultInstance(DIAMOND_FLUID_BARREL),
                TNT_BARREL,

                // Barrel Upgrades
                BARREL_INVENTORY_UPGRADE,
                BARREL_FLUID_UPGRADE,
                BARREL_STORAGE_UPGRADE_1,
                BARREL_STORAGE_UPGRADE_2,
                BARREL_STORAGE_UPGRADE_3,
                BARREL_PUMPING_UPGRADE,
                BARREL_NETHERITE_UPGRADE,

                GOLDEN_SILVERFISH_SPAWN_EGG,
                WIZARD_SPAWN_EGG,
                DRUID_SPAWN_EGG,
                OVATE_SPAWN_EGG,
                BARD_SPAWN_EGG,
                FIRE_GOLEM_SPAWN_EGG,
                DIRT_GOLEM_SPAWN_EGG,
                AIR_SPIRIT_SPAWN_EGG,
                WATER_SPIRIT_SPAWN_EGG,
                DRUIDIC_WOLF_SPAWN_EGG,
                BROWN_BEAR_SPAWN_EGG,
                BUDDHIST_SPAWN_EGG,
                ASIATIC_HERBALIST_SPAWN_EGG,
                FUGU_SPAWN_EGG,
                RED_TUNA_SPAWN_EGG,

                ItemStackProvider.potions(MINERIA_POTION, MineriaCreativeModeTabs.MINERIA.get()),
                ItemStackProvider.potions(MINERIA_SPLASH_POTION, MineriaCreativeModeTabs.MINERIA.get()),
                ItemStackProvider.potions(MINERIA_LINGERING_POTION, MineriaCreativeModeTabs.MINERIA.get())
        );
    };

    public static final CreativeModeTab.DisplayItemsGenerator APOTHECARY_TAB_ITEMS = (display, output) -> {
        acceptAll(output,
                // Apothecarium
                APOTHECARIUM,

                // Crafting Materials
                CHARCOAL_DUST,
                CINNAMON_DUST,
                GUM_ARABIC_JAR,
                ORANGE_BLOSSOM,
                DISTILLED_ORANGE_BLOSSOM_WATER,
                SYRUP,
                JULEP,
                MANDRAKE_ROOT,
                PULSATILLA_CHINENSIS_ROOT,
                SAUSSUREA_COSTUS_ROOT,
                GINGER,
                DRUID_HEART,
                MISTLETOE,

                // Machines
                INFUSER,
                DISTILLER,
                APOTHECARY_TABLE,

                // Plants
                PLANTAIN,
                MINT,
                THYME,
                NETTLE,
                PULMONARY,
                RHUBARB,
                SENNA,
                SENNA_BUSH,
                ELDERBERRY_BUSH,
                BLACK_ELDERBERRY_BUSH,
                STRYCHNOS_TOXIFERA,
                STRYCHNOS_NUX_VOMICA,
                BELLADONNA,
                MANDRAKE,
                LYCIUM_CHINENSE,
                SAUSSUREA_COSTUS,
                SCHISANDRA_CHINENSIS,
                PULSATILLA_CHINENSIS,

                // Fruits
                BLACK_ELDERBERRY,
                ELDERBERRY,
                GOJI,
                FIVE_FLAVOR_FRUIT,
                YEW_BERRIES,

                // Random Items
                BILLHOOK,
                JAR,
                MAGIC_POTION,
                WIZARD_HAT,

                // Tea
                PLANTAIN_TEA,
                MINT_TEA,
                THYME_TEA,
                NETTLE_TEA,
                PULMONARY_TEA,
                RHUBARB_TEA,
                SENNA_TEA,
                BLACK_ELDERBERRY_TEA,
                ELDERBERRY_TEA,
                STRYCHNOS_TOXIFERA_TEA,
                STRYCHNOS_NUX_VOMICA_TEA,
                BELLADONNA_TEA,
                MANDRAKE_TEA,
                MANDRAKE_ROOT_TEA,
                GOJI_TEA,
                SAUSSUREA_COSTUS_ROOT_TEA,
                FIVE_FLAVOR_FRUIT_TEA,
                PULSATILLA_CHINENSIS_ROOT_TEA,

                // Anti-poison
                CATHOLICON,
                CHARCOAL_ANTI_POISON,
                MILK_ANTI_POISON,
                NAUSEOUS_ANTI_POISON,
                ANTI_POISON,
                MIRACLE_ANTI_POISON
        );
    };

    private static void acceptAll(CreativeModeTab.Output output, Object... items) {
        for(Object obj : items) {
            if(obj == null) {
                continue;
            }
            if(obj instanceof ItemLike item) {
                output.accept(item);
                continue;
            }
            if(obj instanceof RegistryObject<?> reg && reg.isPresent() && reg.get() instanceof ItemLike item) {
                output.accept(item);
                continue;
            }
            if(obj instanceof ItemStackProvider stackProvider) {
                for (ItemStack stack : stackProvider.stacks()) {
                    output.accept(stack);
                }
            }
        }
    }

    @FunctionalInterface
    private interface ItemStackProvider {
        ItemStack[] stacks();

        static ItemStackProvider of(ItemStackProvider provider) {
            return provider;
        }

        static ItemStackProvider defaultInstance(RegistryObject<? extends ItemLike> item) {
            return () -> new ItemStack[] {item.get().asItem().getDefaultInstance()};
        }

        static ItemStackProvider potions(RegistryObject<Item> potionItem, CreativeModeTab currentTab) {
            return () -> DeferredRegisterUtil.presentEntries(MineriaPotions.POTIONS)
                    .filter(potion -> !(potion instanceof MineriaPotion mineriaPotion) || mineriaPotion.showInItemGroup(currentTab, potionItem.get()))
                    .map(potion -> PotionUtils.setPotion(new ItemStack(potionItem.get()), potion)).toArray(ItemStack[]::new);
        }
    }
}