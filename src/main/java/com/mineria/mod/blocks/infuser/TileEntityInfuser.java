package com.mineria.mod.blocks.infuser;

import com.mineria.mod.blocks.barrel.TileEntityBarrel;
import com.mineria.mod.init.ItemsInit;
import com.mineria.mod.util.CustomItemStackHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;

import static net.minecraft.tileentity.TileEntityFurnace.getItemBurnTime;

public class TileEntityInfuser extends TileEntity implements ITickable
{
    private final CustomItemStackHandler inventory = new CustomItemStackHandler(4);

    private int burnTime;
    private int currentBurnTime;
    private int infuseTime;
    //2400
    private int totalInfuseTime = 200;
    private String customName;

    public boolean isUsableByPlayer(EntityPlayer player)
    {
        return this.world.getTileEntity(this.pos) == this && player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
    }

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
    public ITextComponent getDisplayName()
    {
        return this.hasCustomName() ? new TextComponentString(this.customName) : new TextComponentTranslation("container.infuser");
    }

    public boolean hasCustomName()
    {
        return this.customName != null && !this.customName.isEmpty();
    }

    public void setCustomName(String customName)
    {
        this.customName = customName;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.inventory.deserializeNBT(compound.getCompoundTag("Inventory"));
        this.burnTime = compound.getInteger("BurnTime");
        this.infuseTime = compound.getInteger("InfuseTime");
        this.totalInfuseTime = compound.getInteger("InfuseTimeTotal");
        this.currentBurnTime = getItemBurnTime(this.inventory.getStackInSlot(2));

        if (compound.hasKey("CustomName", 8))
            this.customName = compound.getString("CustomName");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setInteger("BurnTime", this.burnTime);
        compound.setInteger("InfuseTime", this.infuseTime);
        compound.setInteger("InfuseTimeTotal", this.totalInfuseTime);
        compound.setTag("Inventory", this.inventory.serializeNBT());

        if (this.hasCustomName())
            compound.setString("CustomName", this.customName);

        return compound;
    }

    public static boolean isItemFuel(ItemStack stack)
    {
        return getItemBurnTime(stack) > 0;
    }

    public boolean isInfusing()
    {
        return this.burnTime > 0;
    }

    @SideOnly(Side.CLIENT)
    public static boolean isInfusing(TileEntityInfuser inventory)
    {
        return inventory.getField(0) > 0;
    }

    @Override
    public void update()
    {
        boolean alreadyInfusing = this.isInfusing();
        boolean dirty = false;

        if (this.isInfusing())
            --this.burnTime;

        if (!this.world.isRemote)
        {
            ItemStack input = this.inventory.getStackInSlot(0);
            ItemStack barrel = this.inventory.getStackInSlot(1);
            ItemStack fuelStack = this.inventory.getStackInSlot(2);

            if (this.isInfusing() || !fuelStack.isEmpty() && !input.isEmpty() || !barrel.isEmpty())
            {
                if (!this.isInfusing() && this.canInfuse())
                {
                    this.burnTime = getItemBurnTime(fuelStack);
                    this.currentBurnTime = this.burnTime;

                    if (this.isInfusing())
                    {
                        dirty = true;

                        if (!fuelStack.isEmpty())
                        {
                            Item fuel = fuelStack.getItem();
                            fuelStack.shrink(1);

                            if (fuelStack.isEmpty())
                            {
                                ItemStack result = fuel.getContainerItem(fuelStack);
                                this.inventory.setStackInSlot(2, result);
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
                        this.infuseItem();
                        dirty = true;
                    }
                }
                else
                    this.infuseTime = 0;
            }
            else if (!this.isInfusing() && this.infuseTime > 0)
            {
                this.infuseTime = MathHelper.clamp(this.infuseTime - 2, 0, this.totalInfuseTime);
            }

            if (alreadyInfusing != this.isInfusing())
            {
                dirty = true;
                BlockInfuser.setState(this.isInfusing(), this.world, this.pos);
            }
        }

        if (dirty)
        {
            this.markDirty();
        }
    }

    public void infuseItem()
    {
        if (this.canInfuse())
        {
            ItemStack input = this.inventory.getStackInSlot(0);
            ItemStack barrel = this.inventory.getStackInSlot(1);
            ItemStack result = InfuserRecipes.getInstance().getInfusingResult(input);
            ItemStack output = this.inventory.getStackInSlot(3);

            if (output.getItem() == ItemsInit.CUP)
            {
                output.shrink(1);
                this.inventory.setStackInSlot(3, result.copy());
            }

            input.shrink(1);
            TileEntityBarrel.decreaseWaterFromStack(barrel);
        }
    }

    private boolean canInfuse()
    {
        ItemStack input = this.inventory.getStackInSlot(0);
        ItemStack barrel = this.inventory.getStackInSlot(1);
        ItemStack output = this.inventory.getStackInSlot(3);
        boolean hasWater = TileEntityBarrel.checkWaterFromStack(barrel);

        if (input.isEmpty() || barrel.isEmpty() || output.isEmpty() || !hasWater)
        {
            return false;
        }
        else
        {
            ItemStack result = InfuserRecipes.getInstance().getInfusingResult(input);

            if (result.isEmpty())
            {
                return false;
            }
            else
            {
                if(output.getCount() != 1)
                {
                    return false;
                }
                else if (output.equals(new ItemStack(ItemsInit.CUP)))
                {
                    return true;
                }
                int res = output.getCount() + result.getCount();
                return res <= 64 && res <= output.getMaxStackSize();
            }
        }
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return (T)this.inventory;
        return super.getCapability(capability, facing);
    }

    public CustomItemStackHandler getInventory()
    {
        return inventory;
    }
}
