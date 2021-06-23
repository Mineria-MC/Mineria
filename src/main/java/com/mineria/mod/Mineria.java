package com.mineria.mod;

import com.mineria.mod.blocks.barrel.AbstractWaterBarrelBlock;
import com.mineria.mod.init.*;
import com.mineria.mod.util.MineriaPacketHandler;
import com.mineria.mod.util.RenderHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

@Mod(References.MODID)
public class Mineria
{
	//Instance
	public static Mineria instance;

	//Logger
	public static final Logger LOGGER = LogManager.getLogger();

	//CreativeTabs
	public static final ItemGroup MINERIA_GROUP = new MineriaGroup("mineria");

	//Mod Constructor
	public Mineria()
	{
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		modBus.addListener(this::setup);
		modBus.addListener(this::clientSetup);

		//Registries
		RecipeSerializerInit.RECIPE_SERIALIZERS.register(modBus);
		ItemsInit.ITEMS.register(modBus);
		BlocksInit.BLOCK_ITEMS.register(modBus);
		BlocksInit.BLOCKS.register(modBus);
		TileEntitiesInit.TILE_ENTITY_TYPES.register(modBus);
		ContainerTypeInit.CONTAINER_TYPES.register(modBus);
		EntityInit.ENTITY_TYPES.register(modBus);

		modBus.addListener(EntityInit::registerEntityAttributes);

		instance = this;
		MinecraftForge.EVENT_BUS.register(this);
	}

	//To setup some things other than registries
	private void setup(FMLCommonSetupEvent event)
	{
		MineriaPacketHandler.registerNetworkMessagesMessages();
	}

	//To setup client things
	private void clientSetup(FMLClientSetupEvent event)
	{
		RenderHandler.registerScreenFactories();
		RenderHandler.registerBlockRenders();
		RenderHandler.registerEntityRenders();
		RenderHandler.registerItemModelsProperties();
	}

	//To register server-side objects
	@SubscribeEvent
	public void serverStart(FMLServerStartingEvent event)
	{

	}

	//The Mod ItemGroup
	private static class MineriaGroup extends ItemGroup
	{
		public MineriaGroup(String label)
		{
			super(label);
			//To show the field for item search
			setBackgroundImage(new ResourceLocation("textures/gui/container/creative_inventory/tab_item_search.png"));
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

		@Override
		public void fill(NonNullList<ItemStack> items)
		{
			super.fill(items);
			//We're adding the barrels with custom NBT Tags
			items.addAll(ForgeRegistries.ITEMS.getValues().stream()
					.filter(AbstractWaterBarrelBlock.WaterBarrelBlockItem.class::isInstance)
					.map(Item::getDefaultInstance)
					.collect(Collectors.toList()));
		}
	}
}
