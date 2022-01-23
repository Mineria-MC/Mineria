package com.mineria.mod.util;

import com.mineria.mod.Mineria;
import com.mineria.mod.client.renderers.ExtractorTileEntityRenderer;
import com.mineria.mod.client.renderers.RitualTableTileEntityRenderer;
import com.mineria.mod.client.renderers.entity.*;
import com.mineria.mod.client.screens.*;
import com.mineria.mod.common.blocks.*;
import com.mineria.mod.common.effects.potions.MineriaPotion;
import com.mineria.mod.common.init.*;
import com.mineria.mod.common.items.JarItem;
import com.mineria.mod.common.items.MineriaBow;
import net.minecraft.block.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.ItemFrameRenderer;
import net.minecraft.client.renderer.entity.LightningBoltRenderer;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.item.IDyeableArmorItem;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
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
		ScreenManager.register(MineriaContainerTypes.TITANE_EXTRACTOR.get(), TitaneExtractorScreen::new);
		ScreenManager.register(MineriaContainerTypes.INFUSER.get(), InfuserScreen::new);
		ScreenManager.register(MineriaContainerTypes.XP_BLOCK.get(), XpBlockScreen::new);
		ScreenManager.register(MineriaContainerTypes.COPPER_WATER_BARREL.get(), CopperWaterBarrelScreen::new);
		ScreenManager.register(MineriaContainerTypes.GOLDEN_WATER_BARREL.get(), GoldenWaterBarrelScreen::new);
		ScreenManager.register(MineriaContainerTypes.EXTRACTOR.get(), ExtractorScreen::new);
		ScreenManager.register(MineriaContainerTypes.DISTILLER.get(), DistillerScreen::new);
		ScreenManager.register(MineriaContainerTypes.APOTHECARY_TABLE.get(), ApothecaryTableScreen::new);
	}

	public static void registerEntityRenders()
	{
		RenderingRegistry.registerEntityRenderingHandler(MineriaEntities.GOLDEN_SILVERFISH.get(), GoldenSilverfishRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(MineriaEntities.WIZARD.get(), WizardRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(MineriaEntities.KUNAI.get(), KunaiRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(MineriaEntities.MINERIA_POTION.get(), manager -> new SpriteRenderer<>(manager, Minecraft.getInstance().getItemRenderer()));
		RenderingRegistry.registerEntityRenderingHandler(MineriaEntities.MINERIA_AREA_EFFECT_CLOUD.get(), MineriaAreaEffectCloudRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(MineriaEntities.DRUID.get(), DruidRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(MineriaEntities.OVATE.get(), DruidRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(MineriaEntities.BARD.get(), DruidRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(MineriaEntities.ELEMENTAL_ORB.get(), ElementalOrbRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(MineriaEntities.FIRE_GOLEM.get(), FireGolemRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(MineriaEntities.DIRT_GOLEM.get(), DirtGolemRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(MineriaEntities.AIR_SPIRIT.get(), AirSpiritRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(MineriaEntities.WATER_SPIRIT.get(), WaterSpiritRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(MineriaEntities.DART.get(), BlowgunRefillRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(MineriaEntities.JAR.get(), manager -> new SpriteRenderer<>(manager, Minecraft.getInstance().getItemRenderer()));
		RenderingRegistry.registerEntityRenderingHandler(MineriaEntities.DRUIDIC_WOLF.get(), DruidicWolfRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(MineriaEntities.BROWN_BEAR.get(), BrownBearRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(MineriaEntities.GREAT_DRUID_OF_GAULS.get(), GreatDruidOfGaulsRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(MineriaEntities.MINERIA_LIGHTNING_BOLT.get(), LightningBoltRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(MineriaEntities.BUDDHIST.get(), BuddhistRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(MineriaEntities.ASIATIC_HERBALIST.get(), AsiaticHerbalistRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(MineriaEntities.TEMPORARY_ITEM_FRAME.get(), manager -> new ItemFrameRenderer(manager, Minecraft.getInstance().getItemRenderer()));
	}

	public static void registerTileEntityRenderers()
	{
		ClientRegistry.bindTileEntityRenderer(MineriaTileEntities.EXTRACTOR.get(), ExtractorTileEntityRenderer::new);
		ClientRegistry.bindTileEntityRenderer(MineriaTileEntities.RITUAL_TABLE.get(), RitualTableTileEntityRenderer::new);
	}

	/**
	 * Event for registering particle types. Unused.
	 * @param event the forge event called when these need to register.
	 */
	@SubscribeEvent
	public static void registerParticles(ParticleFactoryRegisterEvent event)
	{
		ParticleManager manager = Minecraft.getInstance().particleEngine;
	}

	public static void registerItemModelsProperties()
	{
		// Bow items
		DeferredRegisterUtil.filterEntriesFromRegister(MineriaItems.ITEMS, MineriaBow.class).forEach(item -> {
			ItemModelsProperties.register(item, new ResourceLocation("pull"), (stack, world, living) -> {
				if (living == null)
					return 0.0F;
				else
					return living.getUseItem() != stack ? 0.0F : (float)(stack.getUseDuration() - living.getUseItemRemainingTicks()) / 20.0F;
			});
			ItemModelsProperties.register(item, new ResourceLocation("pulling"),
					(stack, world, living) -> living != null && living.isUsingItem() && living.getUseItem() == stack ? 1.0F : 0.0F);
		});

		// Golden Water Barrel
		ItemModelsProperties.register(MineriaBlocks.getItemFromBlock(MineriaBlocks.GOLDEN_WATER_BARREL), new ResourceLocation("potions"), (stack, world, living) -> {
			if(stack.getTag() != null)
			{
				if(stack.getTag().contains("BlockEntityTag"))
				{
					CompoundNBT blockEntityTag = stack.getTag().getCompound("BlockEntityTag");
					if(blockEntityTag.contains("Potions"))
						return blockEntityTag.getInt("Potions");
				}
			}
			return 0;
		});

		// Blowgun Refill
		ItemModelsProperties.register(MineriaItems.BLOWGUN_REFILL, new ResourceLocation(Mineria.MODID, "has_poison"), (stack, world, living) -> JarItem.containsPoisonSource(stack) ? 1 : 0);
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
		RenderTypeLookup.setRenderLayer(block, type);
	}

	@SubscribeEvent
	public static void registerBlockColors(ColorHandlerEvent.Block event)
	{
	}

	@SubscribeEvent
	public static void registerItemColors(ColorHandlerEvent.Item event)
	{
		event.getItemColors().register((stack, tintIndex) -> tintIndex > 0 ? -1 : MineriaPotion.getColor(stack), MineriaItems.MINERIA_POTION, MineriaItems.MINERIA_SPLASH_POTION, MineriaItems.MINERIA_LINGERING_POTION);
		event.getItemColors().register((stack, tintIndex) -> tintIndex > 0 ? -1 : ((IDyeableArmorItem) stack.getItem()).getColor(stack), MineriaItems.JAR);
	}
}
