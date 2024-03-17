package io.github.mineria_mc.mineria.common.config;

import io.github.lgatodu47.catconfigmc.RenderedConfigOptionAccess;
import io.github.lgatodu47.catconfigmc.RenderedConfigOptionBuilder;

public class MineriaRenderedOptions {
    
    public static final class Client {
        private static final RenderedConfigOptionBuilder BUILDER = new RenderedConfigOptionBuilder();

        static {
            BUILDER.ofBoolean(MineriaConfigOptions.ENABLE_TER_ANIMATIONS).setCommonTranslationKey("enable_ter_animations").build();

            BUILDER.withCategoryTranslationKey("animations", "animations");
        }
        
        public static RenderedConfigOptionAccess access() {
            return BUILDER;
        }
    }
}
