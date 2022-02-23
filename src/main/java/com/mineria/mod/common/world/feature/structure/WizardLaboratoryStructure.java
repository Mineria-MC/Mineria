package com.mineria.mod.common.world.feature.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

public class WizardLaboratoryStructure extends StructureFeature<NoneFeatureConfiguration>
{
    public WizardLaboratoryStructure()
    {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public StructureStartFactory<NoneFeatureConfiguration> getStartFactory()
    {
        return Start::new;
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
        public void generatePieces(RegistryAccess pRegistryAccess, ChunkGenerator generator, StructureManager pStructureManager, ChunkPos pChunkPos, Biome pBiome, NoneFeatureConfiguration pConfig, LevelHeightAccessor pLevel)
        {
            Rotation rotation = Rotation.getRandom(random);

            int x = (pChunkPos.x << 4) + 7;
            int z = (pChunkPos.z << 4) + 7;
            int y = generator.getBaseHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG, pLevel);
            BlockPos pos = new BlockPos(x, y - 9, z);

//            WizardLaboratoryPiece.start(manager, pos, rotation, this.pieces, random);

            this.getBoundingBox();
        }
    }
}
