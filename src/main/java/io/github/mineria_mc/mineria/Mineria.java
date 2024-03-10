package io.github.mineria_mc.mineria;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.mineria_mc.mineria.common.registries.MineriaBlockRegistry;

public class Mineria implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("Mineria");

	@Override
	public void onInitialize() {
		MineriaBlockRegistry.register();
	}
}