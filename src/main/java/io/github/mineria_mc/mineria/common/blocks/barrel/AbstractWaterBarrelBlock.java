package io.github.mineria_mc.mineria.common.blocks.barrel;

import io.github.mineria_mc.mineria.common.init.MineriaBlocks;
import io.github.mineria_mc.mineria.common.init.MineriaCriteriaTriggers;
import io.github.mineria_mc.mineria.util.KeyboardHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.FastColor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@SuppressWarnings("deprecation")
public abstract class AbstractWaterBarrelBlock extends Block implements EntityBlock {
    protected final int initialCapacity;

    public AbstractWaterBarrelBlock(float hardness, float resistance, int initialCapacity) {
        super(BlockBehaviour.Properties.of(Material.WOOD).sound(SoundType.WOOD).strength(hardness, resistance).noLootTable());
        this.initialCapacity = initialCapacity;
    }

    @Nonnull
    @Override
    public PushReaction getPistonPushReaction(@Nonnull BlockState state) {
        return PushReaction.BLOCK;
    }

    @Nonnull
    @Override
    public InteractionResult use(@Nonnull BlockState state, Level worldIn, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult hit) {
        if (!worldIn.isClientSide) {
            BlockEntity tileAtPos = worldIn.getBlockEntity(pos);

            if (tileAtPos instanceof AbstractWaterBarrelBlockEntity tile) {
                if(fluidInteraction(worldIn, pos, tile, player, hand)) {
                    return InteractionResult.CONSUME;
                }
                basicInteraction(tile, pos, player);
            }
        }

        return InteractionResult.SUCCESS;
    }

    protected void basicInteraction(AbstractWaterBarrelBlockEntity tile, BlockPos pos, Player player) {
        final MutableComponent message;
        if(tile.isFinite()) {
            int buckets = tile.getBuckets();
            int capacity = tile.getCapacity();
            Fluid fluid = tile.getContainedFluid();
            Component fluidNameComponent = fluid.isSame(Fluids.EMPTY) ? Component.empty() : Component.translatable(fluid.defaultFluidState().createLegacyBlock().getBlock().getDescriptionId());
            if(buckets > 1) {
                message = Component.translatable("tooltip.mineria.water_barrel.multiple_buckets_stored", buckets, capacity, fluidNameComponent);
            } else {
                if(buckets <= 0) {
                    message = fluidNameComponent.equals(Component.empty()) ? Component.translatable("tooltip.mineria.water_barrel.no_bucket_stored") : Component.translatable("tooltip.mineria.water_barrel.no_fluid_bucket_stored", fluidNameComponent);
                } else {
                    message = Component.translatable("tooltip.mineria.water_barrel.one_bucket_stored", capacity, fluidNameComponent);
                }
            }
        } else {
            message = Component.translatable("tooltip.mineria.water_barrel.infinite");
        }

        player.displayClientMessage(message.withStyle(ChatFormatting.GREEN), true);
    }

    protected boolean fluidInteraction(Level world, BlockPos pos, AbstractWaterBarrelBlockEntity tile, Player player, InteractionHand hand) {
        // If the item count is superior to 1, the capability doesn't work correctly
        ItemStack handItem = player.getItemInHand(hand);
        ItemStack handItemCopy = handItem.copy();
        handItemCopy.setCount(1);
        Optional<IFluidHandlerItem> capOpt = handItemCopy.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).resolve();
        if(capOpt.isEmpty()) {
            return false;
        }
        IFluidHandlerItem fluidHandler = capOpt.get();
        FluidStack fluid = drainFromFluidHandler(fluidHandler, IFluidHandler.FluidAction.SIMULATE);
        Consumer<SoundEvent> soundPlayer = sound -> world.playSound(null, pos, sound, SoundSource.BLOCKS, 1.0F, 1.0F);

        boolean result = fluid.isEmpty() ? removeWaterBucket(tile, soundPlayer, player, fluidHandler, fluid) : addWaterBucket(tile, soundPlayer, player, fluidHandler, fluid);
        if(result && !player.isCreative()) {
            ItemStack containerItem = fluidHandler.getContainer();
            if(handItem.getCount() > 1) {
                handItem.shrink(1);
                if(!player.addItem(containerItem)) {
                    player.drop(containerItem, false);
                }
            } else {
                player.setItemInHand(hand, containerItem);
            }
        }
        return result;
    }

    protected boolean addWaterBucket(AbstractWaterBarrelBlockEntity tile, Consumer<SoundEvent> soundPlayer, Player player, IFluidHandlerItem fluidHandler, FluidStack containedFluid) {
        if (tile.addFluid(containedFluid)) {
            soundPlayer.accept(containedFluid.getFluid().getFluidType().getSound(SoundActions.BUCKET_EMPTY));
            if (!player.isCreative()) {
                drainFromFluidHandler(fluidHandler, IFluidHandler.FluidAction.EXECUTE);
            }

            if (player instanceof ServerPlayer) {
                MineriaCriteriaTriggers.FLUID_BARREL_FILLED.trigger((ServerPlayer) player, this, tile.getCapacity(), tile.getBuckets());
            }
            return true;
        }
        return false;
    }

    protected boolean removeWaterBucket(AbstractWaterBarrelBlockEntity tile, Consumer<SoundEvent> soundPlayer, Player player, IFluidHandlerItem fluidHandler, FluidStack containedFluid) {
        Fluid tileFluid = tile.getContainedFluid();
        if (tile.removeFluid(fluidHandler, containedFluid)) {
            soundPlayer.accept(tileFluid.getFluidType().getSound(SoundActions.BUCKET_FILL));

            if (!player.isCreative()) {
                fluidHandler.fill(new FluidStack(tileFluid, FluidType.BUCKET_VOLUME), IFluidHandler.FluidAction.EXECUTE);
            }
            return true;
        }
        return false;
    }

    @Nonnull
    public FluidStack drainFromFluidHandler(IFluidHandler handler, IFluidHandler.FluidAction action) {
        return handler.drain(new FluidStack(Fluids.WATER, FluidType.BUCKET_VOLUME), action);
    }

    @Override
    public void playerWillDestroy(Level worldIn, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull Player player) {
        BlockEntity tile = worldIn.getBlockEntity(pos);

        if (tile instanceof AbstractWaterBarrelBlockEntity barrel) {
            barrel.setDestroyedByCreativePlayer(player.isCreative());

            if (barrel.shouldDrop()) {
                ItemStack stack = new ItemStack(this);
                barrel.saveToItem(stack);
                popResource(worldIn, pos, stack);
            }
        }

        super.playerWillDestroy(worldIn, pos, state, player);
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable BlockGetter worldIn, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        CompoundTag stackTag = stack.getTag();

        if (stackTag != null && stackTag.contains("BlockEntityTag", 10)) {
            CompoundTag blockEntityTag = stackTag.getCompound("BlockEntityTag");

            if (blockEntityTag.contains("Buckets") && blockEntityTag.contains("Capacity")) {
                if (blockEntityTag.getInt("Buckets") >= 0) {
                    tooltip.add(Component.literal(blockEntityTag.getInt("Buckets") + " " + I18n.get("tooltip.mineria.buckets") + " / " + blockEntityTag.getInt("Capacity")).withStyle(ChatFormatting.GRAY));
                }
            }
        }

        if (KeyboardHelper.isShiftKeyDown()) {
            tooltip.add(Component.translatable("tooltip.mineria.water_barrel.use").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(FastColor.ARGB32.color(255, 31, 255, 244)))));
            addInformationOnShift(stack, worldIn, tooltip, flagIn);
        } else {
            tooltip.add(Component.translatable("tooltip.mineria.water_barrel.hold_shift").withStyle(ChatFormatting.GRAY));
        }
    }

    @OnlyIn(Dist.CLIENT)
    protected abstract void addInformationOnShift(ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn);

    @Override
    public void onRemove(@Nonnull BlockState state, @Nonnull Level worldIn, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        return MineriaBlocks.getItemFromBlock(this).getDefaultInstance();
    }

    public static class WaterBarrelBlockItem<T extends AbstractWaterBarrelBlock> extends BlockItem {
        protected final T barrel;

        public WaterBarrelBlockItem(T barrel, Properties builder) {
            super(barrel, builder);
            this.barrel = barrel;
        }

        @Nonnull
        @Override
        public ItemStack getDefaultInstance() {
            CompoundTag nbt = new CompoundTag();
            CompoundTag blockEntityTag = new CompoundTag();
            blockEntityTag.putInt("Buckets", this.barrel.initialCapacity < 0 ? -1 : 0);
            blockEntityTag.putInt("Capacity", this.barrel.initialCapacity);
            nbt.put("BlockEntityTag", writeAdditional(blockEntityTag));
            return Util.make(new ItemStack(this), stack -> stack.setTag(nbt));
        }

        public CompoundTag writeAdditional(CompoundTag blockEntityTag) {
            return blockEntityTag;
        }
    }
}
