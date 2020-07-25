package com.mineria.mod.util.handler;

import com.mineria.mod.References;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;

public class LootTableHandler
{
	public static final ResourceLocation GOLDEN_FISH = LootTableList.register(new ResourceLocation(References.MODID, "golden_fish"));
	public static final ResourceLocation SILVERFISH = LootTableList.register(new ResourceLocation(References.MODID, "silverfish"));
}
