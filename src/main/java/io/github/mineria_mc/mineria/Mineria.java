package io.github.mineria_mc.mineria;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.mineria_mc.mineria.common.registries.MineriaBlockEntityRegistry;
import io.github.mineria_mc.mineria.common.registries.MineriaBlockRegistry;
import io.github.mineria_mc.mineria.common.registries.MineriaItemRegistry;
import io.github.mineria_mc.mineria.common.registries.MineriaMenuTypesRegistry;

public class Mineria implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("Mineria");

	@Override
	public void onInitialize() {
		initRegisteries();
	}

	private void initRegisteries() {
		MineriaBlockRegistry.register();
		MineriaBlockEntityRegistry.register();
		MineriaItemRegistry.register();
		MineriaMenuTypesRegistry.register();
	}
}