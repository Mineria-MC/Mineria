package com.mineria.mod.util;

import com.mineria.mod.References;
import com.mineria.mod.blocks.barrel.TileEntityBarrel;
import com.mineria.mod.blocks.extractor.TileEntityExtractor;
import com.mineria.mod.blocks.infuser.TileEntityInfuser;
import com.mineria.mod.blocks.titane_extractor.TileEntityTitaneExtractor;
import com.mineria.mod.blocks.xp_block.TileEntityXpBlock;
import com.mineria.mod.world.gen.WorldGenCustomOres;
import com.mineria.mod.world.gen.WorldGenCustomPlants;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class GameRegistryHandler
{
    public static void registerTileEntities()
    {
        GameRegistry.registerTileEntity(TileEntityXpBlock.class, new ResourceLocation(References.MODID, "xp_block"));
        GameRegistry.registerTileEntity(TileEntityTitaneExtractor.class, new ResourceLocation(References.MODID, "titane_extractor"));
        GameRegistry.registerTileEntity(TileEntityExtractor.class, new ResourceLocation(References.MODID, "extractor"));
        GameRegistry.registerTileEntity(TileEntityInfuser.class, new ResourceLocation(References.MODID, "infuser"));
        GameRegistry.registerTileEntity(TileEntityBarrel.class, new ResourceLocation(References.MODID, "water_barrel"));
    }

    public static void registerWorldGenerators()
    {
        GameRegistry.registerWorldGenerator(new WorldGenCustomOres(), 0);
        GameRegistry.registerWorldGenerator(new WorldGenCustomPlants(), 0);
    }
}
