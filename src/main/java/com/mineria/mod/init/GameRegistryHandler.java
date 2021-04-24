package com.mineria.mod.init;

import com.mineria.mod.References;
import com.mineria.mod.blocks.barrel.TileEntityWaterBarrel;
import com.mineria.mod.blocks.barrel.copper.TileEntityCopperWaterBarrel;
import com.mineria.mod.blocks.barrel.golden.TileEntityGoldenWaterBarrel;
import com.mineria.mod.blocks.barrel.iron.TileEntityIronFluidBarrel;
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
        GameRegistry.registerTileEntity(TileEntityWaterBarrel.class, new ResourceLocation(References.MODID, "water_barrel"));
        GameRegistry.registerTileEntity(TileEntityCopperWaterBarrel.class, new ResourceLocation(References.MODID, "copper_water_barrel"));
        GameRegistry.registerTileEntity(TileEntityIronFluidBarrel.class, new ResourceLocation(References.MODID, "iron_fluid_barrel"));
        GameRegistry.registerTileEntity(TileEntityGoldenWaterBarrel.class, new ResourceLocation(References.MODID, "golden_water_barrel"));
    }

    public static void registerWorldGenerators()
    {
        GameRegistry.registerWorldGenerator(new WorldGenCustomOres(), 0);
        GameRegistry.registerWorldGenerator(new WorldGenCustomPlants(), 0);
    }
}
