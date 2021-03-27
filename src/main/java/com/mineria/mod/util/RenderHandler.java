package com.mineria.mod.util;

import com.mineria.mod.blocks.infuser.InfuserScreen;
import com.mineria.mod.blocks.titane_extractor.TitaneExtractorScreen;
import com.mineria.mod.blocks.xp_block.XpBlockScreen;
import com.mineria.mod.entity.render.GoldenSilverfishRenderer;
import com.mineria.mod.init.BlocksInit;
import com.mineria.mod.init.ContainerTypeInit;
import com.mineria.mod.init.EntityInit;
import net.minecraft.block.Block;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

import java.util.HashMap;
import java.util.Map;

public class RenderHandler
{
	private static final Map<Block, RenderType> BLOCK_RENDERS = new HashMap<>();

	public static void registerScreenFactories()
	{
		ScreenManager.registerFactory(ContainerTypeInit.TITANE_EXTRACTOR.get(), TitaneExtractorScreen::new);
		ScreenManager.registerFactory(ContainerTypeInit.INFUSER.get(), InfuserScreen::new);
		ScreenManager.registerFactory(ContainerTypeInit.XP_BLOCK.get(), XpBlockScreen::new);
	}

	public static void registerEntityRenders()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityInit.GOLDEN_SILVERFISH.get(), GoldenSilverfishRenderer::new);
	}

	public static void registerBlockRenders()
	{
		BLOCK_RENDERS.forEach(RenderTypeLookup::setRenderLayer);
	}

	public static void registerCutout(Block block)
	{
		registerRenderType(block, RenderType.getCutout());
	}

	public static void registerTranslucent(Block block)
	{
		registerRenderType(block, RenderType.getTranslucent());
	}

	public static void registerRenderType(Block block, RenderType type)
	{
		BLOCK_RENDERS.put(block, type);
	}
}
