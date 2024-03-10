package io.github.mineria_mc.mineria.client.registries;

import io.github.mineria_mc.mineria.common.registries.MineriaBlockRegistry;
import io.github.mineria_mc.mineria.util.Constants;
import io.github.mineria_mc.mineria.util.MineriaUtil;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTab.DisplayItemsGenerator;
import net.minecraft.world.level.ItemLike;

import static io.github.mineria_mc.mineria.common.registries.MineriaBlockRegistry.*;

public class MineriaItemGroupRegistry {
    
    private static final DisplayItemsGenerator MINERIA_TAB_ITEMS = (display, output) -> {
        acceptAll(output, 
            // Lead
            LEAD_ORE,
            LEAD_BLOCK,
            COMPRESSED_LEAD_BLOCK,

            // Silver
            SILVER_ORE,
            SILVER_BLOCK,

            // Titane
            TITANE_ORE,
            TITANE_BLOCK,

            // Lonsdaleite
            LONSDALEITE_ORE,
            LONSDALEITE_BLOCK
        );
    };

    private static final CreativeModeTab MINERIA_TAB = FabricItemGroup.builder()
        .icon(() -> new ItemStack(MineriaBlockRegistry.LONSDALEITE_BLOCK))
        .title(MineriaUtil.translatable("itemGroup", "mineria"))
        .displayItems(MINERIA_TAB_ITEMS)
        .build();

    public static void register() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, new ResourceLocation(Constants.MODID), MINERIA_TAB);
    }

    private static void acceptAll(CreativeModeTab.Output output, Object... items) {
        for(Object obj : items) {
            if(obj == null) {
                continue;
            }
            if(obj instanceof ItemLike item) {
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
    }
}
