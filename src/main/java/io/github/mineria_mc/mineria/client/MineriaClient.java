package io.github.mineria_mc.mineria.client;

import io.github.lgatodu47.catconfig.CatConfig;
import io.github.lgatodu47.catconfigmc.MinecraftConfigSides;
import io.github.mineria_mc.mineria.client.registries.MineriaAbstractContainerScreensRegistry;
import io.github.mineria_mc.mineria.client.registries.MineriaBlockEntitiesRendererRegistry;
import io.github.mineria_mc.mineria.client.registries.MineriaItemGroupRegistry;
import io.github.mineria_mc.mineria.common.config.MineriaConfig;
import net.fabricmc.api.ClientModInitializer;

public class MineriaClient implements ClientModInitializer {

    public static CatConfig config;

    @Override
    public void onInitializeClient() {
        config = new MineriaConfig(MinecraftConfigSides.CLIENT);
        initClientRegistries();
    }

    private void initClientRegistries() {
        MineriaItemGroupRegistry.register();
        MineriaAbstractContainerScreensRegistry.register();
        MineriaBlockEntitiesRendererRegistry.register();
    }
}
