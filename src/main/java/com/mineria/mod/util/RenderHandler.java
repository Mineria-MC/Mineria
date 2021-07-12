package com.mineria.mod.util;

import com.mineria.mod.blocks.*;
import com.mineria.mod.blocks.barrel.copper.CopperWaterBarrelScreen;
import com.mineria.mod.blocks.barrel.golden.GoldenWaterBarrelScreen;
import com.mineria.mod.blocks.extractor.ExtractorScreen;
import com.mineria.mod.blocks.infuser.InfuserScreen;
import com.mineria.mod.blocks.titane_extractor.TitaneExtractorScreen;
import com.mineria.mod.blocks.xp_block.XpBlockScreen;
import com.mineria.mod.entity.render.GoldenSilverfishRenderer;
import com.mineria.mod.init.BlocksInit;
import com.mineria.mod.init.ContainerTypeInit;
import com.mineria.mod.init.EntityInit;
import com.mineria.mod.init.ItemsInit;
import com.mineria.mod.items.MineriaBow;
import net.minecraft.block.*;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.FoliageColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

@OnlyIn(Dist.CLIENT)
public class RenderHandler
{
	public static void registerScreenFactories()
	{
		ScreenManager.registerFactory(ContainerTypeInit.TITANE_EXTRACTOR.get(), TitaneExtractorScreen::new);
		ScreenManager.registerFactory(ContainerTypeInit.INFUSER.get(), InfuserScreen::new);
		ScreenManager.registerFactory(ContainerTypeInit.XP_BLOCK.get(), XpBlockScreen::new);
		ScreenManager.registerFactory(ContainerTypeInit.COPPER_WATER_BARREL.get(), CopperWaterBarrelScreen::new);
		ScreenManager.registerFactory(ContainerTypeInit.GOLDEN_WATER_BARREL.get(), GoldenWaterBarrelScreen::new);
		ScreenManager.registerFactory(ContainerTypeInit.EXTRACTOR.get(), ExtractorScreen::new);
	}

	public static void registerEntityRenders()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityInit.GOLDEN_SILVERFISH.get(), GoldenSilverfishRenderer::new);
	}

	public static void registerItemModelsProperties()
	{
		// Bow items
		DeferredRegisterUtil.filterEntriesFromRegister(ItemsInit.ITEMS, MineriaBow.class).forEach(item -> {
			ItemModelsProperties.registerProperty(item, new ResourceLocation("pull"), (stack, world, living) -> {
				if (living == null)
					return 0.0F;
				else
					return living.getActiveItemStack() != stack ? 0.0F : (float)(stack.getUseDuration() - living.getItemInUseCount()) / 20.0F;
			});
			ItemModelsProperties.registerProperty(item, new ResourceLocation("pulling"),
					(stack, world, living) -> living != null && living.isHandActive() && living.getActiveItemStack() == stack ? 1.0F : 0.0F);
		});

		// Golden Water Barrel
		ItemModelsProperties.registerProperty(BlocksInit.getItemFromBlock(BlocksInit.GOLDEN_WATER_BARREL), new ResourceLocation("potions"), (stack, world, living) -> {
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
	}

	public static void registerBlockRenders()
	{
		DeferredRegisterUtil.filterEntriesFromRegister(BlocksInit.BLOCKS, SpikeBlock.class).forEach(RenderHandler::registerCutout);
		DeferredRegisterUtil.filterEntriesFromRegister(BlocksInit.BLOCKS, PlantBlock.class).forEach(RenderHandler::registerCutout);
		DeferredRegisterUtil.filterEntriesFromRegister(BlocksInit.BLOCKS, FruitPlantBlock.class).forEach(RenderHandler::registerCutout);
		DeferredRegisterUtil.filterEntriesFromRegister(BlocksInit.BLOCKS, DoublePlantBlock.class).forEach(RenderHandler::registerCutout);
		DeferredRegisterUtil.filterEntriesFromRegister(BlocksInit.BLOCKS, StrychnosPlantBlock.class).forEach(RenderHandler::registerCutout);
		DeferredRegisterUtil.filterEntriesFromRegister(BlocksInit.BLOCKS, VineBlock.class).forEach(RenderHandler::registerCutout);
		DeferredRegisterUtil.filterEntriesFromRegister(BlocksInit.BLOCKS, LeavesBlock.class).forEach(RenderHandler::registerCutout);
		DeferredRegisterUtil.filterEntriesFromRegister(BlocksInit.BLOCKS, SaplingBlock.class).forEach(RenderHandler::registerCutout);
		DeferredRegisterUtil.filterEntriesFromRegister(BlocksInit.BLOCKS, TNTBarrelBlock.class).forEach(RenderHandler::registerCutout);

		DeferredRegisterUtil.filterEntriesFromRegister(BlocksInit.BLOCKS, LonsdaleiteBlock.class).forEach(RenderHandler::registerTranslucent);
	}

	private static void registerCutout(Block block)
	{
		registerRenderType(block, RenderType.getCutout());
	}

	private static void registerTranslucent(Block block)
	{
		registerRenderType(block, RenderType.getTranslucent());
	}

	private static void registerRenderType(Block block, RenderType type)
	{
		RenderTypeLookup.setRenderLayer(block, type);
	}

	public static void registerBlockColors(ColorHandlerEvent.Block event)
	{
		event.getBlockColors().register((state, reader, pos, color) -> FoliageColors.getSpruce(), BlocksInit.SPRUCE_YEW_LEAVES);
	}

	public static void registerItemColors(ColorHandlerEvent.Item event)
	{
		event.getItemColors().register((stack, color) -> {
			BlockState blockstate = ((BlockItem)stack.getItem()).getBlock().getDefaultState();
			return event.getBlockColors().getColor(blockstate, null, null, color);
		}, BlocksInit.SPRUCE_YEW_LEAVES);
	}
}
