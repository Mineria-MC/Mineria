package com.mineria.mod.entity.render;

import com.mineria.mod.References;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSilverfish;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.util.ResourceLocation;

public class RenderGoldenFish extends RenderSilverfish
{
	public static final ResourceLocation TEXTURES = new ResourceLocation(References.MODID, "textures/entity/golden_silverfish.png");
	
	public RenderGoldenFish(RenderManager manager)
	{
		super(manager);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntitySilverfish entity)
	{
		return TEXTURES;
	}
}
