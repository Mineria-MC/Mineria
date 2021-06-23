package com.mineria.mod.blocks.barrel.iron;

import com.mineria.mod.blocks.barrel.AbstractWaterBarrelBlock;
import com.mineria.mod.blocks.barrel.AbstractWaterBarrelTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class IronFluidBarrelBlock extends AbstractWaterBarrelBlock
{
    public IronFluidBarrelBlock()
    {
        super(4, 10, 1, 24);
    }

    @Override
    protected void interact(World world, BlockPos pos, BlockState state, AbstractWaterBarrelTileEntity tile, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
    {
        Item heldItem = player.getHeldItem(hand).getItem();

        if(tile instanceof IronFluidBarrelTileEntity)
        {
            if(heldItem instanceof BucketItem)
            {
                Fluid fluid = ((BucketItem) heldItem).getFluid();
                if(fluid == Fluids.EMPTY)
                    removeFluidBucket(world, pos, player, (IronFluidBarrelTileEntity) tile, hand);
                else
                    addFluidBucket(world, pos, player, (IronFluidBarrelTileEntity) tile, hand, fluid);
            }
            else
            {
                ITextComponent message = new StringTextComponent(tile.getBuckets() == 0 ? "There is no Fluid stored." : (tile.getBuckets() > 1 ? "There are " + tile.getBuckets() + " Fluid Buckets." : "There is 1 Fluid Bucket stored.")).mergeStyle(TextFormatting.GREEN);
                player.sendStatusMessage(message, true);
            }
        }
    }

    private static void addFluidBucket(World world, BlockPos pos, PlayerEntity player, IronFluidBarrelTileEntity tile, Hand hand, Fluid fluid)
    {
        if(tile.storeFluid(fluid))
        {
            world.playSound(null, pos, fluid.getAttributes().getEmptySound(), SoundCategory.BLOCKS, 1.0F, 1.0F);
            if(!player.isCreative())
            {
                player.getHeldItem(hand).shrink(1);
                player.setHeldItem(hand, new ItemStack(Items.BUCKET));
            }
        }
    }

    private static void removeFluidBucket(World world, BlockPos pos, PlayerEntity player, IronFluidBarrelTileEntity tile, Hand hand)
    {
        Fluid fluid = tile.getFluid();
        if(fluid != null)
        {
            world.playSound(null, pos, fluid.getAttributes().getFillSound(), SoundCategory.BLOCKS, 1.0F, 1.0F);

            if(!player.isCreative())
            {
                player.getHeldItem(hand).shrink(1);
                ItemStack stackToAdd = new ItemStack(fluid.getFilledBucket());
                if(!player.addItemStackToInventory(stackToAdd))
                    player.dropItem(stackToAdd, false);
            }
        }
    }


    @Override
    protected void addInformationOnShift(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(new TranslationTextComponent("tooltip.mineria.water_barrel.ability").mergeStyle(TextFormatting.GOLD, TextFormatting.ITALIC).appendString(" : ").appendSibling(new TranslationTextComponent("tooltip.mineria.water_barrel.store_lava")));
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new IronFluidBarrelTileEntity();
    }
}
