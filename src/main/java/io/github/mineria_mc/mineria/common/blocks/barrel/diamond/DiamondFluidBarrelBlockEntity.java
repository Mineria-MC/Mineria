package io.github.mineria_mc.mineria.common.blocks.barrel.diamond;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.blocks.barrel.AbstractWaterBarrelBlockEntity;
import io.github.mineria_mc.mineria.common.containers.DiamondFluidBarrelMenu;
import io.github.mineria_mc.mineria.common.init.MineriaItems;
import io.github.mineria_mc.mineria.common.init.MineriaBlockEntities;
import io.github.mineria_mc.mineria.util.MineriaItemStackHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class DiamondFluidBarrelBlockEntity extends AbstractWaterBarrelBlockEntity implements MenuProvider {
    protected final UpgradeInventory upgrades;
    protected final OptionalInventory topInv, middleInv, bottomInv;
    @Nonnull
    protected LazyOptional<IItemHandler> upgradesInvCap, topInvCap, middleInvCap, bottomInvCap;
    @Nonnull
    protected Fluid storedFluid = Fluids.EMPTY;
    protected int pumpingTime;

    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            MineriaItemStackHandler inv = invForSlot(index);
            return inv == null ? 0 : inv.getSlots();
        }

        @Override
        public void set(int index, int value) {
            MineriaItemStackHandler inv = invForSlot(index);
            if(inv != null && inv.getSlots() != value) {
                inv.setSize(value);
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    };

    public DiamondFluidBarrelBlockEntity(BlockPos pos, BlockState state) {
        super(MineriaBlockEntities.DIAMOND_FLUID_BARREL.get(), pos, state, 64);
        this.upgrades = new UpgradeInventory(3);
        this.topInv = new OptionalInventory(0);
        this.middleInv = new OptionalInventory(0);
        this.bottomInv = new OptionalInventory(0);
        this.upgradesInvCap = LazyOptional.of(() -> this.upgrades);
        this.topInvCap = LazyOptional.of(() -> this.topInv);
        this.middleInvCap = LazyOptional.of(() -> this.middleInv);
        this.bottomInvCap = LazyOptional.of(() -> this.bottomInv);
    }

    @Nonnull
    @Override
    public Component getDisplayName() {
        return Component.translatable("tile_entity.mineria.diamond_fluid_barrel");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, @Nonnull Inventory playerInv, @Nonnull Player player) {
        return new DiamondFluidBarrelMenu(id, playerInv, this);
    }

    public boolean isInventoryEmpty() {
        return upgrades.isEmpty() && topInv.isEmpty() && middleInv.isEmpty() && bottomInv.isEmpty();
    }

    @Override
    public boolean shouldDrop() {
        return super.shouldDrop() || !isInventoryEmpty();
    }

    @Nonnull
    @Override
    public Fluid getContainedFluid() {
        if(upgrades.containsItem(MineriaItems.BARREL_FLUID_UPGRADE.get())) {
            return buckets == 0 ? Fluids.EMPTY : storedFluid;
        }
        return super.getContainedFluid();
    }

    @Override
    public boolean addFluid(FluidStack stack) {
        if(super.addFluid(stack)) {
            if(storedFluid == Fluids.EMPTY) {
                storedFluid = stack.getFluid();
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean removeFluid(IFluidHandler fluidHandler, FluidStack stack) {
        if(super.removeFluid(fluidHandler, stack)) {
            if(isEmpty()) {
                storedFluid = Fluids.EMPTY;
            }
            return true;
        }
        return false;
    }

    public void serverTick(Level level, BlockPos pos, BlockState state) {
        int pumpingUpgrades = 0;

        for (int i = 0; i < upgrades.getSlots(); i++) {
            ItemStack stack = upgrades.getStackInSlot(i);

            MineriaItemStackHandler inv = invForSlot(i);
            if(inv != null) {
                if(stack.is(MineriaItems.BARREL_INVENTORY_UPGRADE.get())) {
                    if(inv.getSlots() == 0) {
                        inv.setSize(8);
                    }
                } else if(inv.getSlots() > 0) {
                    inv.setSize(0);
                }
            }

            if(stack.is(MineriaItems.BARREL_PUMPING_UPGRADE.get())) {
                pumpingUpgrades++;
            }
        }

        if(pumpingUpgrades > 0) {
            Optional<BlockPos> fluidPos = Direction.stream()
                    .filter(direction -> direction.getAxis().isHorizontal())
                    .map(pos::relative)
                    .filter(position -> {
                        FluidState fluidState = level.getFluidState(position);
                        return !fluidState.isEmpty() && fluidState.isSource() && isValidFluid(fluidState.getType());
                    })
                    .findFirst();
            if(fluidPos.isPresent()) {
                if(pumpingTime > 0) {
                    pumpingTime -= (int) Math.pow(2, pumpingUpgrades - 1);
                } else {
                    pumpingTime = 200;
                    if(addFluid(new FluidStack(level.getFluidState(fluidPos.get()).getType(), FluidType.BUCKET_VOLUME))) {
                        level.setBlock(fluidPos.get(), Blocks.AIR.defaultBlockState(), 3);
                    }
                }
            }
        } else if(pumpingTime != 200) {
            pumpingTime = 200;
        }

        if(upgrades.containsItem(MineriaItems.BARREL_STORAGE_UPGRADE_3.get())) {
            if(capacity != 128) {
                capacity = 128;
                buckets = Math.min(buckets, 128);
            }
        } else if(upgrades.containsItem(MineriaItems.BARREL_STORAGE_UPGRADE_2.get())) {
            if(capacity != 96) {
                capacity = 96;
                buckets = Math.min(buckets, 96);
            }
        } else if(upgrades.containsItem(MineriaItems.BARREL_STORAGE_UPGRADE_1.get())) {
            if(capacity != 80) {
                capacity = 80;
                buckets = Math.min(buckets, 80);
            }
        } else {
            if(capacity != 64) {
                capacity = 64;
                buckets = Math.min(buckets, 64);
            }
        }

        boolean hasNetheriteLook = state.getValue(DiamondFluidBarrelBlock.NETHERITE_LOOK);
        if(upgrades.containsItem(MineriaItems.BARREL_NETHERITE_UPGRADE.get())) {
            if(!hasNetheriteLook) {
                state = state.setValue(DiamondFluidBarrelBlock.NETHERITE_LOOK, true);
                level.setBlock(pos, state, 2);
            }
        } else if(hasNetheriteLook) {
            state = state.setValue(DiamondFluidBarrelBlock.NETHERITE_LOOK, false);
            level.setBlock(pos, state, 2);
        }
    }

    @Nullable
    public MineriaItemStackHandler invForSlot(int slot) {
        return switch (slot) {
            case 0 -> topInv;
            case 1 -> middleInv;
            case 2 -> bottomInv;
            default -> null;
        };
    }

    @Override
    public void saveAdditional(@Nonnull CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putBoolean("NetheriteLook", upgrades.containsItem(MineriaItems.BARREL_NETHERITE_UPGRADE.get())); // For ItemStack NBT
        nbt.put("UpgradeInventory", upgrades.serializeNBT());
        nbt.put("TopInventory", topInv.serializeNBT());
        nbt.put("MiddleInventory", middleInv.serializeNBT());
        nbt.put("BottomInventory", bottomInv.serializeNBT());
        ResourceLocation id = ForgeRegistries.FLUIDS.getKey(storedFluid);
        nbt.putString("StoredFluid", id == null ? "minecraft:empty" : id.toString());
        nbt.putInt("PumpingTime", pumpingTime);
    }

    @Override
    public void load(@Nonnull CompoundTag nbt) {
        super.load(nbt);
        this.upgrades.deserializeNBT(nbt.getCompound("UpgradeInventory"));
        this.topInv.deserializeNBT(nbt.getCompound("TopInventory"));
        this.middleInv.deserializeNBT(nbt.getCompound("MiddleInventory"));
        this.bottomInv.deserializeNBT(nbt.getCompound("BottomInventory"));
        loadStoredFluid(nbt);
        this.pumpingTime = nbt.getInt("PumpingTime");
    }

    protected void loadStoredFluid(@Nonnull CompoundTag nbt) {
        Fluid fluid;
        if(upgrades.containsItem(MineriaItems.BARREL_FLUID_UPGRADE.get())) {
            fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(nbt.getString("StoredFluid")));
            if(fluid == null) {
                BlockPos pos = BlockEntity.getPosFromTag(nbt);
                if(!pos.equals(BlockPos.ZERO)) {
                    Mineria.LOGGER.warn("[{}] Loaded Diamond Fluid Barrel block entity with an absent fluid! The barrel will be emptied.", pos.toShortString());
                }
                this.buckets = 0;
                fluid = Fluids.EMPTY;
            }
        } else {
            fluid = Fluids.WATER;
        }
        this.storedFluid = fluid;
    }

    @Nonnull
    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public MineriaItemStackHandler getUpgradeInventory() {
        return upgrades;
    }

    public MineriaItemStackHandler getTopInv() {
        return topInv;
    }

    public MineriaItemStackHandler getMiddleInv() {
        return middleInv;
    }

    public MineriaItemStackHandler getBottomInv() {
        return bottomInv;
    }

    public ContainerData getContainerData() {
        return data;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ITEM_HANDLER) {
            if(side == null) {
                return upgradesInvCap.cast();
            }

            final LazyOptional<IItemHandler> opt = switch (side) {
                case UP -> topInvCap;
                case NORTH -> middleInvCap;
                case DOWN -> bottomInvCap;
                default -> upgradesInvCap;
            };
            return opt.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        upgradesInvCap.invalidate();
        topInvCap.invalidate();
        middleInvCap.invalidate();
        bottomInvCap.invalidate();
    }

    @Override
    public void reviveCaps() {
        super.reviveCaps();
        upgradesInvCap = LazyOptional.of(() -> this.upgrades);
        topInvCap = LazyOptional.of(() -> this.topInv);
        middleInvCap = LazyOptional.of(() -> this.middleInv);
        bottomInvCap = LazyOptional.of(() -> this.bottomInv);
    }

    public class UpgradeInventory extends MineriaItemStackHandler {
        public UpgradeInventory(int size, ItemStack... stacks) {
            super(size, stacks);
        }

        public boolean containsItem(Item item) {
            return stacks.stream().anyMatch(stack -> stack.is(item));
        }

        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            setChanged();
        }

        @Override
        public void clear() {
            for (int i = 0; i < getSlots(); i++) {
                extractItem(i, Integer.MAX_VALUE, false);
            }
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (amount == 0) {
                return ItemStack.EMPTY;
            }
            validateSlotIndex(slot);
            ItemStack existing = this.stacks.get(slot);
            if(existing.is(MineriaItems.BARREL_INVENTORY_UPGRADE.get())) {
                MineriaItemStackHandler inv = invForSlot(slot);
                if(inv != null && !inv.isEmpty()) {
                    return ItemStack.EMPTY;
                }
            }
            if(existing.is(MineriaItems.BARREL_FLUID_UPGRADE.get())) {
                if(storedFluid != Fluids.EMPTY && storedFluid != Fluids.WATER) {
                    return ItemStack.EMPTY;
                }
            }
            return super.extractItem(slot, amount, simulate);
        }
    }

    public class OptionalInventory extends MineriaItemStackHandler {
        public OptionalInventory(int size, ItemStack... stacks) {
            super(size, stacks);
        }

        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            setChanged();
        }

        @Override
        public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
            if(getSlots() == 0) {
                return;
            }
            super.setStackInSlot(slot, stack);
        }

        @Nonnull
        @Override
        public ItemStack getStackInSlot(int slot) {
            if(getSlots() == 0) {
                return ItemStack.EMPTY;
            }
            return super.getStackInSlot(slot);
        }

        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            if(getSlots() == 0) {
                return ItemStack.EMPTY;
            }
            return super.insertItem(slot, stack, simulate);
        }

        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            if(getSlots() == 0) {
                return ItemStack.EMPTY;
            }
            return super.extractItem(slot, amount, simulate);
        }
    }
}
