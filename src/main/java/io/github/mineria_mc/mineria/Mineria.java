package io.github.mineria_mc.mineria;

import com.mojang.logging.LogUtils;
import io.github.mineria_mc.mineria.common.MineriaProxy;
import io.github.mineria_mc.mineria.common.capabilities.MineriaCapabilities;
import io.github.mineria_mc.mineria.common.init.*;
import io.github.mineria_mc.mineria.common.items.MineriaItem;
import io.github.mineria_mc.mineria.common.world.terrablender.MineriaTerraBlenderApi;
import io.github.mineria_mc.mineria.data.MineriaDataGatherer;
import io.github.mineria_mc.mineria.network.MineriaPacketHandler;
import io.github.mineria_mc.mineria.server.commands.KiCommand;
import io.github.mineria_mc.mineria.server.commands.PoisonCommand;
import io.github.mineria_mc.mineria.util.MineriaConfig;
import io.github.mineria_mc.mineria.common.init.MineriaCreativeModeTabs;
import io.github.mineria_mc.mineria.util.MineriaProxyProvider;
import io.github.mineria_mc.mineria.util.MineriaMissingMappings;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

/**
 * Main class for Mineria
 */
@Mod(Mineria.MODID)
public class Mineria {
    // Mod id
    public static final String MODID = "mineria";

    // Logger
    public static final Logger LOGGER = LogUtils.getLogger();

    // Instance
    private static Mineria instance;

    // Proxy
    private final MineriaProxy proxy;

    // Mod Constructor
    public Mineria() {
        this.proxy = DistExecutor.safeRunForDist(() -> MineriaProxyProvider.CLIENT, () -> MineriaProxyProvider.SERVER);
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::setup);
        bus.addListener(this::clientSetup);
        bus.addListener(new MineriaDataGatherer());

        // Registries
        MineriaRegistries.init();
        MineriaRecipeTypes.RECIPE_TYPES.register(bus);
        MineriaRecipeSerializers.RECIPE_SERIALIZERS.register(bus);
        MineriaLootItemFunctionTypes.FUNCTION_TYPES.register(bus);
        MineriaLootModifierSerializers.SERIALIZERS.register(bus);
        MineriaBiomeModifierSerializers.BIOME_MODIFIER_SERIALIZERS.register(bus);
        MineriaParticleTypes.PARTICLE_TYPES.register(bus);
        MineriaSounds.SOUNDS.register(bus);
        MineriaEffects.EFFECTS.register(bus);
        MineriaEffectInstanceSerializers.SERIALIZERS.register(bus);
        MineriaEnchantments.ENCHANTMENTS.register(bus);
        MineriaPotions.POTIONS.register(bus);
        MineriaBlocks.BLOCKS.register(bus);
        MineriaBlocks.BLOCK_ITEMS.register(bus);
        MineriaItems.ITEMS.register(bus);
        MineriaBlockEntities.TYPES.register(bus);
        MineriaMenuTypes.MENU_TYPES.register(bus);
        MineriaEntities.ENTITY_TYPES.register(bus);
        MineriaPOITypes.POI_TYPES.register(bus);
        MineriaProfessions.PROFESSIONS.register(bus);
        MineriaCreativeModeTabs.CREATIVE_TABS.register(bus);
        MineriaHeightProviderTypes.PROVIDER_TYPES.register(bus);
        MineriaTreeDecoratorTypes.TREE_DECORATORS.register(bus);
        MineriaDataMarkerHandlers.HANDLERS.register(bus);
        MineriaSPETypes.SPE_TYPES.register(bus);
        bus.addListener(MineriaFeatures::registerFeatures);
        MineriaCriteriaTriggers.init();

        // Config
        MineriaConfig.registerConfigSpecs(ModLoadingContext.get());

        bus.addListener(MineriaEntities::registerEntityAttributes);
        bus.addListener(MineriaEntities::registerSpawnPlacements);
        bus.addListener(MineriaCapabilities::registerCapabilities);
        MineriaMissingMappings.initialize();

        instance = this;
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            if(ModList.get().isLoaded("terrablender")) {
                MineriaTerraBlenderApi.load();
            } else {
                Mineria.LOGGER.warn("TerraBlender seems to be absent; custom biome generation is therefore disabled.");
            }

            MineriaItems.postInit();
            MineriaItem.ItemTier.registerTiers();
            MineriaBrewingRecipes.register();
            MineriaPacketHandler.registerNetworkMessages();
        });
    }

    private void clientSetup(FMLClientSetupEvent event) {
        proxy.clientSetup(event);
    }

    // Command registry
    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        PoisonCommand.register(event.getDispatcher());
        KiCommand.register(event.getDispatcher());
    }

    public static Mineria getInstance() {
        if(instance == null) {
            throw new IllegalStateException("Mineria is not loaded yet!");
        }
        return instance;
    }

    public static MineriaProxy getProxy() {
        return getInstance().proxy;
    }
}
