package com.mineria.mod.common.world.feature.structure;

import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

import net.minecraft.world.level.levelgen.feature.StructureFeature.StructureStartFactory;

public class WizardTowerStructure extends StructureFeature<NoneFeatureConfiguration>
{
    public WizardTowerStructure()
    {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public StructureStartFactory<NoneFeatureConfiguration> getStartFactory()
    {
        return WizardTowerStructure.Start::new;
    }

    @Override
    public GenerationStep.Decoration step()
    {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }

    public static final class Start extends StructureStart<NoneFeatureConfiguration>
    {
        public Start(StructureFeature<NoneFeatureConfiguration> structure, ChunkPos pos, int references, long seed)
        {
            super(structure, pos, references, seed);
        }

        @Override
        public void generatePieces(RegistryAccess pRegistryAccess, ChunkGenerator generator, StructureManager pStructureManager, ChunkPos chunkPos, Biome pBiome, NoneFeatureConfiguration pConfig, LevelHeightAccessor pLevel)
        {
            Rotation rotation = Rotation.getRandom(random);

            int x = (chunkPos.x << 4) + 7;
            int z = (chunkPos.z << 4) + 7;
            int y = generator.getBaseHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG, pLevel);
            BlockPos pos = new BlockPos(x, y, z);

//            WizardTowerPiece.start(manager, pos, rotation, this.pieces, random);

            this.getBoundingBox();
        }
    }
}
