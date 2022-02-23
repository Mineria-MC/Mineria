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
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MineriaTileEntities
{
    public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Mineria.MODID);

    public static final RegistryObject<BlockEntityType<TitaneExtractorTileEntity>> TITANE_EXTRACTOR = TILE_ENTITY_TYPES.register("titane_extractor", () -> BlockEntityType.Builder.of(TitaneExtractorTileEntity::new, MineriaBlocks.TITANE_EXTRACTOR).build(null));
    public static final RegistryObject<BlockEntityType<WaterBarrelTileEntity>> WATER_BARREL = TILE_ENTITY_TYPES.register("water_barrel", () -> BlockEntityType.Builder.of(WaterBarrelTileEntity::new, MineriaBlocks.WATER_BARREL, MineriaBlocks.INFINITE_WATER_BARREL).build(null));
    public static final RegistryObject<BlockEntityType<CopperWaterBarrelTileEntity>> COPPER_WATER_BARREL = TILE_ENTITY_TYPES.register("copper_water_barrel", () -> BlockEntityType.Builder.of(CopperWaterBarrelTileEntity::new, MineriaBlocks.COPPER_WATER_BARREL).build(null));
    public static final RegistryObject<BlockEntityType<IronFluidBarrelTileEntity>> IRON_FLUID_BARREL = TILE_ENTITY_TYPES.register("iron_fluid_barrel", () -> BlockEntityType.Builder.of(IronFluidBarrelTileEntity::new, MineriaBlocks.IRON_FLUID_BARREL).build(null));
    public static final RegistryObject<BlockEntityType<GoldenWaterBarrelTileEntity>> GOLDEN_WATER_BARREL = TILE_ENTITY_TYPES.register("golden_water_barrel", () -> BlockEntityType.Builder.of(GoldenWaterBarrelTileEntity::new, MineriaBlocks.GOLDEN_WATER_BARREL).build(null));
    public static final RegistryObject<BlockEntityType<InfuserTileEntity>> INFUSER = TILE_ENTITY_TYPES.register("infuser", () -> BlockEntityType.Builder.of(InfuserTileEntity::new, MineriaBlocks.INFUSER).build(null));
    public static final RegistryObject<BlockEntityType<XpBlockTileEntity>> XP_BLOCK = TILE_ENTITY_TYPES.register("xp_block", () -> BlockEntityType.Builder.of(XpBlockTileEntity::new, MineriaBlocks.XP_BLOCK).build(null));
    public static final RegistryObject<BlockEntityType<ExtractorTileEntity>> EXTRACTOR = TILE_ENTITY_TYPES.register("extractor", () -> BlockEntityType.Builder.of(ExtractorTileEntity::new, MineriaBlocks.EXTRACTOR).build(null));
    public static final RegistryObject<BlockEntityType<DistillerTileEntity>> DISTILLER = TILE_ENTITY_TYPES.register("distiller", () -> BlockEntityType.Builder.of(DistillerTileEntity::new, MineriaBlocks.DISTILLER).build(null));
    public static final RegistryObject<BlockEntityType<ApothecaryTableTileEntity>> APOTHECARY_TABLE = TILE_ENTITY_TYPES.register("apothecary_table", () -> BlockEntityType.Builder.of(ApothecaryTableTileEntity::new, MineriaBlocks.APOTHECARY_TABLE).build(null));
    public static final RegistryObject<BlockEntityType<RitualTableTileEntity>> RITUAL_TABLE = TILE_ENTITY_TYPES.register("ritual_table", () -> BlockEntityType.Builder.of(RitualTableTileEntity::new, MineriaBlocks.RITUAL_TABLE).build(null));
}
