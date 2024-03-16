package io.github.mineria_mc.mineria.client;

import io.github.mineria_mc.mineria.client.registries.MineriaAbstractContainerScreensRegistry;
import io.github.mineria_mc.mineria.client.registries.MineriaItemGroupRegistry;
import net.fabricmc.api.ClientModInitializer;

public class MineriaClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        initClientRegistries();
    }

    private void initClientRegistries() {
        MineriaItemGroupRegistry.register();
        MineriaAbstractContainerScreensRegistry.register();
    }
}
