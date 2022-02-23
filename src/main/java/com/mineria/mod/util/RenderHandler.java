package com.mineria.mod.util;

import com.mineria.mod.Mineria;
import com.mineria.mod.client.models.ExtractorGearModel;
import com.mineria.mod.client.models.entity.*;
import com.mineria.mod.client.renderers.ExtractorTileEntityRenderer;
import com.mineria.mod.client.renderers.RitualTableTileEntityRenderer;
import com.mineria.mod.client.renderers.entity.*;
import com.mineria.mod.client.screens.*;
import com.mineria.mod.common.blocks.*;
import com.mineria.mod.common.effects.potions.MineriaPotion;
import com.mineria.mod.common.init.*;
import com.mineria.mod.common.items.JarItem;
import com.mineria.mod.common.items.MineriaBow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemFrameRenderer;
import net.minecraft.client.renderer.entity.LightningBoltRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.level.block.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * A client class where everything related to rendering is registered.
 */
@Mod.EventBusSubscriber(modid = Mineria.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
@OnlyIn(Dist.CLIENT)
public class RenderHandler
{
	public static void registerScreenFactories()
	{
		MenuScreens.register(MineriaContainerTypes.TITANE_EXTRACTOR.get(), TitaneExtractorScreen::new);
		MenuScreens.register(MineriaContainerTypes.INFUSER.get(), InfuserScreen::new);
		MenuScreens.register(MineriaContainerTypes.XP_BLOCK.get(), XpBlockScreen::new);
		MenuScreens.register(MineriaContainerTypes.COPPER_WATER_BARREL.get(), CopperWaterBarrelScreen::new);
		MenuScreens.register(MineriaContainerTypes.GOLDEN_WATER_BARREL.get(), GoldenWaterBarrelScreen::new);
		MenuScreens.register(MineriaContainerTypes.EXTRACTOR.get(), ExtractorScreen::new);
		MenuScreens.register(MineriaContainerTypes.DISTILLER.get(), DistillerScreen::new);
		MenuScreens.register(MineriaContainerTypes.APOTHECARY_TABLE.get(), ApothecaryTableScreen::new);
	}

	@SubscribeEvent
	public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event)
	{
		registerEntityRenders(event);
		registerTileEntityRenderers(event);
	}

	private static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event)
	{
		event.registerEntityRenderer(MineriaEntities.GOLDEN_SILVERFISH.get(), GoldenSilverfishRenderer::new);
		event.registerEntityRenderer(MineriaEntities.WIZARD.get(), WizardRenderer::new);
		event.registerEntityRenderer(MineriaEntities.KUNAI.get(), KunaiRenderer::new);
		event.registerEntityRenderer(MineriaEntities.MINERIA_POTION.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(MineriaEntities.MINERIA_AREA_EFFECT_CLOUD.get(), MineriaAreaEffectCloudRenderer::new);
		event.registerEntityRenderer(MineriaEntities.DRUID.get(), DruidRenderer::new);
		event.registerEntityRenderer(MineriaEntities.OVATE.get(), DruidRenderer::new);
		event.registerEntityRenderer(MineriaEntities.BARD.get(), DruidRenderer::new);
		event.registerEntityRenderer(MineriaEntities.ELEMENTAL_ORB.get(), ElementalOrbRenderer::new);
		event.registerEntityRenderer(MineriaEntities.FIRE_GOLEM.get(), FireGolemRenderer::new);
		event.registerEntityRenderer(MineriaEntities.DIRT_GOLEM.get(), DirtGolemRenderer::new);
		event.registerEntityRenderer(MineriaEntities.AIR_SPIRIT.get(), AirSpiritRenderer::new);
		event.registerEntityRenderer(MineriaEntities.WATER_SPIRIT.get(), WaterSpiritRenderer::new);
		event.registerEntityRenderer(MineriaEntities.DART.get(), BlowgunRefillRenderer::new);
		event.registerEntityRenderer(MineriaEntities.JAR.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(MineriaEntities.DRUIDIC_WOLF.get(), DruidicWolfRenderer::new);
		event.registerEntityRenderer(MineriaEntities.BROWN_BEAR.get(), BrownBearRenderer::new);
		event.registerEntityRenderer(MineriaEntities.GREAT_DRUID_OF_GAULS.get(), GreatDruidOfGaulsRenderer::new);
		event.registerEntityRenderer(MineriaEntities.MINERIA_LIGHTNING_BOLT.get(), LightningBoltRenderer::new);
		event.registerEntityRenderer(MineriaEntities.BUDDHIST.get(), BuddhistRenderer::new);
		event.registerEntityRenderer(MineriaEntities.ASIATIC_HERBALIST.get(), AsiaticHerbalistRenderer::new);
		event.registerEntityRenderer(MineriaEntities.TEMPORARY_ITEM_FRAME.get(), ItemFrameRenderer::new);
	}

	private static void registerTileEntityRenderers(EntityRenderersEvent.RegisterRenderers event)
	{
		event.registerBlockEntityRenderer(MineriaTileEntities.EXTRACTOR.get(), ExtractorTileEntityRenderer::new);
		event.registerBlockEntityRenderer(MineriaTileEntities.RITUAL_TABLE.get(), RitualTableTileEntityRenderer::new);
	}

	@SubscribeEvent
	public static void registerEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions event)
	{
		event.registerLayerDefinition(AsiaticHerbalistRenderer.LAYER, () -> LayerDefinition.create(VillagerModel.createBodyModel(), 64, 64));
		event.registerLayerDefinition(WizardRenderer.LAYER, WitchModel::createBodyLayer);
		event.registerLayerDefinition(KunaiRenderer.LAYER, KunaiModel::createLayerDefinition);
		event.registerLayerDefinition(AirSpiritRenderer.LAYER, AirSpiritModel::createBody);
		event.registerLayerDefinition(BlowgunRefillRenderer.LAYER, BlowgunRefillModel::createLayerDefinition);
		event.registerLayerDefinition(BrownBearRenderer.LAYER, PolarBearModel::createBodyLayer);
		event.registerLayerDefinition(WaterSpiritRenderer.LAYER, WaterSpiritModel::createBodyLayer);
		event.registerLayerDefinition(DirtGolemRenderer.LAYER, DirtGolemModel::createBodyLayer);
		event.registerLayerDefinition(FireGolemRenderer.LAYER, IronGolemModel::createBodyLayer);
		event.registerLayerDefinition(DruidicWolfRenderer.LAYER, WolfModel::createBodyLayer);
		event.registerLayerDefinition(DruidRenderer.LAYER, IllagerModel::createBodyLayer);
		event.registerLayerDefinition(GreatDruidOfGaulsRenderer.LAYER, IllagerModel::createBodyLayer);
		event.registerLayerDefinition(BuddhistRenderer.LAYER, () -> LayerDefinition.create(VillagerModel.createBodyModel(), 64, 64));

		event.registerLayerDefinition(ExtractorTileEntityRenderer.LAYER, ExtractorGearModel::createLayerDefinition);
	}

	/**
	 * Event for registering particle types. Unused.
	 * @param event the forge event called when these need to register.
	 */
	@SubscribeEvent
	public static void registerParticles(ParticleFactoryRegisterEvent event)
	{
		ParticleEngine manager = Minecraft.getInstance().particleEngine;
	}

	public static void registerItemModelsProperties()
	{
		// Bow items
		DeferredRegisterUtil.filterEntriesFromRegister(MineriaItems.ITEMS, MineriaBow.class).forEach(item -> {
			ItemProperties.register(item, new ResourceLocation("pull"), (stack, world, living, entityId) -> {
				if (living == null)
					return 0.0F;
				else
					return living.getUseItem() != stack ? 0.0F : (float)(stack.getUseDuration() - living.getUseItemRemainingTicks()) / 20.0F;
			});
			ItemProperties.register(item, new ResourceLocation("pulling"),
					(stack, world, living, entityId) -> living != null && living.isUsingItem() && living.getUseItem() == stack ? 1.0F : 0.0F);
		});

		// Golden Water Barrel
		ItemProperties.register(MineriaBlocks.getItemFromBlock(MineriaBlocks.GOLDEN_WATER_BARREL), new ResourceLocation("potions"), (stack, world, living, entityId) -> {
			if(stack.getTag() != null)
			{
				if(stack.getTag().contains("BlockEntityTag"))
				{
					CompoundTag blockEntityTag = stack.getTag().getCompound("BlockEntityTag");
					if(blockEntityTag.contains("Potions"))
						return blockEntityTag.getInt("Potions");
				}
			}
			return 0;
		});

		// Blowgun Refill
		ItemProperties.register(MineriaItems.BLOWGUN_REFILL, new ResourceLocation(Mineria.MODID, "has_poison"), (stack, world, living, entityId) -> JarItem.containsPoisonSource(stack) ? 1 : 0);
	}

	public static void registerBlockRenders()
	{
		DeferredRegisterUtil.filterEntriesFromRegister(MineriaBlocks.BLOCKS, SpikeBlock.class).forEach(RenderHandler::registerCutout);
		DeferredRegisterUtil.filterEntriesFromRegister(MineriaBlocks.BLOCKS, PlantBlock.class).forEach(RenderHandler::registerCutout);
		DeferredRegisterUtil.filterEntriesFromRegister(MineriaBlocks.BLOCKS, FruitPlantBlock.class).forEach(RenderHandler::registerCutout);
		DeferredRegisterUtil.filterEntriesFromRegister(MineriaBlocks.BLOCKS, DoublePlantBlock.class).forEach(RenderHandler::registerCutout);
		DeferredRegisterUtil.filterEntriesFromRegister(MineriaBlocks.BLOCKS, StrychnosPlantBlock.class).forEach(RenderHandler::registerCutout);
		DeferredRegisterUtil.filterEntriesFromRegister(MineriaBlocks.BLOCKS, VineBlock.class).forEach(RenderHandler::registerCutout);
		DeferredRegisterUtil.filterEntriesFromRegister(MineriaBlocks.BLOCKS, LeavesBlock.class).forEach(RenderHandler::registerCutout);
		DeferredRegisterUtil.filterEntriesFromRegister(MineriaBlocks.BLOCKS, SaplingBlock.class).forEach(RenderHandler::registerCutout);
		DeferredRegisterUtil.filterEntriesFromRegister(MineriaBlocks.BLOCKS, TNTBarrelBlock.class).forEach(RenderHandler::registerCutout);
		RenderHandler.registerCutout(MineriaBlocks.DISTILLER);
		RenderHandler.registerCutout(MineriaBlocks.APOTHECARY_TABLE);
		DeferredRegisterUtil.filterEntriesFromRegister(MineriaBlocks.BLOCKS, FlowerPotBlock.class).forEach(RenderHandler::registerCutout);
		DeferredRegisterUtil.filterEntriesFromRegister(MineriaBlocks.BLOCKS, MineriaMushroomBlock.class).forEach(RenderHandler::registerCutout);
	}

	private static void registerCutout(Block block)
	{
		registerRenderType(block, RenderType.cutout());
	}

	private static void registerTranslucent(Block block)
	{
		registerRenderType(block, RenderType.translucent());
	}

	private static void registerRenderType(Block block, RenderType type)
	{
		ItemBlockRenderTypes.setRenderLayer(block, type);
	}

	@SubscribeEvent
	public static void registerBlockColors(ColorHandlerEvent.Block event)
	{
	}

	@SubscribeEvent
	public static void registerItemColors(ColorHandlerEvent.Item event)
	{
		event.getItemColors().register((stack, tintIndex) -> tintIndex > 0 ? -1 : MineriaPotion.getColor(stack), MineriaItems.MINERIA_POTION, MineriaItems.MINERIA_SPLASH_POTION, MineriaItems.MINERIA_LINGERING_POTION);
		event.getItemColors().register((stack, tintIndex) -> tintIndex > 0 ? -1 : ((DyeableLeatherItem) stack.getItem()).getColor(stack), MineriaItems.JAR);
	}
}
