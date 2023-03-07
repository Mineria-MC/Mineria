package io.github.mineria_mc.mineria.common.events;

import com.google.common.collect.ImmutableList;
import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.init.MineriaItems;
import io.github.mineria_mc.mineria.common.init.MineriaProfessions;
import io.github.mineria_mc.mineria.common.init.datagen.MineriaStructures;
import io.github.mineria_mc.mineria.util.IngredientItemListing;
import io.github.mineria_mc.mineria.util.MapTrade;
import io.github.mineria_mc.mineria.util.MineriaUtils;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.Util;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SuspiciousStewItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraftforge.common.BasicItemListing;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Mineria.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class VillagerTradesHandler {
    private static final Lazy<Int2ObjectMap<List<VillagerTrades.ItemListing>>> APOTHECARY_TRADES = Lazy.of(() -> Util.make(new Int2ObjectOpenHashMap<>(), map -> {
        map.put(1, ImmutableList.of(
                new IngredientItemListing(Pair.of(Ingredient.of(Tags.Items.MUSHROOMS), 16), new ItemStack(Items.EMERALD), 16, 1, 0.05F),
                new BasicItemListing(new ItemStack(Items.EMERALD, 2), Util.make(new ItemStack(Items.SUSPICIOUS_STEW), stack -> SuspiciousStewItem.saveMobEffect(stack, MineriaUtils.getRandomElement(ForgeRegistries.MOB_EFFECTS.getValues()), 200)), 12, 2, 0.2F),
                new BasicItemListing(new ItemStack(MineriaItems.ELDERBERRY.get(), 3), new ItemStack(MineriaItems.BLACK_ELDERBERRY.get()), 16, 1, 0.05F)
        ));
        map.put(2, ImmutableList.of(
                new IngredientItemListing(Pair.of(Ingredient.of(MineriaItems.Tags.PLANTS), 8), new ItemStack(Items.EMERALD), 16, 5, 0.2F),
                new BasicItemListing(new ItemStack(Items.EMERALD), new ItemStack(MineriaItems.SYRUP.get()), 12, 10, 0.2F)
        ));
        map.put(3, ImmutableList.of(
                new BasicItemListing(new ItemStack(MineriaItems.DISTILLED_ORANGE_BLOSSOM_WATER.get(), 6), new ItemStack(Items.EMERALD, 2), 12, 10, 0.2F),
                new BasicItemListing(new ItemStack(Items.EMERALD, 3), new ItemStack(MineriaItems.ORANGE_BLOSSOM.get()), 12, 15, 0.25F),
                new BasicItemListing(new ItemStack(Items.EMERALD, 3), new ItemStack(MineriaItems.GUM_ARABIC_JAR.get()), 12, 15, 0.25F)
        ));
        map.put(4, ImmutableList.of(
                new BasicItemListing(new ItemStack(Items.EMERALD, 4), new ItemStack(MineriaItems.CINNAMON_DUST.get()), 12, 20, 0.3F),
                new BasicItemListing(new ItemStack(Items.EMERALD, 4), new ItemStack(MineriaItems.GINGER.get()), 12, 20, 0.3F),
                new BasicItemListing(new ItemStack(Items.EMERALD, 4), new ItemStack(MineriaItems.CATHOLICON.get()), 12, 20, 0.2F),
                new BasicItemListing(new ItemStack(Items.EMERALD, 6), new ItemStack(MineriaItems.ANTI_POISON.get()), 12, 30, 0.2F)
        ));
        map.put(5, ImmutableList.of(
                new BasicItemListing(new ItemStack(Items.EMERALD, 8), new ItemStack(MineriaItems.MIRACLE_ANTI_POISON.get()), 12, 30, 0.35F)
        ));
    }));
    public static final Supplier<ImmutableList<VillagerTrades.ItemListing>> ADDITIONAL_CARTOGRAPHER_TRADES = () -> ImmutableList.of(
            new MapTrade(new ItemStack(Items.EMERALD, 17), new ItemStack(Items.COMPASS), MineriaStructures.Tags.ON_RITUAL_STRUCTURE_MAPS, MapDecoration.Type.TARGET_X, 12, 25, 0.2F)
    );

    @SubscribeEvent
    public static void registerTrades(VillagerTradesEvent event) {
        if (event.getType() == MineriaProfessions.APOTHECARY.get()) {
            event.getTrades().putAll(APOTHECARY_TRADES.get());
        }
        if (event.getType() == VillagerProfession.CARTOGRAPHER) {
            event.getTrades().put(5, ADDITIONAL_CARTOGRAPHER_TRADES.get());
        }
    }
}
