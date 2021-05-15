package com.mineria.mod.blocks.barrel.diamond;

import com.mineria.mod.blocks.barrel.AbstractTileEntityWaterBarrel;
import com.mineria.mod.blocks.barrel.iron.FluidBarrel;
import com.mineria.mod.items.ItemBarrelUpgrade;
import com.mineria.mod.util.CustomItemStackHandler;
import com.mineria.mod.util.MineriaUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.stream.Collectors;

/** Not finished
 * We currently have issues with the diamond fluid barrel, so it'll be released later.
 */
public class TileEntityDiamondFluidBarrel extends AbstractTileEntityWaterBarrel implements FluidBarrel, ITickable
{
    private final DiamondFluidBarrelISH upgrades = new DiamondFluidBarrelISH(3);
    private final DiamondFluidBarrelISH inventory = new DiamondFluidBarrelISH(0);
    private final DiamondFluidBarrelISH potions = new DiamondFluidBarrelISH(0);

    @Nullable
    private Fluid storedFluid;

    public TileEntityDiamondFluidBarrel()
    {
        super(64);
    }

    @Nullable
    @Override
    public ITextComponent getDisplayName()
    {
        return new TextComponentTranslation("container.diamond_fluid_barrel");
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        if(facing != null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            if(facing == EnumFacing.UP)
                return (T) upgrades;
            else if(facing == EnumFacing.DOWN)
                return (T) potions;
            else
                return (T) inventory;
        }

        return super.getCapability(capability, facing);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        if(storedFluid != null)
            compound.setString("StoredFluid", storedFluid.getName());
        compound.setTag("Upgrades", this.upgrades.serializeNBT());
        compound.setTag("Inventory", this.inventory.serializeNBT());
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.storedFluid = compound.hasKey("StoredFluid") ? FluidRegistry.getFluid(compound.getString("StoredFluid")) : null;
        this.upgrades.deserializeNBT(compound.getCompoundTag("Upgrades"));
        this.inventory.deserializeNBT(compound.getCompoundTag("Inventory"));
    }

    public boolean isInventoryEmpty()
    {
        for(ItemStack stack : this.upgrades.toNonNullList())
        {
            if(!stack.isEmpty())
                return false;
        }

        for(ItemStack stack : this.inventory.toNonNullList())
        {
            if(!stack.isEmpty())
                return false;
        }

        for(ItemStack stack : this.potions.toNonNullList())
        {
            if(!stack.isEmpty())
                return false;
        }

        return true;
    }

    @Override
    public boolean shouldDrop()
    {
        return super.shouldDrop() || !isInventoryEmpty();
    }

    public boolean isUsableByPlayer(EntityPlayer player)
    {
        return this.world.getTileEntity(this.pos) == this && player.getDistanceSq(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public boolean storeFluid(Fluid fluidToStore)
    {
        return (MineriaUtils.doIf(this.storedFluid, Objects::isNull, fluid -> this.storedFluid = fluidToStore) || this.storedFluid == fluidToStore) && super.increaseFluidBuckets();
    }

    @Nullable
    @Override
    public Fluid getFluid()
    {
        super.decreaseFluidBuckets();
        Fluid result = storedFluid;
        if(this.isEmpty() && hasUpgrade(null))
            storedFluid = null;
        return result;
    }

    public boolean hasUpgrade(ItemBarrelUpgrade upgrade)
    {
        return this.upgrades.toNonNullList().stream().map(ItemStack::getItem).collect(Collectors.toList()).contains(upgrade);
    }

    public DiamondFluidBarrelISH getInventory()
    {
        return inventory;
    }

    public DiamondFluidBarrelISH getPotionsInventory()
    {
        return potions;
    }

    private void applyUpgrades()
    {
        for(ItemStack stack : this.upgrades.toNonNullList())
        {
            MineriaUtils.doIfInstance(stack.getItem(), ItemBarrelUpgrade.class, upgrade -> upgrade.applyUpgrade(this));
        }
    }

    @Override
    public void onLoad()
    {
        applyUpgrades();
    }

    public static TileEntityDiamondFluidBarrel create()
    {
        TileEntityDiamondFluidBarrel barrel = new TileEntityDiamondFluidBarrel();
        barrel.storedFluid = FluidRegistry.WATER;
        return barrel;
    }

    @Override
    public void update()
    {

    }

    public static class DiamondFluidBarrelISH extends CustomItemStackHandler
    {
        @Nullable
        private Container container = null;

        public DiamondFluidBarrelISH(int size)
        {
            super(size);
        }

        @Override
        public void setSize(int size)
        {
            super.setSize(size);
            if(this.container != null) ((ContainerDiamondFluidBarrel) container).addSlots();
        }

        public void setContainer(@Nullable Container container)
        {
            this.container = container;
        }
    }
}
