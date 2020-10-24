package com.mineria.mod.util.handler;

import com.mineria.mod.entity.EntityGoldenFish;
import com.mineria.mod.entity.render.RenderGoldenFish;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RenderHandler
{
	@SideOnly(Side.CLIENT)
	public static void registerEntityRenders()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityGoldenFish.class, RenderGoldenFish::new);
	}
}
