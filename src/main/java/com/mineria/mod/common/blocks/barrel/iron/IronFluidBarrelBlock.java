package com.mineria.mod.common.blocks.barrel.iron;

import com.mineria.mod.common.blocks.barrel.AbstractWaterBarrelBlock;
import com.mineria.mod.common.blocks.barrel.AbstractWaterBarrelTileEntity;
import com.mineria.mod.common.init.MineriaCriteriaTriggers;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
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
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class IronFluidBarrelBlock extends AbstractWaterBarrelBlock
{
    public IronFluidBarrelBlock()
    {
        super(4, 10, 1, 24);
    }

    @Override
    protected void interact(World world, BlockPos pos, BlockState state, AbstractWaterBarrelTileEntity tile, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
    {
        ItemStack heldStack = player.getItemInHand(hand);

        if(tile instanceof IronFluidBarrelTileEntity)
        {
            Optional<IFluidHandlerItem> provider = heldStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).resolve();
            IFluidHandlerItem cap;
            if(provider.isPresent() && (cap = provider.get()) instanceof FluidBucketWrapper)
            {
                Fluid fluid = ((FluidBucketWrapper) cap).getFluid().getFluid();
                if(fluid == Fluids.EMPTY)
                    removeFluidBucket(this, world, pos, player, (IronFluidBarrelTileEntity) tile, hand);
                else
                    addFluidBucket(this, world, pos, player, (IronFluidBarrelTileEntity) tile, hand, fluid);
            }
            else
            {
                ITextComponent message = new StringTextComponent(tile.getBuckets() == 0 ? "There is no Fluid stored." : (tile.getBuckets() > 1 ? "There are " + tile.getBuckets() + " Fluid Buckets." : "There is 1 Fluid Bucket stored.")).withStyle(TextFormatting.GREEN);
                player.displayClientMessage(message, true);
            }
        }
    }

    private static void addFluidBucket(IronFluidBarrelBlock block, World world, BlockPos pos, PlayerEntity player, IronFluidBarrelTileEntity tile, Hand hand, Fluid fluid)
    {
        if(tile.storeFluid(fluid))
        {
            world.playSound(null, pos, fluid.getAttributes().getEmptySound(), SoundCategory.BLOCKS, 1.0F, 1.0F);
            if(!player.isCreative())
            {
                player.getItemInHand(hand).shrink(1);
                player.setItemInHand(hand, new ItemStack(Items.BUCKET));
            }

            if(player instanceof ServerPlayerEntity)
            {
                MineriaCriteriaTriggers.FLUID_BARREL_FILLED.trigger((ServerPlayerEntity) player, block, tile.getCapacity(), tile.getBuckets());
            }
        }
    }

    private static void removeFluidBucket(IronFluidBarrelBlock block, World world, BlockPos pos, PlayerEntity player, IronFluidBarrelTileEntity tile, Hand hand)
    {
        Fluid fluid = tile.getFluid();
        if(fluid != null)
        {
            world.playSound(null, pos, fluid.getAttributes().getFillSound(), SoundCategory.BLOCKS, 1.0F, 1.0F);

            if(!player.isCreative())
            {
                player.getItemInHand(hand).shrink(1);
                ItemStack stackToAdd = new ItemStack(fluid.getBucket());
                if(!player.addItem(stackToAdd))
                    player.drop(stackToAdd, false);
            }
        }
    }


    @Override
    protected void addInformationOnShift(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(new TranslationTextComponent("tooltip.mineria.water_barrel.ability").withStyle(TextFormatting.GOLD, TextFormatting.ITALIC).append(" : ").append(new TranslationTextComponent("tooltip.mineria.water_barrel.store_lava")));
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new IronFluidBarrelTileEntity();
    }
}
