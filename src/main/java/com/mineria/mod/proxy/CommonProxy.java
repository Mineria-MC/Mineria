package com.mineria.mod.proxy;

import com.mineria.mod.Mineria;
import com.mineria.mod.init.BlocksInit;
import com.mineria.mod.init.EntityInit;
import com.mineria.mod.init.ItemsInit;
import com.mineria.mod.init.RecipesInit;
import com.mineria.mod.util.compat.OreDictionaryCompat;
import com.mineria.mod.util.handler.GuiHandler;
import com.mineria.mod.util.handler.MineriaPacketHandler;
import com.mineria.mod.world.gen.WorldGenCustomOres;
import com.mineria.mod.world.gen.WorldGenCustomPlants;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy
{
    public void preInit()
    {
        BlocksInit.init();
        ItemsInit.init();
        GameRegistry.registerWorldGenerator(new WorldGenCustomOres(), 0);
        GameRegistry.registerWorldGenerator(new WorldGenCustomPlants(), 0);
        EntityInit.registerEntities();
        MineriaPacketHandler.registerNetworkMessagesMessages();
    }

    public void init()
    {
        NetworkRegistry.INSTANCE.registerGuiHandler(Mineria.instance, new GuiHandler());
        RecipesInit.registerFurnaceRecipes();
        OreDictionaryCompat.registerOre();
    }

    public void postInit()
    {

    }
}
