package io.github.mineria_mc.mineria.common.config;

import java.nio.file.Path;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import io.github.lgatodu47.catconfig.CatConfig;
import io.github.lgatodu47.catconfig.CatConfigLogger;
import io.github.lgatodu47.catconfig.ConfigOptionAccess;
import io.github.lgatodu47.catconfig.ConfigSide;
import net.fabricmc.loader.api.FabricLoader;

public class MineriaConfig extends CatConfig {

    private static final Logger LOGGER = LogManager.getLogger("Mineria Config");

    public MineriaConfig(ConfigSide side) {
        super(side, "mineria", CatConfigLogger.delegate(LOGGER));
    }

    @Override
    protected @NotNull ConfigOptionAccess getConfigOptions() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected @NotNull Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }

    @Override
    protected @Nullable ConfigWatcher makeAndStartConfigWatcherThread() {
        ConfigWatcher watcher = new ConfigWatcher("Mineria Config Watcher");
        watcher.start();
        return watcher;
    }
}
