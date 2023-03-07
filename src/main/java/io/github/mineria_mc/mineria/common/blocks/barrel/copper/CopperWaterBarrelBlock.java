package io.github.mineria_mc.mineria.common.blocks.barrel.copper;

import io.github.mineria_mc.mineria.common.blocks.barrel.AbstractWaterBarrelBlock;
import io.github.mineria_mc.mineria.common.blocks.barrel.AbstractWaterBarrelBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class CopperWaterBarrelBlock extends AbstractWaterBarrelBlock {
    public CopperWaterBarrelBlock() {
        super(3.5F, 10, 16);
    }

    @Override
    protected void basicInteraction(AbstractWaterBarrelBlockEntity tile, BlockPos pos, Player player) {
        if(player.isShiftKeyDown()) {
            super.basicInteraction(tile, pos, player);
        } else {
            NetworkHooks.openScreen((ServerPlayer) player, (CopperWaterBarrelBlockEntity) tile, pos);
        }
    }

    @Override
    protected void addInformationOnShift(ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("tooltip.mineria.water_barrel.ability").withStyle(ChatFormatting.GOLD, ChatFormatting.ITALIC).append(": ").append(Component.translatable("tooltip.mineria.water_barrel.small_inventory_ability")));
        tooltip.add(Component.translatable("tooltip.mineria.water_barrel.view_capacity").withStyle(ChatFormatting.GRAY));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockPos pPos, @Nonnull BlockState pState) {
        return new CopperWaterBarrelBlockEntity(pPos, pState);
    }
}
