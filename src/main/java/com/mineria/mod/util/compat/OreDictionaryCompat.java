package com.mineria.mod.util.compat;

import com.mineria.mod.init.ItemsInit;
import net.minecraftforge.oredict.OreDictionary;

public class OreDictionaryCompat
{
	public static void registerOre()
	{
		OreDictionary.registerOre("ingotCopper", ItemsInit.copper_ingot);
		OreDictionary.registerOre("ingotLead", ItemsInit.lead_ingot);
		OreDictionary.registerOre("ingotTitanium", ItemsInit.titane_ingot);
		OreDictionary.registerOre("ingotSilver", ItemsInit.silver_ingot);
	}
}
