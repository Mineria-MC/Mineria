package com.mineria.mod.init;

import com.mineria.mod.References;
import com.mineria.mod.blocks.barrel.AbstractWaterBarrelTileEntity;
import com.mineria.mod.blocks.barrel.WaterBarrelTileEntity;
import com.mineria.mod.blocks.barrel.copper.CopperWaterBarrelTileEntity;
import com.mineria.mod.blocks.barrel.golden.GoldenWaterBarrelTileEntity;
import com.mineria.mod.blocks.barrel.iron.IronFluidBarrelTileEntity;
import com.mineria.mod.blocks.extractor.ExtractorTileEntity;
import com.mineria.mod.blocks.infuser.InfuserTileEntity;
import com.mineria.mod.blocks.titane_extractor.TitaneExtractorTileEntity;
import com.mineria.mod.blocks.xp_block.XpBlockTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileEntitiesInit
{
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, References.MODID);

    public static final RegistryObject<TileEntityType<TitaneExtractorTileEntity>> TITANE_EXTRACTOR = TILE_ENTITY_TYPES.register("titane_extractor", () -> TileEntityType.Builder.create(TitaneExtractorTileEntity::new, BlocksInit.TITANE_EXTRACTOR).build(null));
    public static final RegistryObject<TileEntityType<WaterBarrelTileEntity>> WATER_BARREL = TILE_ENTITY_TYPES.register("water_barrel", () -> TileEntityType.Builder.create(WaterBarrelTileEntity::new, BlocksInit.WATER_BARREL, BlocksInit.INFINITE_WATER_BARREL).build(null));
    public static final RegistryObject<TileEntityType<CopperWaterBarrelTileEntity>> COPPER_WATER_BARREL = TILE_ENTITY_TYPES.register("copper_water_barrel", () -> TileEntityType.Builder.create(CopperWaterBarrelTileEntity::new, BlocksInit.COPPER_WATER_BARREL).build(null));
    public static final RegistryObject<TileEntityType<IronFluidBarrelTileEntity>> IRON_FLUID_BARREL = TILE_ENTITY_TYPES.register("iron_fluid_barrel", () -> TileEntityType.Builder.create(IronFluidBarrelTileEntity::new, BlocksInit.IRON_FLUID_BARREL).build(null));
    public static final RegistryObject<TileEntityType<GoldenWaterBarrelTileEntity>> GOLDEN_WATER_BARREL = TILE_ENTITY_TYPES.register("golden_water_barrel", () -> TileEntityType.Builder.create(GoldenWaterBarrelTileEntity::new, BlocksInit.GOLDEN_WATER_BARREL).build(null));
    public static final RegistryObject<TileEntityType<InfuserTileEntity>> INFUSER = TILE_ENTITY_TYPES.register("infuser", () -> TileEntityType.Builder.create(InfuserTileEntity::new, BlocksInit.INFUSER).build(null));
    public static final RegistryObject<TileEntityType<XpBlockTileEntity>> XP_BLOCK = TILE_ENTITY_TYPES.register("xp_block", () -> TileEntityType.Builder.create(XpBlockTileEntity::new, BlocksInit.XP_BLOCK).build(null));
    public static final RegistryObject<TileEntityType<ExtractorTileEntity>> EXTRACTOR = TILE_ENTITY_TYPES.register("extractor", () -> TileEntityType.Builder.create(ExtractorTileEntity::new, BlocksInit.EXTRACTOR).build(null));
}
