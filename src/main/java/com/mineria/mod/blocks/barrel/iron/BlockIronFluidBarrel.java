package com.mineria.mod.blocks.barrel.iron;

import com.mineria.mod.blocks.barrel.AbstractBlockWaterBarrel;
import com.mineria.mod.blocks.barrel.AbstractTileEntityWaterBarrel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;

import javax.annotation.Nullable;
import java.util.List;

public class BlockIronFluidBarrel extends AbstractBlockWaterBarrel
{
    public BlockIronFluidBarrel()
    {
        super(24, 4, 10, 1);
    }

    @Override
    protected void executeAction(World world, BlockPos pos, IBlockState state, AbstractTileEntityWaterBarrel tile, EntityPlayer player, EnumHand hand, EnumFacing facing)
    {
        ItemStack heldStack = player.getHeldItem(hand);
        Item heldItem = heldStack.getItem();

        if(tile instanceof TileEntityIronFluidBarrel)
        {
            ICapabilityProvider cap = heldItem.initCapabilities(heldStack, null);
            if(cap instanceof FluidBucketWrapper)
            {
                FluidStack fluidStack = ((FluidBucketWrapper) cap).getFluid();
                if(fluidStack == null)
                    removeFluidBucket(world, pos, player, (TileEntityIronFluidBarrel) tile, hand);
                else if(fluidStack.amount == Fluid.BUCKET_VOLUME)
                    addFluidBucket(world, pos, player, (TileEntityIronFluidBarrel) tile, hand, fluidStack.getFluid());
            }
            else
            {
                ITextComponent message = new TextComponentString(tile.getBuckets() == 0 ? "There is no Fluid stored." : (tile.getBuckets() > 1 ? "There are " + tile.getBuckets() + " Fluid Buckets." : "There is 1 Fluid Bucket stored"));
                message.setStyle(message.getStyle().setColor(TextFormatting.GREEN));
                player.sendStatusMessage(message, true);
            }
        }
    }

    private static void addFluidBucket(World world, BlockPos pos, EntityPlayer player, TileEntityIronFluidBarrel tile, EnumHand hand, Fluid fluid)
    {
        if(tile.storeFluid(fluid))
        {
            world.playSound(null, pos, fluid.getEmptySound(), SoundCategory.BLOCKS, 1.0F, 1.0F);
            if(!player.capabilities.isCreativeMode)
            {
                player.getHeldItem(hand).shrink(1);
                player.setHeldItem(hand, new ItemStack(Items.BUCKET));
            }
        }
    }

    private static void removeFluidBucket(World world, BlockPos pos, EntityPlayer player, TileEntityIronFluidBarrel tile, EnumHand hand)
    {
        Fluid fluid = tile.getFluid();
        if(fluid != null)
        {
            world.playSound(null, pos, fluid.getFillSound(), SoundCategory.BLOCKS, 1.0F, 1.0F);

            if(!player.capabilities.isCreativeMode)
            {
                player.getHeldItem(hand).shrink(1);
                ItemStack stackToAdd = FluidUtil.getFilledBucket(new FluidStack(fluid, Fluid.BUCKET_VOLUME));
                if(!player.addItemStackToInventory(stackToAdd))
                    player.dropItem(stackToAdd, false);
            }
        }
    }

    @Override
    protected void addInformationOnShift(ItemStack stack, List<String> tooltip, ITooltipFlag advanced)
    {
        tooltip.add(TextFormatting.GOLD.toString().concat(TextFormatting.ITALIC.toString()).concat(I18n.format("tooltip.water_barrel.ability")).concat(" : ").concat(I18n.format("tooltip.water_barrel.store_lava")));
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityIronFluidBarrel();
    }
}
