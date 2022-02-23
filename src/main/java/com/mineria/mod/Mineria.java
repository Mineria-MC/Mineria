package com.mineria.mod;

import com.mineria.mod.client.ClientProxy;
import com.mineria.mod.common.CommonProxy;
import com.mineria.mod.common.capabilities.CapabilityRegistry;
import com.mineria.mod.common.init.*;
import com.mineria.mod.network.MineriaPacketHandler;
import com.mineria.mod.server.ServerProxy;
import com.mineria.mod.server.commands.PoisonCommand;
import com.mineria.mod.util.MineriaConfig;
import com.mineria.mod.util.RenderHandler;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmlserverevents.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

/**
 * Main class for Mineria
 */
@Mod(Mineria.MODID)
public class Mineria
{
	// Mod id
    public static final String MODID = "mineria";

    // Instance
	public static Mineria instance;

	// Proxy
	public static CommonProxy proxy;

	// Logger
	public static final Logger LOGGER = LogManager.getLogger();

	// CreativeTabs
	public static final CreativeModeTab MINERIA_GROUP = new MineriaGroup("mineria", () -> new ItemStack(MineriaBlocks.LONSDALEITE_ORE));
	public static final CreativeModeTab APOTHECARY_GROUP = new MineriaGroup("apothecary", () -> new ItemStack(MineriaBlocks.APOTHECARY_TABLE));

	/**
	 * TODOS
	 * TODO Fix loot tables
	 * TODO Fix capabilities
	 */

	// Mod Constructor
	public Mineria()
	{
		proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> ServerProxy::new);
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		modBus.addListener(this::setup);
		modBus.addListener(this::clientSetup);

		// Registries
		MineriaRecipeSerializers.RECIPE_SERIALIZERS.register(modBus);
		MineriaLootModifierSerializers.SERIALIZERS.register(modBus);
		MineriaParticleTypes.PARTICLE_TYPES.register(modBus);
		MineriaSounds.SOUNDS.register(modBus);
		MineriaEffects.EFFECTS.register(modBus);
		MineriaEffectInstanceSerializers.SERIALIZERS.register(modBus);
		MineriaEnchantments.ENCHANTMENTS.register(modBus);
		MineriaPotions.POTIONS.register(modBus);
		MineriaItems.ITEMS.register(modBus);
		MineriaBlocks.BLOCK_ITEMS.register(modBus);
		MineriaBlocks.BLOCKS.register(modBus);
		MineriaTileEntities.TILE_ENTITY_TYPES.register(modBus);
		MineriaContainerTypes.CONTAINER_TYPES.register(modBus);
		MineriaEntities.ENTITY_TYPES.register(modBus);
		MineriaPOITypes.POI_TYPES.register(modBus);
		MineriaProfessions.PROFESSIONS.register(modBus);
		MineriaTreeDecoratorTypes.TREE_DECORATORS.register(modBus);
		modBus.addGenericListener(Feature.class, MineriaFeatures::registerFeatures);
//		MineriaStructures.STRUCTURES.register(modBus);
		modBus.addGenericListener(Biome.class, MineriaBiomes::registerBiomes);
		MineriaCriteriaTriggers.init();

		// Config
		MineriaConfig.registerConfigSpecs(ModLoadingContext.get());

		modBus.addListener(MineriaEntities::registerEntityAttributes);

		instance = this;
		MinecraftForge.EVENT_BUS.register(this);
	}

	// To set up some things other than registries
	private void setup(FMLCommonSetupEvent event)
	{
		MineriaPacketHandler.registerNetworkMessages();
		CapabilityRegistry.registerCapabilities();
		MineriaEntities.registerSpawnPlacements();
//		MineriaStructures.setupStructures();
//		MineriaStructures.Configured.registerConfiguredStructures();
		MineriaBrewingRecipes.register();
	}

	// To set up client things
	private void clientSetup(FMLClientSetupEvent event)
	{
		RenderHandler.registerScreenFactories();
		RenderHandler.registerBlockRenders();
		RenderHandler.registerItemModelsProperties();
	}

	// To register server-side objects
	@SubscribeEvent
	public void serverStart(FMLServerStartingEvent event)
	{
		PoisonCommand.register(event.getServer().getCommands().getDispatcher());
	}

	// The Mod ItemGroup
	private static class MineriaGroup extends CreativeModeTab
	{
		private final Supplier<ItemStack> icon;

		public MineriaGroup(String label, Supplier<ItemStack> icon)
		{
			super(label);
			this.icon = icon;
		}

		@Override
		public ItemStack makeIcon()
		{
			return icon.get();
		}

		@Override
		public void fillItemList(NonNullList<ItemStack> items)
		{
			super.fillItemList(items);
			// We are sorting here the items to make the block-items appear first (cleaner look)
			items.sort((o1, o2) -> {
				boolean o1BlockItem = o1.getItem() instanceof BlockItem;
				boolean o2BlockItem = o2.getItem() instanceof BlockItem;
				return o1BlockItem == o2BlockItem ? 0 : (o2BlockItem ? 1 : -1);
			});
		}
	}
}
