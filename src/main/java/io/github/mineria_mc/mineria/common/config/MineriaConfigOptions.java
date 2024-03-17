package io.github.mineria_mc.mineria.common.config;

import io.github.lgatodu47.catconfig.ConfigOption;
import io.github.lgatodu47.catconfig.ConfigOptionAccess;
import io.github.lgatodu47.catconfig.ConfigOptionBuilder;
import io.github.lgatodu47.catconfigmc.MinecraftConfigSides;

public class MineriaConfigOptions {

    private static final ConfigOptionBuilder BUILDER = ConfigOptionBuilder.create();
    public static final ConfigOptionAccess OPTIONS = BUILDER;

    static {
        BUILDER.onSides(MinecraftConfigSides.CLIENT);
    }
    public static final ConfigOption<Boolean> ENABLE_TER_ANIMATIONS = BUILDER.createBool("enable_ter_animations").category("animations").defaultValue(true).make();
}
