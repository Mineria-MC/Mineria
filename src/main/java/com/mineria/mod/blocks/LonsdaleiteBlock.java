package com.mineria.mod.blocks;

import com.mineria.mod.util.RenderHandler;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;

/** Unused Class
 * Translucency is actually broken idk why
 * When I enable it it just turns into an x-ray block so... :'(
 */
public class LonsdaleiteBlock extends Block
{
    public LonsdaleiteBlock()
    {
        super(AbstractBlock.Properties.create(Material.GLASS).hardnessAndResistance(10F, 17.5F).sound(SoundType.METAL).harvestTool(ToolType.PICKAXE).harvestLevel(3).setOpaque((e, f, g) -> false).setSuffocates((e, f, g) -> false).setBlocksVision((e, f, g) -> false));
        RenderHandler.registerTranslucent(this);
    }

    @Override
    public boolean isSideInvisible(BlockState state, BlockState adjacentBlockState, Direction side)
    {
        return adjacentBlockState.getBlock() instanceof LonsdaleiteBlock;
    }

    @OnlyIn(Dist.CLIENT)
    public float getAmbientOcclusionLightValue(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return 1.0F;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos)
    {
        return true;
    }
}
