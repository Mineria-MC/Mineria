package io.github.mineria_mc.mineria.util;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.init.MineriaBlocks;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class MineriaCreativeModeTabs {
    private static CreativeModeTab MINERIA, APOTHECARY;

    public static void registerCreativeTabs(CreativeModeTabEvent.Register event) {
        MINERIA = event.registerCreativeModeTab(new ResourceLocation(Mineria.MODID, "mineria"), builder ->
                builder.title(Component.translatable("itemGroup." + Mineria.MODID + ".mineria")).icon(() -> new ItemStack(MineriaBlocks.LONSDALEITE_ORE.get())));
        APOTHECARY = event.registerCreativeModeTab(new ResourceLocation(Mineria.MODID, "apothecary"), List.of(), List.of(MINERIA), builder ->
                builder.title(Component.translatable("itemGroup." + Mineria.MODID + ".apothecary")).icon(() -> new ItemStack(MineriaBlocks.APOTHECARY_TABLE.get())));
    }

    public static void buildTabsContent(CreativeModeTabEvent.BuildContents event) {
        if(event.getTab() == MINERIA) {
            MineriaCreativeTabsContents.MINERIA_TAB_ITEMS.accept(event);
        }
        if(event.getTab() == APOTHECARY) {
            MineriaCreativeTabsContents.APOTHECARY_TAB_ITEMS.accept(event);
        }
        // Weird bug messes everything up in the tab.
        /*if(event.getTab() == CreativeModeTabs.FOOD_AND_DRINKS) {
            MutableHashedLinkedMap<ItemStack, CreativeModeTab.TabVisibility> entries = event.getEntries();
            List<ItemStack> toRemove = new ArrayList<>();
            for (Map.Entry<ItemStack, CreativeModeTab.TabVisibility> entry : entries) {
                ItemStack stack = entry.getKey();
                if(DeferredRegisterUtil.contains(MineriaPotions.POTIONS, PotionUtils.getPotion(stack))) {
                    toRemove.add(stack);
                }
            }
            toRemove.forEach(entries::remove);
        }*/
    }

    public static CreativeModeTab getMineriaTab() {
        return nonNull(MINERIA);
    }

    public static CreativeModeTab getApothecaryTab() {
        return nonNull(APOTHECARY);
    }

    @Nonnull
    private static CreativeModeTab nonNull(@Nullable CreativeModeTab tab) {
        if(tab == null) {
            throw new IllegalStateException("Mineria Creative Mode Tabs are not initialized yet!");
        }
        return tab;
    }
}
