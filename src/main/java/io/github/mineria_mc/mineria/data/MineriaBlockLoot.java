package io.github.mineria_mc.mineria.data;

import io.github.mineria_mc.mineria.common.blocks.RiceBlock;
import io.github.mineria_mc.mineria.common.init.MineriaItems;
import io.github.mineria_mc.mineria.util.DeferredRegisterUtil;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.function.UnaryOperator;

import static io.github.mineria_mc.mineria.common.init.MineriaBlocks.*;

public class MineriaBlockLoot extends BlockLootSubProvider {
    private static final LootItemCondition.Builder HAS_SHEARS_OR_SILK_TOUCH = HAS_SHEARS.or(HAS_SILK_TOUCH);
    private static final LootItemCondition.Builder HAS_NO_SHEARS_OR_SILK_TOUCH = HAS_SHEARS_OR_SILK_TOUCH.invert();

    public MineriaBlockLoot() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Nonnull
    @Override
    protected Iterable<Block> getKnownBlocks() {
        return DeferredRegisterUtil.presentEntries(BLOCKS).toList();
    }

    @Override
    protected void generate() {
        dropSelf(
                LEAD_ORE,
                TITANE_ORE,
                NETHER_GOLD_ORE,
                SILVER_ORE,

                DEEPSLATE_LEAD_ORE,
                DEEPSLATE_TITANE_ORE,
                DEEPSLATE_SILVER_ORE,

                LEAD_BLOCK,
                TITANE_BLOCK,
                LONSDALEITE_BLOCK,
                SILVER_BLOCK,
                COMPRESSED_LEAD_BLOCK,

                XP_BLOCK,
                TITANE_EXTRACTOR,
                EXTRACTOR,
                INFUSER,
                DISTILLER,
                APOTHECARY_TABLE,

                PLANTAIN,
                MINT,
                THYME,
                NETTLE,
                PULMONARY,
                RHUBARB,
                SENNA,
                BELLADONNA,

                GIROLLE,
                HORN_OF_PLENTY,
                PUFFBALL,

                SPRUCE_YEW_SAPLING,
                SAKURA_SAPLING,

                BLUE_GLOWSTONE,
                MINERAL_SAND,
                LEAD_SPIKE,
                COMPRESSED_LEAD_SPIKE,
                XP_WALL,
                TNT_BARREL
        );
        dropOther(MUDDY_FARMLAND.get(), Blocks.MUD);
        add(RICE.get(), applyExplosionDecay(RICE.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool().add(LootItem.lootTableItem(MineriaItems.RICE_PLANTS.get())))
                .withPool(LootPool.lootPool()
                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(RICE.get()).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(RiceBlock.AGE, 7)))
                        .add(LootItem.lootTableItem(MineriaItems.RICE_PLANTS.get())
                                .apply(ApplyBonusCount.addBonusBinomialDistributionCount(Enchantments.BLOCK_FORTUNE, 0.5714286F, 3))))));
        dropLonsdaleiteOre(LONSDALEITE_ORE);
        dropLonsdaleiteOre(DEEPSLATE_LONSDALEITE_ORE);
        dropWithShears(SENNA_BUSH);
        dropWithShears(ELDERBERRY_BUSH);
        dropWithShears(BLACK_ELDERBERRY_BUSH);
        dropWithShears(STRYCHNOS_TOXIFERA);
        dropWithShears(STRYCHNOS_NUX_VOMICA);
        dropWithShears(LYCIUM_CHINENSE);
        add(SCHISANDRA_CHINENSIS.get(), createShearsDispatchTable(SCHISANDRA_CHINENSIS.get(), applyExplosionCondition(SCHISANDRA_CHINENSIS.get(),
                LootItem.lootTableItem(MineriaItems.FIVE_FLAVOR_FRUIT.get())
                        .when(HAS_SHEARS.invert())
                        .when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, 0.12f, 0.2f, 0.30f, 0.44f, 0.62f))
        )));
        dropWithRoots(MANDRAKE, MineriaItems.MANDRAKE_ROOT, false);
        dropWithRoots(SAUSSUREA_COSTUS, MineriaItems.SAUSSUREA_COSTUS_ROOT, true);
        dropWithRoots(PULSATILLA_CHINENSIS, MineriaItems.PULSATILLA_CHINENSIS_ROOT, false);
        add(SPRUCE_YEW_LEAVES.get(),
                createLeavesDrops(SPRUCE_YEW_LEAVES.get(), SPRUCE_YEW_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES)
                        .withPool(LootPool.lootPool().when(HAS_NO_SHEARS_OR_SILK_TOUCH).add(
                                applyExplosionDecay(SPRUCE_YEW_LEAVES.get(), LootItem.lootTableItem(MineriaItems.YEW_BERRIES.get())
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))
                                        .when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, 0.01F, 0.01111112F, 0.0125F, 0.016666666F, 0.05F)))
                        ))
        );
        add(SAKURA_LEAVES.get(), createLeavesDrops(SAKURA_LEAVES.get(), SAKURA_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));
        dropPottedContents(
                POTTED_PLANTAIN,
                POTTED_MINT,
                POTTED_THYME,
                POTTED_NETTLE,
                POTTED_PULMONARY,
                POTTED_RHUBARB,
                POTTED_SENNA,
                POTTED_BELLADONNA,
                POTTED_MANDRAKE,
                POTTED_PULSATILLA_CHINENSIS,
                POTTED_SPRUCE_YEW_SAPLING,
                POTTED_SAKURA_SAPLING
        );
        dropWhenSilkTouch(GOLDEN_SILVERFISH_NETHERRACK.get());
    }

    @SafeVarargs
    private void dropSelf(RegistryObject<Block>... blocks) {
        for (RegistryObject<Block> block : blocks) {
            dropSelf(block.get());
        }
    }

    private void dropLonsdaleiteOre(RegistryObject<Block> ore) {
        add(ore.get(), createOreDrop(ore.get(), MineriaItems.LONSDALEITE.get()));
    }

    private void dropWithShears(RegistryObject<Block> plant) {
        add(plant.get(), LootTable.lootTable().withPool(
                LootPool.lootPool()
                        .when(ExplosionCondition.survivesExplosion())
                        .add(LootItem.lootTableItem(plant.get()).when(HAS_SHEARS))));
    }

    private void dropWithRoots(RegistryObject<Block> plant, RegistryObject<Item> root, boolean isDouble) {
        LootItemCondition.Builder hasBillhook = MatchTool.toolMatches(ItemPredicate.Builder.item().of(MineriaItems.BILLHOOK.get()));
        UnaryOperator<LootPoolSingletonContainer.Builder<?>> stateCheck = isDouble ? builder ->
                builder.when(new LootItemBlockStatePropertyCondition.Builder(plant.get())
                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER)))
                : UnaryOperator.identity();
        add(plant.get(), LootTable.lootTable().withPool(LootPool.lootPool().add(new AlternativesEntry.Builder(
                stateCheck.apply(LootItem.lootTableItem(plant.get()).when(hasBillhook.invert())),
                stateCheck.apply(LootItem.lootTableItem(root.get()).when(hasBillhook)))
        ).when(ExplosionCondition.survivesExplosion())));
    }

    @SafeVarargs
    private void dropPottedContents(RegistryObject<Block>... pots) {
        for (RegistryObject<Block> pot : pots) {
            dropPottedContents(pot.get());
        }
    }
}
