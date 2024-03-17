package io.github.mineria_mc.mineria.client.compat.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import io.github.lgatodu47.catconfigmc.MinecraftConfigSides;
import io.github.lgatodu47.catconfigmc.screen.ConfigSideSelectionScreen;
import io.github.lgatodu47.catconfigmc.screen.ModConfigScreen;
import io.github.mineria_mc.mineria.client.MineriaClient;
import io.github.mineria_mc.mineria.common.config.MineriaRenderedOptions;
import net.minecraft.network.chat.Component;

public class MineriaModMenuAPI implements ModMenuApi {
    
    private static final ConfigSideSelectionScreen.Builder BUILDER = ConfigSideSelectionScreen.create()
        .with(MinecraftConfigSides.CLIENT, parent -> new ModConfigScreen(Component.literal("Mineria Client Config"), parent, MineriaClient.config, MineriaRenderedOptions.Client.access()));

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return BUILDER::build;
    }
}
