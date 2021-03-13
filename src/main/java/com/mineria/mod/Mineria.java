package com.mineria.mod;

import com.mineria.mod.init.*;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(References.MODID)
public class Mineria
{
	//Instance
	public static Mineria instance;
	
	//CreativeTabs
	public static final ItemGroup MINERIA_GROUP = new MineriaGroup("mineria");

	//Mod Constructor
	public Mineria()
	{
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		modBus.addListener(this::setup);
		modBus.addListener(this::clientSetup);

		//Registries
		ItemsInit.ITEMS.register(modBus);
		BlocksInit.BLOCK_ITEMS.register(modBus);
		BlocksInit.BLOCKS.register(modBus);
		TileEntitiesInit.TILE_ENTITY_TYPES.register(modBus);
		ContainerTypeInit.CONTAINER_TYPES.register(modBus);
		EntityInit.ENTITY_TYPES.register(modBus);

		instance = this;
		MinecraftForge.EVENT_BUS.register(this);
	}

	//To setup some things other than registries
	private void setup(FMLCommonSetupEvent event)
	{

	}

	//To setup client things
	private void clientSetup(FMLClientSetupEvent event)
	{

	}

	//To register server-side objects
	@SubscribeEvent
	public void serverStart(FMLServerStartingEvent event)
	{

	}

	private static class MineriaGroup extends ItemGroup
	{
		public MineriaGroup(String label)
		{
			super(label);
			setBackgroundImageName("item_search.png");
		}

		@Override
		public ItemStack createIcon()
		{
			return new ItemStack(BlocksInit.LONSDALEITE_ORE);
		}

		@Override
		public boolean hasSearchBar()
		{
			return true;
		}
	}
}
