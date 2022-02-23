package com.mineria.mod.common.blocks.xp_block;

import com.mineria.mod.common.containers.XpBlockContainer;
import com.mineria.mod.common.init.MineriaItems;
import com.mineria.mod.common.init.MineriaTileEntities;
import com.mineria.mod.util.CustomItemStackHandler;
import com.mineria.mod.util.MineriaLockableTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class XpBlockTileEntity extends MineriaLockableTileEntity
{
    private Player player;
    private boolean active;
    private int orbItemDelay = 20;
    private int orbItemTickCount;

    public final ContainerData dataSlots = new ContainerData()
    {
        @Override
        public int get(int index)
        {
            switch (index)
            {
                case 0:
                    return active ? 1 : 0;
                case 1:
                    return orbItemDelay;
            }
            return 0;
        }

        @Override
        public void set(int index, int value)
        {
            switch (index)
            {
                case 0:
                    setActive(value == 1);
                case 1:
                    orbItemDelay = value;
            }
        }

        @Override
        public int getCount()
        {
            return 2;
        }
    };

    public XpBlockTileEntity(BlockPos pos, BlockState state)
    {
        super(MineriaTileEntities.XP_BLOCK.get(), pos, state, new CustomItemStackHandler(18));
    }

    @Override
    protected Component getDefaultName()
    {
        return new TranslatableComponent("tile_entity.mineria.xp_block");
    }

    /*@Nullable
    @Override
    public Container createMenu(int windowId, PlayerInventory playerInv, PlayerEntity player)
    {
        return super.createMenu(windowId, playerInv, player);
    }*/

    @Override
    protected AbstractContainerMenu createMenu(int windowId, Inventory playerInv)
    {
        return new XpBlockContainer(windowId, playerInv, this, dataSlots);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, XpBlockTileEntity tile)
    {
        if(level != null && !level.isClientSide)
        {
            if(tile.player == null)
                return;

            if(!(tile.player.containerMenu instanceof XpBlockContainer))
            {
                tile.onClose();
                return;
            }

            if(tile.active && !tile.isFull() && (tile.player.totalExperience > 0 || tile.player.getAbilities().instabuild))
            {
                tile.orbItemTickCount++;
                if(tile.orbItemTickCount % tile.orbItemDelay == 0)
                {
                    if(tile.addItem() && !tile.player.getAbilities().instabuild)
                    {
                        tile.player.giveExperiencePoints(-1);
                    }
                }
            }
        }
    }

    private boolean isFull()
    {
        for(ItemStack stack : this.inventory.toNonNullList())
        {
            if(stack.getCount() < stack.getMaxStackSize() && stack.getCount() < stack.getMaxStackSize())
            {
                return false;
            }
        }
        return true;
    }

    private boolean addItem()
    {
        ItemStack stack = new ItemStack(MineriaItems.XP_ORB);

        for(int index = 0; index < this.inventory.getSlots(); index++)
        {
            ItemStack slotStack = this.getItem(index);
            if(slotStack.isEmpty())
            {
                this.setItem(index, stack);
                return true;
            }
            else if(canStack(slotStack, stack))
            {
                slotStack.grow(1);
                return true;
            }
        }

        return false;
    }

    private boolean canStack(ItemStack stack1, ItemStack stack2)
    {
        return stack1.getItem() == stack2.getItem() && ItemStack.tagMatches(stack1, stack2) && stack1.isStackable() && stack1.getCount() < stack1.getMaxStackSize() && stack1.getCount() < getMaxStackSize();
    }

    public void setActive(boolean state)
    {
        this.active = state;
        this.orbItemTickCount = 0;
    }

    public void onOpen(Player player)
    {
        this.player = player;
        this.active = false;
        this.orbItemDelay = 20;
        this.orbItemTickCount = 0;
    }

    public void init(Player player)
    {
        this.player = player;
        this.active = !this.active;
        this.orbItemTickCount = 0;
    }

    public void setPlayer(Player player)
    {
        this.player = player;
    }

    public void setOrbItemDelay(int delay)
    {
        this.orbItemDelay = delay;
    }

    public void onClose()
    {
        this.player = null;
        this.active = false;
        this.orbItemDelay = 20;
        this.orbItemTickCount = 0;
    }

    /*public XpBlockTileEntity()
    {
        super(MineriaTileEntities.XP_BLOCK.get(), new CustomItemStackHandler(1));
    }

    @Override
    protected ITextComponent getDefaultName()
    {
        return new TranslationTextComponent("tile_entity.mineria.xp_block");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player)
    {
        return new XpBlockContainer(id, player, this);
    }

    @Override
    public int getMaxStackSize()
    {
        return 16;
    }

    private void spawnXp(PlayerEntity player)
    {
        ItemStack stack = new ItemStack(ItemsInit.XP_ORB);
        ItemStack stack1 = this.inventory.getStackInSlot(0);
        if (stack1.getCount() < this.getMaxStackSize())
        {
            if (player.abilities.instabuild)
            {
                if (stack1.isEmpty())
                {
                    this.inventory.setStackInSlot(0, stack.copy());
                } else if (stack1.sameItem(stack))
                {
                    stack1.grow(1);
                }
            } else if (player.totalExperience > 0)
            {
                player.giveExperiencePoints(-1);
                if (stack1.isEmpty())
                {
                    this.inventory.setStackInSlot(0, stack.copy());
                } else if (stack1.sameItem(stack))
                {
                    stack1.grow(1);
                }
            }
        }
    }

    public static void spawnXp(BlockPos pos, World world, PlayerEntity player)
    {
        TileEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity instanceof XpBlockTileEntity)
            ((XpBlockTileEntity) tileEntity).spawnXp(player);
    }*/


}