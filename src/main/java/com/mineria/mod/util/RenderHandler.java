package com.mineria.mod.util;

import com.mineria.mod.entity.EntityGoldenSilverfish;
import com.mineria.mod.entity.render.RenderGoldenFish;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RenderHandler
{
	@SideOnly(Side.CLIENT)
	public static void registerEntityRenders()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityGoldenSilverfish.class, RenderGoldenFish::new);
	}
}
