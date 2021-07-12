package com.mineria.mod.util;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;

public class DistSafeRunnable implements DistExecutor.SafeRunnable
{
    private final IEventBus bus;

    public DistSafeRunnable(IEventBus bus)
    {
        this.bus = bus;
    }

    @Override
    public void run()
    {
        bus.addListener(RenderHandler::registerBlockColors);
        bus.addListener(RenderHandler::registerItemColors);
    }
}
