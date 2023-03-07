package io.github.mineria_mc.mineria.common.blocks.barrel.diamond;

import io.github.mineria_mc.mineria.common.blocks.barrel.AbstractWaterBarrelBlock;
import io.github.mineria_mc.mineria.common.blocks.barrel.AbstractWaterBarrelBlockEntity;
import io.github.mineria_mc.mineria.common.init.MineriaTileEntities;
import io.github.mineria_mc.mineria.common.items.IMineriaItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class DiamondFluidBarrelBlock extends AbstractWaterBarrelBlock {
    public static final BooleanProperty NETHERITE_LOOK = BooleanProperty.create("netherite_look");

    public DiamondFluidBarrelBlock() {
        super(5, 15, 64);
        registerDefaultState(this.stateDefinition.any().setValue(NETHERITE_LOOK, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(NETHERITE_LOOK);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState state = super.getStateForPlacement(ctx);
        CompoundTag data = BlockItem.getBlockEntityData(ctx.getItemInHand());
        if(state != null && data != null && data.getBoolean("NetheriteLook")) {
            state = state.setValue(NETHERITE_LOOK, true);
        }
        return state;
    }

    @Override
    protected void basicInteraction(AbstractWaterBarrelBlockEntity tile, BlockPos pos, Player player) {
        if(player.isShiftKeyDown()) {
            super.basicInteraction(tile, pos, player);
        } else {
            NetworkHooks.openScreen((ServerPlayer) player, (DiamondFluidBarrelBlockEntity) tile, pos);
        }
    }

    @Override
    protected void addInformationOnShift(ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("tooltip.mineria.water_barrel.ability").withStyle(ChatFormatting.GOLD, ChatFormatting.ITALIC).append(": ").append(Component.translatable("tooltip.mineria.water_barrel.upgrades")));
        tooltip.add(Component.translatable("tooltip.mineria.water_barrel.view_capacity").withStyle(ChatFormatting.GRAY));
    }

    @Nonnull
    @Override
    public FluidStack drainFromFluidHandler(IFluidHandler handler, IFluidHandler.FluidAction action) {
        return handler.drain(1000, action);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        return new DiamondFluidBarrelBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @Nonnull BlockState state, @Nonnull BlockEntityType<T> blockEntityType) {
        return level.isClientSide || blockEntityType != MineriaTileEntities.DIAMOND_FLUID_BARREL.get() ? null : (level1, pos, state1, blockEntity) -> ((DiamondFluidBarrelBlockEntity) blockEntity).serverTick(level1, pos, state1);
    }

    public static class BarrelBlockItem extends AbstractWaterBarrelBlock.WaterBarrelBlockItem<DiamondFluidBarrelBlock> implements IMineriaItem {
        public BarrelBlockItem(DiamondFluidBarrelBlock barrel) {
            super(barrel, new Item.Properties().stacksTo(1));
        }

        @Override
        public boolean isFireResistant(ItemStack stack) {
            CompoundTag data = BlockItem.getBlockEntityData(stack);
            return data != null && data.getBoolean("NetheriteLook");
        }

        @Override
        public boolean canBeHurtBy(ItemStack stack, DamageSource source) {
            return !isFireResistant(stack) || !source.isFire();
        }
    }
}
