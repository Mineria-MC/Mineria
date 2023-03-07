package io.github.mineria_mc.mineria.common.blocks.barrel.golden;

import io.github.mineria_mc.mineria.common.blocks.barrel.AbstractWaterBarrelBlock;
import io.github.mineria_mc.mineria.common.blocks.barrel.AbstractWaterBarrelBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class GoldenWaterBarrelBlock extends AbstractWaterBarrelBlock {
    public static final IntegerProperty POTIONS = IntegerProperty.create("potions", 0, 4);

    public GoldenWaterBarrelBlock() {
        super(4.5F, 12, 32);
        registerDefaultState(this.stateDefinition.any().setValue(POTIONS, 0));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@Nonnull BlockPlaceContext ctx) {
        BlockState state = super.getStateForPlacement(ctx);
        CompoundTag data = BlockItem.getBlockEntityData(ctx.getItemInHand());
        if(state != null && data != null && data.contains("Potions", Tag.TAG_INT)) {
            state = state.setValue(POTIONS, data.getInt("Potions"));
        }
        return state;
    }

    @Override
    protected void basicInteraction(AbstractWaterBarrelBlockEntity tile, BlockPos pos, Player player) {
        if(player.isShiftKeyDown()) {
            super.basicInteraction(tile, pos, player);
        } else {
            NetworkHooks.openScreen((ServerPlayer) player, (GoldenWaterBarrelBlockEntity) tile, pos);
        }
    }

    @Override
    protected void addInformationOnShift(ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("tooltip.mineria.water_barrel.ability").withStyle(ChatFormatting.GOLD, ChatFormatting.ITALIC).append(": ").append(Component.translatable("tooltip.mineria.water_barrel.store_potions")));
        tooltip.add(Component.translatable("tooltip.mineria.water_barrel.view_capacity").withStyle(ChatFormatting.GRAY));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(POTIONS);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockPos pPos, @Nonnull BlockState pState) {
        return new GoldenWaterBarrelBlockEntity(pPos, pState);
    }

    public static class BarrelBlockItem extends AbstractWaterBarrelBlock.WaterBarrelBlockItem<GoldenWaterBarrelBlock> {
        public BarrelBlockItem(GoldenWaterBarrelBlock barrel) {
            super(barrel, new Properties().stacksTo(1));
        }

        @Override
        public CompoundTag writeAdditional(CompoundTag blockEntityTag) {
            blockEntityTag.putInt("Potions", 0);
            return blockEntityTag;
        }
    }
}
