package com.mineria.mod.common.blocks.barrel.iron;

import com.mineria.mod.common.blocks.barrel.AbstractWaterBarrelBlock;
import com.mineria.mod.common.blocks.barrel.AbstractWaterBarrelTileEntity;
import com.mineria.mod.common.init.MineriaCriteriaTriggers;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
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
        super(4, 10, 24);
    }

    @Override
    protected void interact(Level world, BlockPos pos, BlockState state, AbstractWaterBarrelTileEntity tile, Player player, InteractionHand hand, BlockHitResult hit)
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
                Component message = new TextComponent(tile.getBuckets() == 0 ? "There is no Fluid stored." : (tile.getBuckets() > 1 ? "There are " + tile.getBuckets() + " Fluid Buckets." : "There is 1 Fluid Bucket stored.")).withStyle(ChatFormatting.GREEN);
                player.displayClientMessage(message, true);
            }
        }
    }

    private static void addFluidBucket(IronFluidBarrelBlock block, Level world, BlockPos pos, Player player, IronFluidBarrelTileEntity tile, InteractionHand hand, Fluid fluid)
    {
        if(tile.storeFluid(fluid))
        {
            world.playSound(null, pos, fluid.getAttributes().getEmptySound(), SoundSource.BLOCKS, 1.0F, 1.0F);
            if(!player.isCreative())
            {
                player.getItemInHand(hand).shrink(1);
                player.setItemInHand(hand, new ItemStack(Items.BUCKET));
            }

            if(player instanceof ServerPlayer)
            {
                MineriaCriteriaTriggers.FLUID_BARREL_FILLED.trigger((ServerPlayer) player, block, tile.getCapacity(), tile.getBuckets());
            }
        }
    }

    private static void removeFluidBucket(IronFluidBarrelBlock block, Level world, BlockPos pos, Player player, IronFluidBarrelTileEntity tile, InteractionHand hand)
    {
        Fluid fluid = tile.getFluid();
        if(fluid != null)
        {
            world.playSound(null, pos, fluid.getAttributes().getFillSound(), SoundSource.BLOCKS, 1.0F, 1.0F);

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
    protected void addInformationOnShift(ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn)
    {
        tooltip.add(new TranslatableComponent("tooltip.mineria.water_barrel.ability").withStyle(ChatFormatting.GOLD, ChatFormatting.ITALIC).append(" : ").append(new TranslatableComponent("tooltip.mineria.water_barrel.store_lava")));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState)
    {
        return new IronFluidBarrelTileEntity(pPos, pState);
    }
}
