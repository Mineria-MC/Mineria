package io.github.mineria_mc.mineria.common.blocks.barrel.iron;

import io.github.mineria_mc.mineria.common.blocks.barrel.AbstractWaterBarrelBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class IronFluidBarrelBlock extends AbstractWaterBarrelBlock {
    public IronFluidBarrelBlock() {
        super(4, 10, 24);
    }

    @Nonnull
    @Override
    public FluidStack drainFromFluidHandler(IFluidHandler handler, IFluidHandler.FluidAction action) {
        return handler.drain(FluidType.BUCKET_VOLUME, action);
    }

    @Override
    protected void addInformationOnShift(ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("tooltip.mineria.water_barrel.ability").withStyle(ChatFormatting.GOLD, ChatFormatting.ITALIC).append(": ").append(Component.translatable("tooltip.mineria.water_barrel.store_lava")));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockPos pPos, @Nonnull BlockState pState) {
        return new IronFluidBarrelBlockEntity(pPos, pState);
    }
}
