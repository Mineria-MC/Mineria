package com.mineria.mod.common.world.feature.structure;

import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class PagodaStructure extends Structure<NoFeatureConfig>
{
    public PagodaStructure()
    {
        super(NoFeatureConfig.CODEC);
    }

    @Override
    public IStartFactory<NoFeatureConfig> getStartFactory()
    {
        return Start::new;
    }

    @Override
    public GenerationStage.Decoration step()
    {
        return GenerationStage.Decoration.SURFACE_STRUCTURES;
    }

    private static final class Start extends StructureStart<NoFeatureConfig>
    {
        public Start(Structure<NoFeatureConfig> structure, int chunkX, int chunkZ, MutableBoundingBox bounds, int references, long seed)
        {
            super(structure, chunkX, chunkZ, bounds, references, seed);
        }

        @Override
        public void generatePieces(DynamicRegistries registries, ChunkGenerator generator, TemplateManager manager, int chunkX, int chunkZ, Biome biome, NoFeatureConfig config)
        {
            Rotation rotation = Rotation.getRandom(random);

            int x = (chunkX << 4) + 7;
            int z = (chunkZ << 4) + 7;
            int y = generator.getFirstFreeHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG);
            BlockPos pos = new BlockPos(x, y, z);

            PagodaPiece.start(manager, pos, rotation, this.pieces, random);

            this.calculateBoundingBox();
        }
    }
}
