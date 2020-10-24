package com.mineria.mod;

import com.mineria.mod.proxy.ServerProxy;
import com.mineria.mod.tab.MineriaTab;
import com.mineria.mod.util.handler.RegistryHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = References.MODID, name = References.NAME, version = References.VERSION, acceptedMinecraftVersions = References.MC_VERSION)
public class Mineria
{
	//Instance
	@Instance
	public static Mineria instance = new Mineria();
	
	//CreativeTabs
	public static final CreativeTabs mineriaTab = new MineriaTab("mineria").setBackgroundImageName("item_search.png");
	
	//Proxy
	@SidedProxy(clientSide = References.CLIENT_PROXY, serverSide = References.SERVER_PROXY, modId = References.MODID)
	public static ServerProxy proxy;

	//PacketHandler
	public static final SimpleNetworkWrapper PACKET_HANDLER = NetworkRegistry.INSTANCE.newSimpleChannel("mineria");
	
	//Pre-Initialization Event
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		RegistryHandler.preInitRegistries(event);
	}
	
	//Initialization Event
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		proxy.register();
		RegistryHandler.initRegistries(event);
	}
	
	//Post-Initialization Event
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		RegistryHandler.postInitRegistries(event);
	}
	
	//Server Initialization Event
	@EventHandler
	public void serverInit(FMLServerStartingEvent event)
	{
		RegistryHandler.serverRegistries(event);
	}
	
	//Client Pre-Initialization Event
	@EventHandler
	@SideOnly(Side.CLIENT)
	public void clientPreInit(FMLPreInitializationEvent event)
	{
		RegistryHandler.clientRegistries(event);
	}

	private int messageID = 0;

	public <T extends IMessage, V extends IMessage> void addNetworkMessage(Class<? extends IMessageHandler<T, V>> handler, Class<T> messageClass, Side... sides)
	{
		for (Side side : sides)
			Mineria.PACKET_HANDLER.registerMessage(handler, messageClass, messageID, side);
		messageID++;
	}
}
