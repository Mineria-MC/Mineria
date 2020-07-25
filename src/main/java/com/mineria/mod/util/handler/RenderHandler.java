package com.mineria.mod.util.handler;

import com.mineria.mod.entity.EntityGoldenFish;
import com.mineria.mod.entity.render.RenderGoldenFish;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RenderHandler
{
	@SideOnly(Side.CLIENT)
	public static void registerEntityRenders()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityGoldenFish.class, new IRenderFactory<EntityGoldenFish>()
		{
			@Override
			public Render<? super EntityGoldenFish> createRenderFor(RenderManager manager)
			{
				return new RenderGoldenFish(manager);
			}
		});
	}
}
