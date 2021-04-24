package com.mineria.mod.proxy;

import com.mineria.mod.Mineria;
import com.mineria.mod.init.BlocksInit;
import com.mineria.mod.init.EntityInit;
import com.mineria.mod.init.ItemsInit;
import com.mineria.mod.init.RecipesInit;
import com.mineria.mod.init.GameRegistryHandler;
import com.mineria.mod.util.MineriaOreDictionary;
import com.mineria.mod.util.GuiHandler;
import com.mineria.mod.util.MineriaPacketHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommonProxy
{
    public void preInit()
    {
        BlocksInit.init();
        ItemsInit.init();
        GameRegistryHandler.registerTileEntities();
        GameRegistryHandler.registerWorldGenerators();
        EntityInit.registerEntities();
        MineriaPacketHandler.registerNetworkMessagesMessages();
    }

    public void init()
    {
        NetworkRegistry.INSTANCE.registerGuiHandler(Mineria.INSTANCE, new GuiHandler());
        RecipesInit.registerFurnaceRecipes();
        MineriaOreDictionary.registerOres();
    }

    public void postInit()
    {

    }
}
