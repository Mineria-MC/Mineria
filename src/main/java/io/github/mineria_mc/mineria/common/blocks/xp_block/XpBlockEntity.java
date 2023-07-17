package io.github.mineria_mc.mineria.common.blocks.xp_block;

import io.github.mineria_mc.mineria.common.containers.XpBlockMenu;
import io.github.mineria_mc.mineria.common.init.MineriaItems;
import io.github.mineria_mc.mineria.common.init.MineriaBlockEntities;
import io.github.mineria_mc.mineria.util.MineriaItemStackHandler;
import io.github.mineria_mc.mineria.util.MineriaLockableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

public class XpBlockEntity extends MineriaLockableBlockEntity {
    private Player player;
    private boolean active;
    private int orbItemDelay = 20;
    private int orbItemTickCount;

    public final ContainerData dataSlots = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> active ? 1 : 0;
                case 1 -> orbItemDelay;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> setActive(value == 1);
                case 1 -> orbItemDelay = value;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    public XpBlockEntity(BlockPos pos, BlockState state) {
        super(MineriaBlockEntities.XP_BLOCK.get(), pos, state, new MineriaItemStackHandler(18));
    }

    @Nonnull
    @Override
    protected Component getDefaultName() {
        return Component.translatable("tile_entity.mineria.xp_block");
    }

    @Nonnull
    @Override
    protected AbstractContainerMenu createMenu(int windowId, @Nonnull Inventory playerInv) {
        return new XpBlockMenu(windowId, playerInv, this, dataSlots);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, XpBlockEntity tile) {
        if (level != null && !level.isClientSide) {
            if (tile.player == null)
                return;

            if (!(tile.player.containerMenu instanceof XpBlockMenu)) {
                tile.onClose();
                return;
            }

            if (tile.active && !tile.isFull() && (tile.player.totalExperience > 0 || tile.player.getAbilities().instabuild)) {
                tile.orbItemTickCount++;
                if (tile.orbItemTickCount % tile.orbItemDelay == 0) {
                    if (tile.addItem() && !tile.player.getAbilities().instabuild) {
                        tile.player.giveExperiencePoints(-1);
                    }
                }
            }
        }
    }

    private boolean isFull() {
        for (ItemStack stack : this.inventory.toNonNullList()) {
            if (stack.getCount() < stack.getMaxStackSize() && stack.getCount() < stack.getMaxStackSize()) {
                return false;
            }
        }
        return true;
    }

    private boolean addItem() {
        ItemStack stack = new ItemStack(MineriaItems.XP_ORB.get());

        for (int index = 0; index < this.inventory.getSlots(); index++) {
            ItemStack slotStack = this.getItem(index);
            if (slotStack.isEmpty()) {
                this.setItem(index, stack);
                return true;
            } else if (canStack(slotStack, stack)) {
                slotStack.grow(1);
                return true;
            }
        }

        return false;
    }

    private boolean canStack(ItemStack stack1, ItemStack stack2) {
        return stack1.getItem() == stack2.getItem() && ItemStack.isSameItemSameTags(stack1, stack2) && stack1.isStackable() && stack1.getCount() < stack1.getMaxStackSize() && stack1.getCount() < getMaxStackSize();
    }

    public void setActive(boolean state) {
        this.active = state;
        this.orbItemTickCount = 0;
    }

    public void onOpen(Player player) {
        this.player = player;
        this.active = false;
        this.orbItemDelay = 20;
        this.orbItemTickCount = 0;
    }

    public void init(Player player) {
        this.player = player;
        this.active = !this.active;
        this.orbItemTickCount = 0;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setOrbItemDelay(int delay) {
        this.orbItemDelay = delay;
    }

    public void onClose() {
        this.player = null;
        this.active = false;
        this.orbItemDelay = 20;
        this.orbItemTickCount = 0;
    }
}