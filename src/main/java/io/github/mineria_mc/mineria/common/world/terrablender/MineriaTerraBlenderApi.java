package io.github.mineria_mc.mineria.common.world.terrablender;

import io.github.mineria_mc.mineria.Mineria;
import terrablender.api.Regions;

public class MineriaTerraBlenderApi {
    public static void load() {
        Regions.register(new MineriaBiomeRegion());

        Mineria.LOGGER.debug("Mineria TerraBlender api successfully initialized.");
    }
}
