package io.github.mineria_mc.mineria.util;

import io.github.mineria_mc.mineria.client.ClientProxy;
import io.github.mineria_mc.mineria.common.MineriaProxy;
import io.github.mineria_mc.mineria.server.ServerProxy;
import net.minecraftforge.fml.DistExecutor;

public enum MineriaProxyProvider implements DistExecutor.SafeSupplier<MineriaProxy> {
    CLIENT,
    SERVER;

    @Override
    public MineriaProxy get() {
        return switch (this) {
            case CLIENT -> new ClientProxy();
            case SERVER -> new ServerProxy();
        };
    }
}
