package com.mineria.mod.util.handler;

import com.mineria.mod.Mineria;
import com.mineria.mod.commands.CommandHeal;
import com.mineria.mod.init.BlocksInit;
import com.mineria.mod.init.EntityInit;
import com.mineria.mod.init.ItemsInit;
import com.mineria.mod.init.RecipesInit;
import com.mineria.mod.util.compat.OreDictionaryCompat;
import com.mineria.mod.world.gen.WorldGenCustomOres;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RegistryHandler
{
	public static void preInitRegistries(FMLPreInitializationEvent event)
	{
		BlocksInit.init();
		ItemsInit.init();
		GameRegistry.registerWorldGenerator(new WorldGenCustomOres(), 0);
		EntityInit.registerEntities();
	}
	
	public static void initRegistries(FMLInitializationEvent event)
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(Mineria.instance, new GuiHandler());
		RecipesInit.init();
		OreDictionaryCompat.registerOre();
	}
	
	public static void postInitRegistries(FMLPostInitializationEvent event)
	{
		
	}
	
	public static void serverRegistries(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new CommandHeal());
	}
	
	@SideOnly(Side.CLIENT)
	public static void clientRegistries(FMLPreInitializationEvent event)
	{
		RenderHandler.registerEntityRenders();
	}
}
