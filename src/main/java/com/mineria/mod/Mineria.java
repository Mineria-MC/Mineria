package com.mineria.mod;

import com.mineria.mod.blocks.barrel.AbstractWaterBarrelBlock;
import com.mineria.mod.capabilities.CapabilityRegistry;
import com.mineria.mod.init.*;
import com.mineria.mod.network.MineriaPacketHandler;
import com.mineria.mod.util.RenderHandler;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.structure.Structure;
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
	public static final ItemGroup DEV_GROUP = new ItemGroup("mineria_dev") {
		@Override
		public ItemStack createIcon()
		{
			return new ItemStack(Blocks.COMMAND_BLOCK);
		}
	}.setBackgroundImage(new ResourceLocation("textures/gui/container/creative_inventory/tab_item_search.png"));

	public static final long FOOD_DIGESTION_TIME = 12000;

	//Mod Constructor
	public Mineria()
	{
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		modBus.addListener(this::setup);
		modBus.addListener(this::clientSetup);

		//Registries
		RecipeSerializerInit.RECIPE_SERIALIZERS.register(modBus);
		LootModifierSerializersInit.SERIALIZERS.register(modBus);
		EffectsInit.EFFECTS.register(modBus);
		ItemsInit.ITEMS.register(modBus);
		BlocksInit.BLOCK_ITEMS.register(modBus);
		BlocksInit.BLOCKS.register(modBus);
		TileEntitiesInit.TILE_ENTITY_TYPES.register(modBus);
		ContainerTypeInit.CONTAINER_TYPES.register(modBus);
		EntityInit.ENTITY_TYPES.register(modBus);
		PointOfInterestTypeInit.POI_TYPES.register(modBus);
		ProfessionsInit.PROFESSIONS.register(modBus);
		modBus.addGenericListener(Feature.class, FeaturesInit::registerFeatures);
		modBus.addGenericListener(Structure.class, StructuresInit::registerStructures);
		modBus.addGenericListener(Biome.class, BiomesInit::registerBiomes);

		modBus.addListener(EntityInit::registerEntityAttributes);

		instance = this;
		MinecraftForge.EVENT_BUS.register(this);
	}

	//To setup some things other than registries
	private void setup(FMLCommonSetupEvent event)
	{
		MineriaPacketHandler.registerNetworkMessages();
		CapabilityRegistry.registerCapabilities();
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
