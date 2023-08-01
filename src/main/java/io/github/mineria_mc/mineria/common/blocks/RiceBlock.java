package io.github.mineria_mc.mineria.common.blocks;

import io.github.mineria_mc.mineria.common.init.MineriaBlocks;
import io.github.mineria_mc.mineria.common.init.MineriaItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;

public class RiceBlock extends CropBlock {
    public RiceBlock() {
        super(Properties.copy(Blocks.WHEAT));
    }

    @Override
    protected boolean mayPlaceOn(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return state.is(MineriaBlocks.MUDDY_FARMLAND.get()) && state.getValue(BlockStateProperties.WATERLOGGED);
    }

    @Override
    protected @NotNull ItemLike getBaseSeedId() {
        return MineriaItems.RICE_GRAINS.get();
    }
}
