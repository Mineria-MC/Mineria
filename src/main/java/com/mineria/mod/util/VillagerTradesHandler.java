package com.mineria.mod.util;

import com.mineria.mod.References;
import com.mineria.mod.init.ItemsInit;
import com.mineria.mod.init.ProfessionsInit;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SuspiciousStewItem;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.Effects;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Util;
import net.minecraftforge.common.BasicTrade;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

@Mod.EventBusSubscriber(modid = References.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class VillagerTradesHandler
{
    @SubscribeEvent
    public static void registerTrades(VillagerTradesEvent event)
    {
        Int2ObjectMap<List<VillagerTrades.ITrade>> APOTHECARY_TRADES = Util.make(new Int2ObjectOpenHashMap<>(), map -> {
            map.put(1, Util.make(NonNullList.create(), list -> {
                list.add(new IngredientTrade(Pair.of(Ingredient.fromTag(Tags.Items.MUSHROOMS), 16), new ItemStack(Items.EMERALD), 16, 1, 0.05F));
                list.add(new BasicTrade(new ItemStack(Items.EMERALD, 2), Util.make(new ItemStack(Items.SUSPICIOUS_STEW), stack -> SuspiciousStewItem.addEffect(stack, Effects.GLOWING, 200)), 12, 2, 0.2F));
                list.add(new BasicTrade(new ItemStack(ItemsInit.ELDERBERRY, 3), new ItemStack(ItemsInit.BLACK_ELDERBERRY), 16, 1, 0.05F));
            }));
            map.put(2, Util.make(NonNullList.create(), list -> {
                list.add(new IngredientTrade(Pair.of(Ingredient.fromTag(ItemsInit.Tags.PLANTS), 8), new ItemStack(Items.EMERALD), 16, 5, 0.2F));
                    list.add(new BasicTrade(new ItemStack(Items.EMERALD), new ItemStack(ItemsInit.SYRUP), 12, 10, 0.2F));
            }));
            map.put(3, Util.make(NonNullList.create(), list -> {
                list.add(new BasicTrade(new ItemStack(ItemsInit.DISTILLED_ORANGE_BLOSSOM_WATER, 6), new ItemStack(Items.EMERALD, 2), 12, 10, 0.2F));
                list.add(new BasicTrade(new ItemStack(Items.EMERALD, 3), new ItemStack(ItemsInit.ORANGE_BLOSSOM), 12, 15, 0.25F));
                list.add(new BasicTrade(new ItemStack(Items.EMERALD, 3), new ItemStack(ItemsInit.GUM_ARABIC_JAR), 12, 15, 0.25F));
            }));
            map.put(4, Util.make(NonNullList.create(), list -> {
                list.add(new BasicTrade(new ItemStack(Items.EMERALD, 4), new ItemStack(ItemsInit.CINNAMON), 12, 20, 0.3F));
                list.add(new BasicTrade(new ItemStack(Items.EMERALD, 4), new ItemStack(ItemsInit.GINGER), 12, 20, 0.3F));
                list.add(new BasicTrade(new ItemStack(Items.EMERALD, 4), new ItemStack(ItemsInit.CATHOLICON), 12, 20, 0.2F));
                list.add(new BasicTrade(new ItemStack(Items.EMERALD, 6), new ItemStack(ItemsInit.ANTI_POISON), 12, 30, 0.2F));
            }));
            map.put(5, Util.make(NonNullList.create(), list -> {
                list.add(new BasicTrade(new ItemStack(Items.EMERALD, 8), new ItemStack(ItemsInit.MIRACLE_ANTI_POISON), 12, 30, 0.35F));
            }));
        });

        if(event.getType() == ProfessionsInit.APOTHECARY.get())
            event.getTrades().putAll(APOTHECARY_TRADES);
    }
}
