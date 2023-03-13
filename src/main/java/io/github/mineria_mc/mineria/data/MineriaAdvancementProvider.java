package io.github.mineria_mc.mineria.data;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.data.predicates.FluidBarrelCapacityPredicate;
import io.github.mineria_mc.mineria.common.data.predicates.ShapedRecipePredicate;
import io.github.mineria_mc.mineria.common.data.triggers.*;
import io.github.mineria_mc.mineria.common.effects.util.PoisonSource;
import io.github.mineria_mc.mineria.common.init.*;
import io.github.mineria_mc.mineria.common.init.datagen.MineriaStructures;
import io.github.mineria_mc.mineria.common.items.JarItem;
import io.github.mineria_mc.mineria.common.items.MineriaItem;
import io.github.mineria_mc.mineria.data.advancement.AdvancementTreeBuilder;
import io.github.mineria_mc.mineria.util.MineriaUtils;
import net.minecraft.Util;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class MineriaAdvancementProvider extends ForgeAdvancementProvider {
    public MineriaAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper helper) {
        super(output, registries, helper, List.of(new MineriaAdvancements()));
    }

    private static class MineriaAdvancements implements AdvancementGenerator {
        @Override
        public void generate(@Nonnull HolderLookup.Provider registries, @Nonnull Consumer<Advancement> saver, @Nonnull ExistingFileHelper helper) {
            new AdvancementTreeBuilder()
                    .group("mineria")
                    .background(new ResourceLocation(Mineria.MODID, "textures/block/blue_glowstone.png"))
                    .root("root", root -> root
                            .icon(MineriaBlocks.LONSDALEITE_BLOCK)
                            .showToast(false)
                            .announceChat(false)
                            .singleCriterion(InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.CRAFTING_TABLE))
                            .children(b0 -> {
                                b0.create("mine_mineria_ore")
                                        .icon(MineriaBlocks.LEAD_ORE)
                                        .criteria(criteriaBuilder -> criteriaBuilder
                                                .add("mine_lead", MinedBlockTrigger.Instance.minedBlock(MineriaBlocks.Tags.LEAD_ORES))
                                                .or("mine_silver", MinedBlockTrigger.Instance.minedBlock(MineriaBlocks.Tags.SILVER_ORES))
                                                .or("mine_titane", MinedBlockTrigger.Instance.minedBlock(MineriaBlocks.Tags.TITANE_ORES))
                                                .or("mine_lonsdaleite", MinedBlockTrigger.Instance.minedBlock(MineriaBlocks.Tags.LONSDALEITE_ORES))
                                        ).children(b1 -> {
                                            b1.create("make_copper_sword")
                                                    .icon(MineriaItems.COPPER_SWORD)
                                                    .singleCriterion(InventoryChangeTrigger.TriggerInstance.hasItems(MineriaItems.COPPER_SWORD.get()));
                                            b1.create("make_filter")
                                                    .icon(MineriaItems.FILTER)
                                                    .singleCriterion(InventoryChangeTrigger.TriggerInstance.hasItems(MineriaItems.FILTER.get()));
                                            b1.create("smelt_lead")
                                                    .icon(MineriaItems.LEAD_INGOT)
                                                    .singleCriterion(InventoryChangeTrigger.TriggerInstance.hasItems(MineriaItems.LEAD_INGOT.get()))
                                                    .children(b2 -> {
                                                        b2.create("make_compressed_lead")
                                                                .icon(MineriaItems.COMPRESSED_LEAD_INGOT)
                                                                .singleCriterion(InventoryChangeTrigger.TriggerInstance.hasItems(MineriaItems.COMPRESSED_LEAD_INGOT.get()))
                                                                .children(b3 ->
                                                                        b3.create("make_compressed_lead_sword")
                                                                                .icon(MineriaItems.COMPRESSED_LEAD_SWORD)
                                                                                .frame(FrameType.GOAL)
                                                                                .singleCriterion(InventoryChangeTrigger.TriggerInstance.hasItems(MineriaItems.COMPRESSED_LEAD_SWORD.get()))
                                                                );
                                                        b2.create("make_lead_sword")
                                                                .icon(MineriaItems.LEAD_SWORD)
                                                                .frame(FrameType.GOAL)
                                                                .singleCriterion(InventoryChangeTrigger.TriggerInstance.hasItems(MineriaItems.LEAD_SWORD.get()));
                                                        b2.create("damage_from_spike")
                                                                .icon(MineriaBlocks.LEAD_SPIKE)
                                                                .frame(FrameType.GOAL)
                                                                .singleCriterion(DamageFromSpikeTrigger.Instance.ANY);
                                                    });
                                            b1.create("smelt_silver")
                                                    .icon(MineriaItems.SILVER_INGOT)
                                                    .singleCriterion(InventoryChangeTrigger.TriggerInstance.hasItems(MineriaItems.SILVER_INGOT.get()))
                                                    .children(b2 ->
                                                            b2.create("eat_silver_apple")
                                                                    .icon(MineriaItems.SILVER_APPLE)
                                                                    .singleCriterion(ConsumeItemTrigger.TriggerInstance.usedItem(MineriaItems.SILVER_APPLE.get()))
                                                    );
                                            b1.create("smelt_titane")
                                                    .icon(MineriaItems.TITANE_INGOT)
                                                    .singleCriterion(InventoryChangeTrigger.TriggerInstance.hasItems(MineriaItems.TITANE_INGOT.get()))
                                                    .children(b2 -> {
                                                        b2.create("make_vanadium")
                                                                .icon(MineriaItems.VANADIUM_INGOT)
                                                                .singleCriterion(InventoryChangeTrigger.TriggerInstance.hasItems(MineriaItems.VANADIUM_INGOT.get()))
                                                                .children(b3 -> {
                                                                    b3.create("make_vanadium_helmet")
                                                                            .icon(MineriaItems.VANADIUM_HELMET)
                                                                            .singleCriterion(InventoryChangeTrigger.TriggerInstance.hasItems(MineriaItems.VANADIUM_HELMET.get()));
                                                                    b3.create("make_blue_glowstone")
                                                                            .icon(MineriaBlocks.BLUE_GLOWSTONE)
                                                                            .singleCriterion(InventoryChangeTrigger.TriggerInstance.hasItems(MineriaBlocks.BLUE_GLOWSTONE.get()));
                                                                });
                                                        b2.create("make_all_titane_swords")
                                                                .icon(MineriaItems.TITANE_SWORD)
                                                                .frame(FrameType.GOAL)
                                                                .criteria(criteriaBuilder -> criteriaBuilder
                                                                        .add("get_titane_sword", InventoryChangeTrigger.TriggerInstance.hasItems(MineriaItems.TITANE_SWORD.get()))
                                                                        .and("get_titane_sword_with_copper_handle", InventoryChangeTrigger.TriggerInstance.hasItems(MineriaItems.TITANE_SWORD_WITH_COPPER_HANDLE.get()))
                                                                        .and("get_titane_sword_with_iron_handle", InventoryChangeTrigger.TriggerInstance.hasItems(MineriaItems.TITANE_SWORD_WITH_IRON_HANDLE.get()))
                                                                        .and("get_titane_sword_with_gold_handle", InventoryChangeTrigger.TriggerInstance.hasItems(MineriaItems.TITANE_SWORD_WITH_GOLD_HANDLE.get()))
                                                                        .and("get_titane_sword_with_silver_handle", InventoryChangeTrigger.TriggerInstance.hasItems(MineriaItems.TITANE_SWORD_WITH_SILVER_HANDLE.get()))
                                                                );
                                                        b2.create("make_titane_armor")
                                                                .icon(MineriaItems.TITANE_CHESTPLATE)
                                                                .frame(FrameType.CHALLENGE)
                                                                .criteria(criteriaBuilder -> criteriaBuilder
                                                                        .add("get_titane_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(MineriaItems.TITANE_HELMET.get()))
                                                                        .and("get_titane_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(MineriaItems.TITANE_CHESTPLATE.get()))
                                                                        .and("get_titane_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(MineriaItems.TITANE_LEGGINGS.get()))
                                                                        .and("get_titane_boots", InventoryChangeTrigger.TriggerInstance.hasItems(MineriaItems.TITANE_BOOTS.get()))
                                                                )
                                                                .rewards(rewards -> rewards.addExperience(70));
                                                    });
                                            b1.create("mine_lonsdaleite")
                                                    .icon(MineriaBlocks.LONSDALEITE_ORE)
                                                    .frame(FrameType.GOAL)
                                                    .singleCriterion(MinedBlockTrigger.Instance.minedBlock(MineriaBlocks.Tags.LONSDALEITE_ORES));
                                        });

                                b0.create("make_apothecarium")
                                        .icon(MineriaItems.APOTHECARIUM)
                                        .singleCriterion(InventoryChangeTrigger.TriggerInstance.hasItems(MineriaItems.APOTHECARIUM.get()))
                                        .children(b1 -> {
                                            b1.create("mine_mineria_plant")
                                                    .icon(MineriaBlocks.PLANTAIN)
                                                    .singleCriterion(MinedBlockTrigger.Instance.minedBlock(MineriaBlocks.Tags.PLANTS))
                                                    .children(b2 -> {
                                                        b2.create("make_cup")
                                                                .icon(MineriaItems.CUP)
                                                                .singleCriterion(InventoryChangeTrigger.TriggerInstance.hasItems(MineriaItems.CUP.get()))
                                                                .children(b3 ->
                                                                        b3.create("put_poison_in_jar")
                                                                                .icon(JarItem.addPoisonSourceToStack(new ItemStack(MineriaItems.JAR.get()), PoisonSource.ELDERBERRY))
                                                                                .singleCriterion(UsedApothecaryTableTrigger.Instance.filledItem(MineriaItems.JAR.get()))
                                                                );
                                                        b2.create("make_infuser")
                                                                .icon(MineriaBlocks.INFUSER)
                                                                .singleCriterion(InventoryChangeTrigger.TriggerInstance.hasItems(MineriaBlocks.INFUSER.get()))
                                                                .children(b3 -> {
                                                                    b3.create("make_distiller")
                                                                            .icon(MineriaBlocks.DISTILLER)
                                                                            .singleCriterion(InventoryChangeTrigger.TriggerInstance.hasItems(MineriaBlocks.DISTILLER.get()))
                                                                            .children(b4 ->
                                                                                    b4.create("make_julep")
                                                                                            .icon(MineriaItems.JULEP)
                                                                                            .singleCriterion(InventoryChangeTrigger.TriggerInstance.hasItems(MineriaItems.JULEP.get()))
                                                                                            .children(b5 -> {
                                                                                                b5.create("make_anti_poison")
                                                                                                        .icon(MineriaItems.ANTI_POISON)
                                                                                                        .singleCriterion(InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(MineriaItems.Tags.ANTI_POISONS).build()));
                                                                                                b5.create("make_all_anti_poison")
                                                                                                        .icon(MineriaItems.MIRACLE_ANTI_POISON)
                                                                                                        .frame(FrameType.CHALLENGE)
                                                                                                        .criteria(criteriaBuilder -> criteriaBuilder
                                                                                                                .add("get_catholicon", InventoryChangeTrigger.TriggerInstance.hasItems(MineriaItems.CATHOLICON.get()))
                                                                                                                .and("get_charcoal_anti_poison", InventoryChangeTrigger.TriggerInstance.hasItems(MineriaItems.CHARCOAL_ANTI_POISON.get()))
                                                                                                                .and("get_milk_anti_poison", InventoryChangeTrigger.TriggerInstance.hasItems(MineriaItems.MILK_ANTI_POISON.get()))
                                                                                                                .and("get_nauseous_anti_poison", InventoryChangeTrigger.TriggerInstance.hasItems(MineriaItems.NAUSEOUS_ANTI_POISON.get()))
                                                                                                                .and("get_anti_poison", InventoryChangeTrigger.TriggerInstance.hasItems(MineriaItems.ANTI_POISON.get()))
                                                                                                                .and("get_miracle_anti_poison", InventoryChangeTrigger.TriggerInstance.hasItems(MineriaItems.MIRACLE_ANTI_POISON.get()))
                                                                                                        );
                                                                                            })
                                                                            );
                                                                    b3.create("make_all_teas")
                                                                            .icon(Util.make(new ItemStack(MineriaBlocks.DEBUG_BLOCK.get()), stack -> stack.getOrCreateTag().putInt("CustomModelData", 2)))
                                                                            .frame(FrameType.CHALLENGE)
                                                                            .criteria(criteriaBuilder -> criteriaBuilder
                                                                                    .add("get_plantain_tea", InventoryChangeTrigger.TriggerInstance.hasItems(MineriaItems.PLANTAIN_TEA.get()))
                                                                                    .and("get_mint_tea", InventoryChangeTrigger.TriggerInstance.hasItems(MineriaItems.MINT_TEA.get()))
                                                                                    .and("get_thyme_tea", InventoryChangeTrigger.TriggerInstance.hasItems(MineriaItems.THYME_TEA.get()))
                                                                                    .and("get_nettle_tea", InventoryChangeTrigger.TriggerInstance.hasItems(MineriaItems.NETTLE_TEA.get()))
                                                                                    .and("get_pulmonary_tea", InventoryChangeTrigger.TriggerInstance.hasItems(MineriaItems.PULMONARY_TEA.get()))
                                                                                    .and("get_rhubarb_tea", InventoryChangeTrigger.TriggerInstance.hasItems(MineriaItems.RHUBARB_TEA.get()))
                                                                                    .and("get_senna_tea", InventoryChangeTrigger.TriggerInstance.hasItems(MineriaItems.SENNA_TEA.get()))
                                                                                    .and("get_black_elderberry_tea", InventoryChangeTrigger.TriggerInstance.hasItems(MineriaItems.BLACK_ELDERBERRY_TEA.get()))
                                                                                    .and("get_elderberry_tea", InventoryChangeTrigger.TriggerInstance.hasItems(MineriaItems.ELDERBERRY_TEA.get()))
                                                                                    .and("get_strychnos_toxifera_tea", InventoryChangeTrigger.TriggerInstance.hasItems(MineriaItems.STRYCHNOS_TOXIFERA_TEA.get()))
                                                                                    .and("get_strychnos_nux_vomica_tea", InventoryChangeTrigger.TriggerInstance.hasItems(MineriaItems.STRYCHNOS_NUX_VOMICA_TEA.get()))
                                                                                    .and("get_belladonna_tea", InventoryChangeTrigger.TriggerInstance.hasItems(MineriaItems.BELLADONNA_TEA.get()))
                                                                                    .and("get_mandrake_tea", InventoryChangeTrigger.TriggerInstance.hasItems(MineriaItems.MANDRAKE_TEA.get()))
                                                                                    .and("get_mandrake_root_tea", InventoryChangeTrigger.TriggerInstance.hasItems(MineriaItems.MANDRAKE_ROOT_TEA.get()))
                                                                                    .and("get_goji_tea", InventoryChangeTrigger.TriggerInstance.hasItems(MineriaItems.GOJI_TEA.get()))
                                                                                    .and("get_saussurea_costus_root_tea", InventoryChangeTrigger.TriggerInstance.hasItems(MineriaItems.SAUSSUREA_COSTUS_ROOT_TEA.get()))
                                                                                    .and("get_five_flavor_fruit_tea", InventoryChangeTrigger.TriggerInstance.hasItems(MineriaItems.FIVE_FLAVOR_FRUIT_TEA.get()))
                                                                                    .and("get_pulsatilla_chinensis_root_tea", InventoryChangeTrigger.TriggerInstance.hasItems(MineriaItems.PULSATILLA_CHINENSIS_ROOT_TEA.get()))
                                                                            );
                                                                    b3.create("make_poisonous_tea")
                                                                            .icon(MineriaItems.ELDERBERRY_TEA)
                                                                            .singleCriterion(InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(MineriaItems.Tags.POISONOUS_TEAS).build()))
                                                                            .children(b4 ->
                                                                                    b4.create("drink_poisonous_tea")
                                                                                            .icon(MineriaItems.STRYCHNOS_NUX_VOMICA_TEA)
                                                                                            .frame(FrameType.GOAL)
                                                                                            .singleCriterion(ConsumeItemTrigger.TriggerInstance.usedItem(ItemPredicate.Builder.item().of(MineriaItems.Tags.POISONOUS_TEAS).build()))
                                                                                            .children(b5 ->
                                                                                                    b5.create("drink_all_poisonous_tea")
                                                                                                            .icon(Util.make(new ItemStack(MineriaBlocks.DEBUG_BLOCK.get()), stack -> stack.getOrCreateTag().putInt("CustomModelData", 1)))
                                                                                                            .frame(FrameType.CHALLENGE)
                                                                                                            .criteria(criteriaBuilder -> criteriaBuilder
                                                                                                                    .add("drink_elderberry_tea", ConsumeItemTrigger.TriggerInstance.usedItem(MineriaItems.ELDERBERRY_TEA.get()))
                                                                                                                    .and("drink_strychnos_toxifera_tea", ConsumeItemTrigger.TriggerInstance.usedItem(MineriaItems.STRYCHNOS_TOXIFERA_TEA.get()))
                                                                                                                    .and("drink_strychnos_nux-vomica_tea", ConsumeItemTrigger.TriggerInstance.usedItem(MineriaItems.STRYCHNOS_NUX_VOMICA_TEA.get()))
                                                                                                                    .and("drink_belladonna_tea", ConsumeItemTrigger.TriggerInstance.usedItem(MineriaItems.BELLADONNA_TEA.get()))
                                                                                                                    .and("drink_mandrake_tea", ConsumeItemTrigger.TriggerInstance.usedItem(MineriaItems.MANDRAKE_TEA.get()))
                                                                                                            )
                                                                                            )
                                                                            );
                                                                });
                                                        b2.create("make_syrup")
                                                                .icon(MineriaItems.SYRUP)
                                                                .singleCriterion(InventoryChangeTrigger.TriggerInstance.hasItems(MineriaItems.SYRUP.get()));
                                                        b2.create("make_bamboo_blowgun")
                                                                .icon(MineriaItems.BAMBOO_BLOWGUN)
                                                                .singleCriterion(InventoryChangeTrigger.TriggerInstance.hasItems(MineriaItems.BAMBOO_BLOWGUN.get()))
                                                                .children(b3 ->
                                                                        b3.create("shoot_poisonous_dart")
                                                                                .icon(MineriaItems.BLOWGUN_REFILL)
                                                                                .criteria(criteriaBuilder -> criteriaBuilder
                                                                                        .add("shot_elderberry_dart", ShotBlowgunTrigger.Instance.poisonousAmmo(PoisonSource.ELDERBERRY))
                                                                                        .or("shot_strychnos_toxifera_dart", ShotBlowgunTrigger.Instance.poisonousAmmo(PoisonSource.STRYCHNOS_TOXIFERA))
                                                                                        .or("shot_strychnos_nux_vomica_dart", ShotBlowgunTrigger.Instance.poisonousAmmo(PoisonSource.STRYCHNOS_NUX_VOMICA))
                                                                                        .or("shot_belladonna_dart", ShotBlowgunTrigger.Instance.poisonousAmmo(PoisonSource.BELLADONNA))
                                                                                        .or("shot_mandrake_dart", ShotBlowgunTrigger.Instance.poisonousAmmo(PoisonSource.MANDRAKE))
                                                                                )
                                                                );
                                                        b2.create("use_scalpel")
                                                                .icon(MineriaItems.SCALPEL)
                                                                .singleCriterion(UsedScalpelTrigger.Instance.ANY);
                                                    });
                                            b1.create("trade_apothecary")
                                                    .icon(MineriaItem.withCustomModelData(MineriaBlocks.DEBUG_BLOCK, 3))
                                                    .singleCriterion(tradedWithVillager(EntityPredicate.Builder.entity().of(EntityType.VILLAGER).nbt(new NbtPredicate(MineriaUtils.parseTag("{VillagerData:{profession:\"%s\"}}", MineriaProfessions.APOTHECARY.getId()))).build()))
                                                    .children(b2 -> {
                                                        b2.create("get_unique_ingredients")
                                                                .icon(MineriaItems.GUM_ARABIC_JAR)
                                                                .criteria(criteriaBuilder -> criteriaBuilder
                                                                        .add("get_orange-blossom", InventoryChangeTrigger.TriggerInstance.hasItems(MineriaItems.ORANGE_BLOSSOM.get()))
                                                                        .and("get_gum_arabic_jar", InventoryChangeTrigger.TriggerInstance.hasItems(MineriaItems.GUM_ARABIC_JAR.get()))
                                                                        .and("get_cinnamon_dust", InventoryChangeTrigger.TriggerInstance.hasItems(MineriaItems.CINNAMON_DUST.get()))
                                                                        .and("get_ginger", InventoryChangeTrigger.TriggerInstance.hasItems(MineriaItems.GINGER.get()))
                                                                );
                                                        b2.create("drink_miracle_anti_poison")
                                                                .icon(MineriaItems.MIRACLE_ANTI_POISON)
                                                                .frame(FrameType.GOAL)
                                                                .singleCriterion(ConsumeItemTrigger.TriggerInstance.usedItem(MineriaItems.MIRACLE_ANTI_POISON.get()));
                                                    });
                                        });

                                b0.create("get_mineral_sand")
                                        .icon(MineriaBlocks.MINERAL_SAND)
                                        .singleCriterion(InventoryChangeTrigger.TriggerInstance.hasItems(MineriaBlocks.MINERAL_SAND.get()))
                                        .children(b1 ->
                                                b1.create("make_titane_extractor")
                                                        .icon(MineriaBlocks.TITANE_EXTRACTOR)
                                                        .singleCriterion(InventoryChangeTrigger.TriggerInstance.hasItems(MineriaBlocks.TITANE_EXTRACTOR.get()))
                                                        .children(b2 -> {
                                                            b2.create("fill_fluid_barrel")
                                                                    .icon(MineriaBlocks.WATER_BARREL)
                                                                    .singleCriterion(new FluidBarrelFilledTrigger.Instance(EntityPredicate.Composite.ANY, null, new FluidBarrelCapacityPredicate(-1, -1, true)));
                                                            b2.create("extract_titane")
                                                                    .icon(MineriaItems.TITANE_NUGGET)
                                                                    .singleCriterion(new ExtractedItemTrigger.Instance(EntityPredicate.Composite.ANY, ItemPredicate.Builder.item().of(MineriaItems.TITANE_NUGGET.get()).build()))
                                                                    .children(b3 ->
                                                                            b3.create("craft_titane_with_nuggets")
                                                                                    .icon(MineriaItems.TITANE_INGOT)
                                                                                    .singleCriterion(new ShapedRecipeUsedTrigger.Instance(EntityPredicate.Composite.ANY, ShapedRecipePredicate.builder("nnn", "nnn", "nnn").key("n", Ingredient.of(MineriaItems.TITANE_NUGGET.get())).result(MineriaItems.TITANE_INGOT)))
                                                                    );
                                                            b2.create("make_extractor")
                                                                    .icon(MineriaBlocks.EXTRACTOR)
                                                                    .singleCriterion(InventoryChangeTrigger.TriggerInstance.hasItems(MineriaBlocks.EXTRACTOR.get()))
                                                                    .children(b3 ->
                                                                            b3.create("extract_lonsdaleite")
                                                                                    .icon(MineriaItems.LONSDALEITE)
                                                                                    .frame(FrameType.CHALLENGE)
                                                                                    .singleCriterion(new ExtractedItemTrigger.Instance(EntityPredicate.Composite.ANY, ItemPredicate.Builder.item().of(MineriaItems.LONSDALEITE.get()).build()))
                                                                    );
                                                        })
                                        );

                                b0.create("find_ritual_structure")
                                        .icon(MineriaBlocks.RITUAL_TABLE)
                                        .singleCriterion(PlayerTrigger.TriggerInstance.located(LocationPredicate.inStructure(MineriaStructures.RITUAL_STRUCTURE)))
                                        .children(b1 -> {
                                            b1.create("kill_druid")
                                                    .icon(Items.APPLE)
                                                    .singleCriterion(KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(MineriaEntities.Tags.DRUIDS)))
                                                    .children(b2 -> {
                                                        b2.create("get_mistletoe")
                                                                .icon(MineriaItems.MISTLETOE)
                                                                .singleCriterion(InventoryChangeTrigger.TriggerInstance.hasItems(MineriaItems.MISTLETOE.get()))
                                                                .children(b3 ->
                                                                        b3.create("kill_gdog")
                                                                                .icon(MineriaItems.DRUID_HEART)
                                                                                .frame(FrameType.CHALLENGE)
                                                                                .singleCriterion(KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(MineriaEntities.GREAT_DRUID_OF_GAULS.get())))
                                                                                .rewards(builder -> builder.addExperience(100))
                                                                                .children(b4 -> {
                                                                                    b4.create("drink_vampire_potion")
                                                                                            .icon(PotionUtils.setPotion(new ItemStack(MineriaItems.MINERIA_POTION.get()), MineriaPotions.VAMPIRE.get()))
                                                                                            .frame(FrameType.GOAL)
                                                                                            .criteria(criteriaBuilder -> criteriaBuilder
                                                                                                    .add("drink_vampire", ConsumeItemTrigger.TriggerInstance.usedItem(ItemPredicate.Builder.item().isPotion(MineriaPotions.VAMPIRE.get()).build()))
                                                                                                    .or("drink_long_vampire", ConsumeItemTrigger.TriggerInstance.usedItem(ItemPredicate.Builder.item().isPotion(MineriaPotions.LONG_VAMPIRE.get()).build()))
                                                                                                    .or("drink_strong_vampire", ConsumeItemTrigger.TriggerInstance.usedItem(ItemPredicate.Builder.item().isPotion(MineriaPotions.STRONG_VAMPIRE.get()).build()))
                                                                                                    .or("drink_very_strong_vampire", ConsumeItemTrigger.TriggerInstance.usedItem(ItemPredicate.Builder.item().isPotion(MineriaPotions.VERY_STRONG_VAMPIRE.get()).build()))
                                                                                            );
                                                                                    b4.create("apply_four_elements")
                                                                                            .icon(Items.ENCHANTED_BOOK)
                                                                                            .singleCriterion(new UsedAnvilTrigger.Instance(EntityPredicate.Composite.ANY, ItemPredicate.ANY, ItemPredicate.Builder.item().hasStoredEnchantment(new EnchantmentPredicate(MineriaEnchantments.FOUR_ELEMENTS.get(), MinMaxBounds.Ints.ANY)).build(), ItemPredicate.ANY));
                                                                                })
                                                                );
                                                        b2.create("get_billhook")
                                                                .icon(MineriaItems.BILLHOOK)
                                                                .singleCriterion(InventoryChangeTrigger.TriggerInstance.hasItems(MineriaItems.BILLHOOK.get()));
                                                        b2.create("kill_druid_with_silver_sword")
                                                                .icon(MineriaItems.SILVER_SWORD)
                                                                .singleCriterion(KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(MineriaEntities.Tags.DRUIDS), DamageSourcePredicate.Builder.damageType().source(EntityPredicate.Builder.entity().equipment(EntityEquipmentPredicate.Builder.equipment().mainhand(ItemPredicate.Builder.item().of(MineriaItems.SILVER_SWORD.get()).build()).build()))));
                                                    });
                                            b1.create("trade_druid")
                                                    .icon(MineriaItem.withCustomModelData(MineriaBlocks.DEBUG_BLOCK, 4))
                                                    .singleCriterion(tradedWithVillager(EntityPredicate.Builder.entity().of(MineriaEntities.Tags.DRUIDS).build()))
                                                    .children(b2 ->
                                                            b2.create("obtain_bonus_rewards_druid")
                                                                    .icon(Items.EMERALD)
                                                                    .singleCriterion(ObtainedTradeBonusRewardsTrigger.Instance.obtainedRewardsFrom(EntityPredicate.Builder.entity().of(MineriaEntities.Tags.DRUIDS).build()))
                                                    );
                                        });
                                b0.create("find_pagoda")
                                        .icon(MineriaBlocks.RITUAL_TABLE)
                                        .singleCriterion(PlayerTrigger.TriggerInstance.located(LocationPredicate.inStructure(MineriaStructures.PAGODA)))
                                        .children(b1 ->
                                                b1.create("trade_asiatic_herbalist")
                                                        .icon(MineriaItem.withCustomModelData(MineriaBlocks.DEBUG_BLOCK, 5))
                                                        .singleCriterion(tradedWithVillager(EntityPredicate.Builder.entity().of(MineriaEntities.ASIATIC_HERBALIST.get()).build()))
                                                        .children(b2 ->
                                                                b2.create("get_maxed_kunai")
                                                                        .icon(MineriaItems.KUNAI)
                                                                        .frame(FrameType.GOAL)
                                                                        .singleCriterion(InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(MineriaItems.KUNAI.get()).hasNbt(Util.make(new CompoundTag(), nbt -> nbt.putInt("HitCount", 40))).build()))
                                                        )
                                        );
                            }))
                    .saveAll(saver, helper);
        }

        public static TradeTrigger.TriggerInstance tradedWithVillager(EntityPredicate villagerPredicate) {
            return new TradeTrigger.TriggerInstance(EntityPredicate.Composite.ANY, EntityPredicate.Composite.wrap(villagerPredicate), ItemPredicate.ANY);
        }
    }
}
