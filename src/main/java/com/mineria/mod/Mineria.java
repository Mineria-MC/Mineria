package com.mineria.mod;

import com.mineria.mod.commands.CommandHeal;
import com.mineria.mod.proxy.CommonProxy;
import com.mineria.mod.tab.MineriaTab;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@SuppressWarnings("unused")
@Mod(modid = References.MODID, name = References.NAME, version = References.VERSION, acceptedMinecraftVersions = References.MC_VERSION)
public class Mineria
{
	//Instance
	@Instance
	public static Mineria instance = new Mineria();
	
	//CreativeTabs
	public static final CreativeTabs mineriaTab = new MineriaTab("mineria");
	
	//Proxy
	@SidedProxy(clientSide = References.CLIENT_PROXY, serverSide = References.SERVER_PROXY, modId = References.MODID)
	public static CommonProxy proxy;
	
	//Pre-Initialization Event
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		proxy.preInit();
	}
	
	//Initialization Event
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		proxy.init();
	}
	
	//Post-Initialization Event
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		proxy.postInit();
	}
	
	//Server Starting Event
	@EventHandler
	public void serverStart(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new CommandHeal());
	}
}
