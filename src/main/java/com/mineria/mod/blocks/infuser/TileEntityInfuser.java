package com.mineria.mod.blocks.infuser;

import com.mineria.mod.init.ItemsInit;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityInfuser extends TileEntity implements ISidedInventory, ITickable
{
    private static final int[] SLOTS_TOP = new int[] {0};
    private static final int[] SLOTS_MIDDLE = new int[] {1, 3};
    private static final int[] SLOTS_SIDES = new int[] {2};

    private int burnTime;
    private int currentBurnTime;
    private int infuseTime;
    private int totalInfuseTime;

    private String infuserCustomName;

    private NonNullList<ItemStack> infuserItemStacks = NonNullList.<ItemStack>withSize(4, ItemStack.EMPTY);

    @Override
    public int[] getSlotsForFace(EnumFacing side)
    {
        if (side == EnumFacing.WEST)
        {
            return SLOTS_MIDDLE;
        }
        else
        {
            return side == EnumFacing.UP ? SLOTS_TOP : SLOTS_SIDES;
        }
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
    {
        return this.isItemValidForSlot(index, itemStackIn);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
    {
        return false;
    }

    @Override
    public int getSizeInventory()
    {
        return infuserItemStacks.size();
    }

    @Override
    public boolean isEmpty()
    {
        for (ItemStack itemstack : this.infuserItemStacks)
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index)
    {
        return this.infuserItemStacks.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count)
    {
        return ItemStackHelper.getAndSplit(this.infuserItemStacks, index, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index)
    {
        return ItemStackHelper.getAndRemove(this.infuserItemStacks, index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack)
    {
        ItemStack itemstack = this.infuserItemStacks.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        this.infuserItemStacks.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit())
        {
            stack.setCount(this.getInventoryStackLimit());
        }

        if (index == 0 && index + 1 == 1 && !flag)
        {
            ItemStack stack1 = (ItemStack)this.infuserItemStacks.get(index + 1);
            this.totalInfuseTime = this.getInfuseTime(stack, stack1);
            this.infuseTime = 0;
            this.markDirty();
        }
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player)
    {
        if (this.world.getTileEntity(this.pos) != this)
        {
            return false;
        }
        else
        {
            return player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public void openInventory(EntityPlayer player)
    {

    }

    @Override
    public void closeInventory(EntityPlayer player)
    {

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        if (index == 3)
        {
            return stack == new ItemStack(Items.BOWL);
        }
        else if (index != 2)
        {
            return true;
        }
        else
        {
            ItemStack itemstack = this.infuserItemStacks.get(2);
            return isItemFuel(stack);
        }
    }

    @Override
    public int getField(int id)
    {
        switch (id)
        {
            case 0:
                return this.burnTime;
            case 1:
                return this.currentBurnTime;
            case 2:
                return this.infuseTime;
            case 3:
                return this.totalInfuseTime;
            default:
                return 0;
        }
    }

    @Override
    public void setField(int id, int value)
    {
        switch (id)
        {
            case 0:
                this.burnTime = value;
                break;
            case 1:
                this.currentBurnTime = value;
                break;
            case 2:
                this.infuseTime = value;
                break;
            case 3:
                this.totalInfuseTime = value;
        }
    }

    @Override
    public int getFieldCount()
    {
        return 4;
    }

    @Override
    public String getName()
    {
        return this.hasCustomName() ? this.infuserCustomName : "container.infuser";
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return this.hasCustomName() ? new TextComponentString(this.infuserCustomName) : new TextComponentTranslation("container.infuser");
    }

    @Override
    public boolean hasCustomName()
    {
        return this.infuserCustomName != null && !this.infuserCustomName.isEmpty();
    }

    public void setCustomInventoryName(String name)
    {
        this.infuserCustomName = name;
    }

    public static void registerFixesFurnace(DataFixer fixer)
    {
        fixer.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileEntityInfuser.class, new String[] {"Items"}));
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.infuserItemStacks = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.infuserItemStacks);
        this.burnTime = compound.getInteger("BurnTime");
        this.infuseTime = compound.getInteger("InfuseTime");
        this.totalInfuseTime = compound.getInteger("InfuseTimeTotal");
        this.currentBurnTime = getItemBurnTime(this.infuserItemStacks.get(2));

        if (compound.hasKey("CustomName", 8))
        {
            this.infuserCustomName = compound.getString("CustomName");
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setInteger("BurnTime", (short)this.burnTime);
        compound.setInteger("InfuseTime", (short)this.infuseTime);
        compound.setInteger("InfuseTimeTotal", (short)this.totalInfuseTime);
        ItemStackHelper.saveAllItems(compound, this.infuserItemStacks);

        if (this.hasCustomName())
        {
            compound.setString("CustomName", this.infuserCustomName);
        }

        return compound;
    }

    public static int getItemBurnTime(ItemStack stack)
    {
        if (stack.isEmpty())
        {
            return 0;
        }
        else
        {
            int burnTime = net.minecraftforge.event.ForgeEventFactory.getItemBurnTime(stack);
            if (burnTime >= 0) return burnTime;
            Item item = stack.getItem();

            if (item == Item.getItemFromBlock(Blocks.WOODEN_SLAB))
            {
                return 150;
            }
            else if (item == Item.getItemFromBlock(Blocks.WOOL))
            {
                return 100;
            }
            else if (item == Item.getItemFromBlock(Blocks.CARPET))
            {
                return 67;
            }
            else if (item == Item.getItemFromBlock(Blocks.LADDER))
            {
                return 300;
            }
            else if (item == Item.getItemFromBlock(Blocks.WOODEN_BUTTON))
            {
                return 100;
            }
            else if (Block.getBlockFromItem(item).getDefaultState().getMaterial() == Material.WOOD)
            {
                return 300;
            }
            else if (item == Item.getItemFromBlock(Blocks.COAL_BLOCK))
            {
                return 16000;
            }
            else if (item instanceof ItemTool && "WOOD".equals(((ItemTool)item).getToolMaterialName()))
            {
                return 200;
            }
            else if (item instanceof ItemSword && "WOOD".equals(((ItemSword)item).getToolMaterialName()))
            {
                return 200;
            }
            else if (item instanceof ItemHoe && "WOOD".equals(((ItemHoe)item).getMaterialName()))
            {
                return 200;
            }
            else if (item == Items.STICK)
            {
                return 100;
            }
            else if (item != Items.BOW && item != Items.FISHING_ROD)
            {
                if (item == Items.SIGN)
                {
                    return 200;
                }
                else if (item == Items.COAL)
                {
                    return 1600;
                }
                else if (item == Items.LAVA_BUCKET)
                {
                    return 20000;
                }
                else if (item != Item.getItemFromBlock(Blocks.SAPLING) && item != Items.BOWL)
                {
                    if (item == Items.BLAZE_ROD)
                    {
                        return 2400;
                    }
                    else if (item instanceof ItemDoor && item != Items.IRON_DOOR)
                    {
                        return 200;
                    }
                    else
                    {
                        return item instanceof ItemBoat ? 400 : 0;
                    }
                }
                else
                {
                    return 100;
                }
            }
            else
            {
                return 300;
            }
        }
    }

    public int getInfuseTime(ItemStack stack, ItemStack stack2)
    {
        return 2400;
    }

    public static boolean isItemFuel(ItemStack stack)
    {
        return getItemBurnTime(stack) > 0;
    }

    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
    {
        return new ContainerInfuser(playerInventory, this);
    }

    public boolean isInfusing()
    {
        return this.burnTime > 0;
    }

    @SideOnly(Side.CLIENT)
    public static boolean isInfusing(IInventory inventory)
    {
        return inventory.getField(0) > 0;
    }

    @Override
    public void update()
    {
        boolean flag = this.isInfusing();
        boolean flag1 = false;

        if (this.isInfusing())
        {
            --this.burnTime;
        }

        if (!this.world.isRemote)
        {
            ItemStack itemstack = this.infuserItemStacks.get(2);

            if (this.isInfusing() || !itemstack.isEmpty() && !((ItemStack)this.infuserItemStacks.get(0)).isEmpty() || !((ItemStack)this.infuserItemStacks.get(1)).isEmpty())
            {
                if (!this.isInfusing() && this.canInfuse())
                {
                    this.burnTime = getItemBurnTime(itemstack);
                    this.currentBurnTime = this.burnTime;

                    if (this.isInfusing())
                    {
                        flag1 = true;

                        if (!itemstack.isEmpty())
                        {
                            Item item = itemstack.getItem();
                            itemstack.shrink(1);

                            if (itemstack.isEmpty())
                            {
                                ItemStack item1 = item.getContainerItem(itemstack);
                                this.infuserItemStacks.set(2, item1);
                            }
                        }
                    }
                }

                if(this.isInfusing() && this.canInfuse())
                {
                    ++this.infuseTime;

                    if (this.infuseTime == this.totalInfuseTime)
                    {
                        this.infuseTime = 0;
                        this.totalInfuseTime = this.getInfuseTime(this.infuserItemStacks.get(0), this.infuserItemStacks.get(1));
                        this.infuseItem();
                        flag1 = true;
                    }
                }
                else
                {
                    this.infuseTime = 0;
                }
            }
            else if (!this.isInfusing() && this.infuseTime > 0)
            {
                this.infuseTime = MathHelper.clamp(this.infuseTime - 2, 0, this.totalInfuseTime);
            }

            if (flag != this.isInfusing())
            {
                flag1 = true;
                BlockInfuser.setState(this.isInfusing(), this.world, this.pos);
            }
        }

        if (flag1)
        {
            this.markDirty();
        }
    }

    public void infuseItem()
    {
        if (this.canInfuse())
        {
            ItemStack itemstack = this.infuserItemStacks.get(0);
            ItemStack itemstack1 = this.infuserItemStacks.get(1);
            ItemStack itemstack2 = InfuserRecipes.getInstance().getInfusingResult(itemstack, itemstack1);
            ItemStack itemstack3 = this.infuserItemStacks.get(3);

            if (itemstack3.getItem() == ItemsInit.cup)
            {
                itemstack3.shrink(1);
                this.infuserItemStacks.set(3, itemstack2.copy());
            }

            itemstack.shrink(1);
            NBTTagCompound compound = itemstack1.getTagCompound();
            NBTTagCompound compound1 = compound.getCompoundTag("BlockEntityTag");
            compound1.setInteger("Water", compound1.getInteger("Water") - 1);
        }
    }

    private boolean canInfuse()
    {
        ItemStack stack = ((ItemStack)this.infuserItemStacks.get(1));
        NBTTagCompound compound = stack.getTagCompound();
        if(compound == null || !compound.hasKey("BlockEntityTag", 10))
        {
            return false;
        }
        NBTTagCompound compound1 = compound.getCompoundTag("BlockEntityTag");

        if (((ItemStack)this.infuserItemStacks.get(0)).isEmpty() || stack.isEmpty() || ((ItemStack)this.infuserItemStacks.get(3)).isEmpty() || compound1.getInteger("Water") == 0)
        {
            return false;
        }
        else
        {
            ItemStack itemstack = InfuserRecipes.getInstance().getInfusingResult((ItemStack)this.infuserItemStacks.get(0), (ItemStack)this.infuserItemStacks.get(1));

            if (itemstack.isEmpty())
            {
                return false;
            }
            else
            {
                ItemStack itemstack1 = (ItemStack)this.infuserItemStacks.get(3);

                if(itemstack1.getCount() != 1)
                {
                    return false;
                }
                else if (itemstack1.equals(new ItemStack(ItemsInit.cup)))
                {
                    return true;
                }
                int res = itemstack1.getCount() + itemstack.getCount();
                return res <= getInventoryStackLimit() && res <= itemstack1.getMaxStackSize();
            }
        }
    }

    @Override
    public void clear()
    {
        this.infuserItemStacks.clear();
    }
}
