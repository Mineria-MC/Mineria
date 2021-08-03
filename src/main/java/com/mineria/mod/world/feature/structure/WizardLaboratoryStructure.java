package com.mineria.mod.world.feature.structure;

import com.google.common.collect.ImmutableList;
import com.mineria.mod.References;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CauldronBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.tileentity.BrewingStandTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.*;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class WizardLaboratoryStructure extends Structure<NoFeatureConfig>
{
    public static final IStructurePieceType MAIN_PIECE = WizardLaboratoryStructure.MainPiece::new;

    public WizardLaboratoryStructure()
    {
        super(NoFeatureConfig.CODEC);
        setRegistryName("wizard_laboratory");
    }

    @Override
    public IStartFactory<NoFeatureConfig> getStartFactory()
    {
        return Start::new;
    }

    @Override
    public GenerationStage.Decoration getDecorationStage()
    {
        return GenerationStage.Decoration.SURFACE_STRUCTURES;
    }

    @Override
    public String getStructureName()
    {
        return References.MODID.concat(":wizard_laboratory");
    }

    public static Map<String, IStructurePieceType> getPieces()
    {
        return Util.make(new HashMap<>(), map -> map.put("main_piece", WizardLaboratoryStructure.MAIN_PIECE));
    }

    public static final class Start extends StructureStart<NoFeatureConfig>
    {
        public Start(Structure<NoFeatureConfig> structure, int chunkX, int chunkZ, MutableBoundingBox bounds, int references, long seed)
        {
            super(structure, chunkX, chunkZ, bounds, references, seed);
        }

        @Override
        public void func_230364_a_(DynamicRegistries registries, ChunkGenerator generator, TemplateManager manager, int chunkX, int chunkZ, Biome biome, NoFeatureConfig config)
        {
            Rotation rotation = Rotation.values()[this.rand.nextInt(Rotation.values().length)];

            int x = (chunkX << 4) + 7;
            int z = (chunkZ << 4) + 7;
            int y = generator.getHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG);
            BlockPos pos = new BlockPos(x, y, z);

            this.components.add(new MainPiece(manager, pos.down(9).rotate(rotation), rotation));

            this.recalculateStructureSize();
        }
    }

    public static final class MainPiece extends TemplateStructurePiece
    {
        private final Rotation rot;

        public MainPiece(TemplateManager manager, BlockPos pos, Rotation rot)
        {
            super(WizardLaboratoryStructure.MAIN_PIECE, 0);
            this.templatePosition = pos;
            this.rot = rot;
            setupPiece(manager);
        }

        public MainPiece(TemplateManager manager, CompoundNBT nbt)
        {
            super(WizardLaboratoryStructure.MAIN_PIECE, nbt);
            this.rot = Rotation.valueOf(nbt.getString("Rot"));
            setupPiece(manager);
        }

        private void setupPiece(TemplateManager manager)
        {
            Template template = manager.getTemplateDefaulted(new ResourceLocation(References.MODID, "wizard_laboratory"));
            PlacementSettings placementSettings = (new PlacementSettings()).setRotation(rot).setMirror(Mirror.NONE);

            this.setup(template, templatePosition, placementSettings);
        }

        @Override
        public boolean func_230383_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random rand, MutableBoundingBox bounds, ChunkPos chunkPos, BlockPos pos)
        {
            PlacementSettings placementSettings = (new PlacementSettings()).setRotation(rot).setMirror(Mirror.NONE);
            this.templatePosition.add(Template.transformedBlockPos(placementSettings, new BlockPos(0, 40, 0)));

            return super.func_230383_a_(world, manager, generator, rand, bounds, chunkPos, pos);
        }

        @Override
        protected void handleDataMarker(String function, BlockPos pos, IServerWorld worldIn, Random rand, MutableBoundingBox sbb)
        {
            switch (function)
            {
                case "chest":
                    generateChest(worldIn, sbb, rand, pos.down(), new ResourceLocation("chests/igloo_chest"), null);
                    worldIn.removeBlock(pos, false);
                    break;
                case "cauldron":
                    BlockPos cauldronPos = pos.down();
                    BlockState cauldronState = worldIn.getBlockState(cauldronPos);

                    if(cauldronState.matchesBlock(Blocks.CAULDRON) && sbb.isVecInside(cauldronPos))
                    {
                        int randLevel = rand.nextInt(4);
                        worldIn.setBlockState(cauldronPos, cauldronState.with(CauldronBlock.LEVEL, randLevel), 2);
                    }
                    worldIn.removeBlock(pos, false);
                    break;
                case "brewing_stand":
                    BlockPos pos1 = pos.down();
                    TileEntity tile = worldIn.getTileEntity(pos1);

                    if(tile instanceof BrewingStandTileEntity && sbb.isVecInside(pos1))
                    {
                        BrewingStandTileEntity brewingStand = (BrewingStandTileEntity) tile;
                        int netherWartCount = rand.nextInt(6);
                        brewingStand.setInventorySlotContents(3, new ItemStack(Items.NETHER_WART, netherWartCount));

                        int potionCount = rand.nextInt(4);
                        List<Pair<Potion, Boolean>> potions = ImmutableList.of(Pair.of(Potions.HEALING, false), Pair.of(Potions.POISON, true),
                                Pair.of(Potions.INVISIBILITY, false), Pair.of(Potions.SLOWNESS, true), Pair.of(Potions.NIGHT_VISION, false),
                                Pair.of(Potions.STRENGTH, false));

                        for (int i = 0; i < potionCount; i++)
                        {
                            Pair<Potion, Boolean> pair = potions.get(rand.nextInt(potions.size()));
                            brewingStand.setInventorySlotContents(i, PotionUtils.addPotionToItemStack(new ItemStack(pair.getRight() ? Items.SPLASH_POTION : Items.POTION), pair.getLeft()));
                        }
                    }
                    worldIn.removeBlock(pos, false);
                    break;
                case "potions":
                    break;
            }
        }

        // writeAdditional
        @Override
        protected void readAdditional(CompoundNBT tagCompound)
        {
            super.readAdditional(tagCompound);
            tagCompound.putString("Rot", this.rot.name());
        }
    }
}
