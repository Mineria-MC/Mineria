package io.github.mineria_mc.mineria.common.block.xp_block;

import io.github.mineria_mc.mineria.common.containers.XpBlockMenu;
import io.github.mineria_mc.mineria.common.registries.MineriaBlockEntityRegistry;
import io.github.mineria_mc.mineria.common.registries.MineriaItemRegistry;
import io.github.mineria_mc.mineria.util.MineriaLockableBlockEntity;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class XpBlockEntity extends MineriaLockableBlockEntity implements ExtendedScreenHandlerFactory {

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
        super(MineriaBlockEntityRegistry.XP_BLOCK, pos, state, NonNullList.withSize(18, ItemStack.EMPTY));
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("tile_entity.mineria.xp_block");
    }

    @Override
    protected AbstractContainerMenu createMenu(int windowId, Inventory playerInv) {
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
        for (ItemStack stack : this.inventory) {
            if (stack.getCount() < stack.getMaxStackSize() && stack.getCount() < stack.getMaxStackSize()) {
                return false;
            }
        }
        return true;
    }

    private boolean addItem() {
        ItemStack stack = new ItemStack(MineriaItemRegistry.XP_ORB);

        for (int index = 0; index < this.inventory.size(); index++) {
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

    @Override
    public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
        buf.writeBlockPos(worldPosition);
    }
}
