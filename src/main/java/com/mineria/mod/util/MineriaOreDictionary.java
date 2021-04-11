package com.mineria.mod.util;

import com.mineria.mod.init.ItemsInit;
import net.minecraftforge.oredict.OreDictionary;

public class MineriaOreDictionary
{
	public static void registerOres()
	{
		OreDictionary.registerOre("ingotCopper", ItemsInit.COPPER_INGOT);
		OreDictionary.registerOre("ingotLead", ItemsInit.LEAD_INGOT);
		OreDictionary.registerOre("ingotTitanium", ItemsInit.TITANE_INGOT);
		OreDictionary.registerOre("ingotSilver", ItemsInit.SILVER_INGOT);
	}
}
