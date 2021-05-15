package com.mineria.mod.proxy;

import com.mineria.mod.util.RenderHandler;

public class ClientProxy extends CommonProxy
{
    @Override
    public void preInit()
    {
        super.preInit();
        RenderHandler.registerEntityRenders();
        RenderHandler.registerTileEntityRenderers();
    }

    @Override
    public void init()
    {
        super.init();
    }

    @Override
    public void postInit()
    {
        super.postInit();
    }
}
