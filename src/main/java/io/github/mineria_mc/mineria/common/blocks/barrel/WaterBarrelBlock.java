package io.github.mineria_mc.mineria.common.blocks.barrel;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.BlockGetter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class WaterBarrelBlock extends AbstractWaterBarrelBlock {
    public WaterBarrelBlock(int initialCapacity) {
        super(2, 10, initialCapacity);
    }

    @Override
    protected void addInformationOnShift(ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        return new WaterBarrelBlockEntity(initialCapacity, pos, state);
    }
}
