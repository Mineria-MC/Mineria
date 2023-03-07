package io.github.mineria_mc.mineria.common.init;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.blocks.apothecary_table.ApothecaryTableTileEntity;
import io.github.mineria_mc.mineria.common.blocks.barrel.WaterBarrelBlockEntity;
import io.github.mineria_mc.mineria.common.blocks.barrel.copper.CopperWaterBarrelBlockEntity;
import io.github.mineria_mc.mineria.common.blocks.barrel.diamond.DiamondFluidBarrelBlockEntity;
import io.github.mineria_mc.mineria.common.blocks.barrel.golden.GoldenWaterBarrelBlockEntity;
import io.github.mineria_mc.mineria.common.blocks.barrel.iron.IronFluidBarrelBlockEntity;
import io.github.mineria_mc.mineria.common.blocks.distiller.DistillerBlockEntity;
import io.github.mineria_mc.mineria.common.blocks.extractor.ExtractorTileEntity;
import io.github.mineria_mc.mineria.common.blocks.infuser.InfuserBlockEntity;
import io.github.mineria_mc.mineria.common.blocks.ritual_table.RitualTableTileEntity;
import io.github.mineria_mc.mineria.common.blocks.titane_extractor.TitaneExtractorBlockEntity;
import io.github.mineria_mc.mineria.common.blocks.xp_block.XpBlockTileEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MineriaTileEntities {
    public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Mineria.MODID);

    public static final RegistryObject<BlockEntityType<TitaneExtractorBlockEntity>> TITANE_EXTRACTOR = TILE_ENTITY_TYPES.register("titane_extractor", () -> BlockEntityType.Builder.of(TitaneExtractorBlockEntity::new, MineriaBlocks.TITANE_EXTRACTOR.get()).build(null));
    public static final RegistryObject<BlockEntityType<WaterBarrelBlockEntity>> WATER_BARREL = TILE_ENTITY_TYPES.register("water_barrel", () -> BlockEntityType.Builder.of(WaterBarrelBlockEntity::new, MineriaBlocks.WATER_BARREL.get(), MineriaBlocks.INFINITE_WATER_BARREL.get()).build(null));
    public static final RegistryObject<BlockEntityType<CopperWaterBarrelBlockEntity>> COPPER_WATER_BARREL = TILE_ENTITY_TYPES.register("copper_water_barrel", () -> BlockEntityType.Builder.of(CopperWaterBarrelBlockEntity::new, MineriaBlocks.COPPER_WATER_BARREL.get()).build(null));
    public static final RegistryObject<BlockEntityType<IronFluidBarrelBlockEntity>> IRON_FLUID_BARREL = TILE_ENTITY_TYPES.register("iron_fluid_barrel", () -> BlockEntityType.Builder.of(IronFluidBarrelBlockEntity::new, MineriaBlocks.IRON_FLUID_BARREL.get()).build(null));
    public static final RegistryObject<BlockEntityType<GoldenWaterBarrelBlockEntity>> GOLDEN_WATER_BARREL = TILE_ENTITY_TYPES.register("golden_water_barrel", () -> BlockEntityType.Builder.of(GoldenWaterBarrelBlockEntity::new, MineriaBlocks.GOLDEN_WATER_BARREL.get()).build(null));
    public static final RegistryObject<BlockEntityType<DiamondFluidBarrelBlockEntity>> DIAMOND_FLUID_BARREL = TILE_ENTITY_TYPES.register("diamond_fluid_barrel", () -> BlockEntityType.Builder.of(DiamondFluidBarrelBlockEntity::new, MineriaBlocks.DIAMOND_FLUID_BARREL.get()).build(null));
    public static final RegistryObject<BlockEntityType<InfuserBlockEntity>> INFUSER = TILE_ENTITY_TYPES.register("infuser", () -> BlockEntityType.Builder.of(InfuserBlockEntity::new, MineriaBlocks.INFUSER.get()).build(null));
    public static final RegistryObject<BlockEntityType<XpBlockTileEntity>> XP_BLOCK = TILE_ENTITY_TYPES.register("xp_block", () -> BlockEntityType.Builder.of(XpBlockTileEntity::new, MineriaBlocks.XP_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<ExtractorTileEntity>> EXTRACTOR = TILE_ENTITY_TYPES.register("extractor", () -> BlockEntityType.Builder.of(ExtractorTileEntity::new, MineriaBlocks.EXTRACTOR.get()).build(null));
    public static final RegistryObject<BlockEntityType<DistillerBlockEntity>> DISTILLER = TILE_ENTITY_TYPES.register("distiller", () -> BlockEntityType.Builder.of(DistillerBlockEntity::new, MineriaBlocks.DISTILLER.get()).build(null));
    public static final RegistryObject<BlockEntityType<ApothecaryTableTileEntity>> APOTHECARY_TABLE = TILE_ENTITY_TYPES.register("apothecary_table", () -> BlockEntityType.Builder.of(ApothecaryTableTileEntity::new, MineriaBlocks.APOTHECARY_TABLE.get()).build(null));
    public static final RegistryObject<BlockEntityType<RitualTableTileEntity>> RITUAL_TABLE = TILE_ENTITY_TYPES.register("ritual_table", () -> BlockEntityType.Builder.of(RitualTableTileEntity::new, MineriaBlocks.RITUAL_TABLE.get()).build(null));
}
