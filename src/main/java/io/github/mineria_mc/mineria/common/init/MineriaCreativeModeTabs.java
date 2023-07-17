package io.github.mineria_mc.mineria.common.init;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.util.MineriaCreativeTabsContents;
import io.github.mineria_mc.mineria.util.MineriaUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class MineriaCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Mineria.MODID);

    public static final RegistryObject<CreativeModeTab> MINERIA = CREATIVE_TABS.register("mineria", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + Mineria.MODID + ".mineria"))
            .icon(MineriaBlocks.LONSDALEITE_ORE.lazyMap(ItemStack::new))
            .displayItems(MineriaCreativeTabsContents.MINERIA_TAB_ITEMS)
            .build());

    public static final RegistryObject<CreativeModeTab> APOTHECARY = CREATIVE_TABS.register("apothecary", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + Mineria.MODID + ".apothecary"))
            .icon(MineriaBlocks.APOTHECARY_TABLE.lazyMap(ItemStack::new))
            .displayItems(MineriaCreativeTabsContents.APOTHECARY_TAB_ITEMS)
            .withTabsBefore(MineriaUtils.key(MINERIA))
            .build());
}
