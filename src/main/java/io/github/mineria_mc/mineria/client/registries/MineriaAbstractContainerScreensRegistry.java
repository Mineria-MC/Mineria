package io.github.mineria_mc.mineria.client.registries;

import io.github.mineria_mc.mineria.client.screens.XpBlockScreen;
import io.github.mineria_mc.mineria.common.registries.MineriaMenuTypesRegistry;
import net.minecraft.client.gui.screens.MenuScreens;

public class MineriaAbstractContainerScreensRegistry {

    public static void register() {
        MenuScreens.register(MineriaMenuTypesRegistry.XP_BLOCK, XpBlockScreen::new);
    }
}
