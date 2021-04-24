package com.mineria.mod;

import com.mineria.mod.blocks.barrel.AbstractBlockWaterBarrel;
import com.mineria.mod.init.BlocksInit;
import com.mineria.mod.proxy.CommonProxy;
import com.mineria.mod.proxy.ServerProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

@SuppressWarnings("unused")
@Mod(modid = References.MODID, name = References.NAME, version = References.VERSION, acceptedMinecraftVersions = References.MC_VERSION)
public class Mineria
{
	//Logger
	public static final Logger LOGGER = LogManager.getLogger();

	//Instance
	@Instance
	public static Mineria INSTANCE = new Mineria();
	
	//CreativeTabs
	public static final CreativeTabs MINERIA_TAB = new CreativeTab();
	
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
		if(proxy instanceof ServerProxy)
			((ServerProxy)proxy).serverStart(event);
	}

	public static class CreativeTab extends CreativeTabs
	{
		public CreativeTab()
		{
			super("mineria");
			setBackgroundImageName("item_search.png");
		}

		@Override
		public ItemStack getTabIconItem()
		{
			return new ItemStack(BlocksInit.LONSDALEITE_ORE);
		}

		@Override
		public boolean hasSearchBar()
		{
			return true;
		}

		@Override
		public void displayAllRelevantItems(NonNullList<ItemStack> items)
		{
			super.displayAllRelevantItems(items);
			//We're adding the barrels with custom NBT Tags
			items.addAll(ForgeRegistries.ITEMS.getValuesCollection().stream()
					.filter(AbstractBlockWaterBarrel.ItemBlockBarrel.class::isInstance)
					.map(Item::getDefaultInstance)
					.collect(Collectors.toList()));
		}
	}
}
