package com.mineria.mod.common.init;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.blocks.apothecary_table.ApothecaryTableTileEntity;
import com.mineria.mod.common.blocks.barrel.WaterBarrelTileEntity;
import com.mineria.mod.common.blocks.barrel.copper.CopperWaterBarrelTileEntity;
import com.mineria.mod.common.blocks.barrel.golden.GoldenWaterBarrelTileEntity;
import com.mineria.mod.common.blocks.barrel.iron.IronFluidBarrelTileEntity;
import com.mineria.mod.common.blocks.distiller.DistillerTileEntity;
import com.mineria.mod.common.blocks.extractor.ExtractorTileEntity;
import com.mineria.mod.common.blocks.infuser.InfuserTileEntity;
import com.mineria.mod.common.blocks.ritual_table.RitualTableTileEntity;
import com.mineria.mod.common.blocks.titane_extractor.TitaneExtractorTileEntity;
import com.mineria.mod.common.blocks.xp_block.XpBlockTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MineriaTileEntities
{
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Mineria.MODID);

    public static final RegistryObject<TileEntityType<TitaneExtractorTileEntity>> TITANE_EXTRACTOR = TILE_ENTITY_TYPES.register("titane_extractor", () -> TileEntityType.Builder.of(TitaneExtractorTileEntity::new, MineriaBlocks.TITANE_EXTRACTOR).build(null));
    public static final RegistryObject<TileEntityType<WaterBarrelTileEntity>> WATER_BARREL = TILE_ENTITY_TYPES.register("water_barrel", () -> TileEntityType.Builder.of(WaterBarrelTileEntity::new, MineriaBlocks.WATER_BARREL, MineriaBlocks.INFINITE_WATER_BARREL).build(null));
    public static final RegistryObject<TileEntityType<CopperWaterBarrelTileEntity>> COPPER_WATER_BARREL = TILE_ENTITY_TYPES.register("copper_water_barrel", () -> TileEntityType.Builder.of(CopperWaterBarrelTileEntity::new, MineriaBlocks.COPPER_WATER_BARREL).build(null));
    public static final RegistryObject<TileEntityType<IronFluidBarrelTileEntity>> IRON_FLUID_BARREL = TILE_ENTITY_TYPES.register("iron_fluid_barrel", () -> TileEntityType.Builder.of(IronFluidBarrelTileEntity::new, MineriaBlocks.IRON_FLUID_BARREL).build(null));
    public static final RegistryObject<TileEntityType<GoldenWaterBarrelTileEntity>> GOLDEN_WATER_BARREL = TILE_ENTITY_TYPES.register("golden_water_barrel", () -> TileEntityType.Builder.of(GoldenWaterBarrelTileEntity::new, MineriaBlocks.GOLDEN_WATER_BARREL).build(null));
    public static final RegistryObject<TileEntityType<InfuserTileEntity>> INFUSER = TILE_ENTITY_TYPES.register("infuser", () -> TileEntityType.Builder.of(InfuserTileEntity::new, MineriaBlocks.INFUSER).build(null));
    public static final RegistryObject<TileEntityType<XpBlockTileEntity>> XP_BLOCK = TILE_ENTITY_TYPES.register("xp_block", () -> TileEntityType.Builder.of(XpBlockTileEntity::new, MineriaBlocks.XP_BLOCK).build(null));
    public static final RegistryObject<TileEntityType<ExtractorTileEntity>> EXTRACTOR = TILE_ENTITY_TYPES.register("extractor", () -> TileEntityType.Builder.of(ExtractorTileEntity::new, MineriaBlocks.EXTRACTOR).build(null));
    public static final RegistryObject<TileEntityType<DistillerTileEntity>> DISTILLER = TILE_ENTITY_TYPES.register("distiller", () -> TileEntityType.Builder.of(DistillerTileEntity::new, MineriaBlocks.DISTILLER).build(null));
    public static final RegistryObject<TileEntityType<ApothecaryTableTileEntity>> APOTHECARY_TABLE = TILE_ENTITY_TYPES.register("apothecary_table", () -> TileEntityType.Builder.of(ApothecaryTableTileEntity::new, MineriaBlocks.APOTHECARY_TABLE).build(null));
    public static final RegistryObject<TileEntityType<RitualTableTileEntity>> RITUAL_TABLE = TILE_ENTITY_TYPES.register("ritual_table", () -> TileEntityType.Builder.of(RitualTableTileEntity::new, MineriaBlocks.RITUAL_TABLE).build(null));
}
