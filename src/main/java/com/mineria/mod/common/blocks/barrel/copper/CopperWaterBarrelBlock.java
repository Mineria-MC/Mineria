package com.mineria.mod.common.blocks.barrel.copper;

import com.mineria.mod.common.blocks.barrel.AbstractWaterBarrelBlock;
import com.mineria.mod.common.blocks.barrel.AbstractWaterBarrelTileEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;

public class CopperWaterBarrelBlock extends AbstractWaterBarrelBlock
{
    public CopperWaterBarrelBlock()
    {
        super(3.5F, 10, 16);
    }

    @Override
    protected void interact(Level world, BlockPos pos, BlockState state, AbstractWaterBarrelTileEntity tile, Player player, InteractionHand hand, BlockHitResult hit)
    {
        Item heldItem = player.getItemInHand(hand).getItem();

        if(heldItem.equals(Items.WATER_BUCKET))
            addWaterBucket(this, world, pos, player, tile, hand);
        else if(heldItem.equals(Items.BUCKET))
            removeWaterBucket(this, world, pos, player, tile, hand);
        else
        {
            if(player.isShiftKeyDown())
            {
                Component message = new TextComponent(tile.getBuckets() == 0 ? "There is no Water stored." : (tile.getBuckets() > 1 ? "There are " + tile.getBuckets() + " Water Buckets." : "There is 1 Water Bucket stored.")).withStyle(ChatFormatting.GREEN);
                player.displayClientMessage(message, true);
            }
            else
                NetworkHooks.openGui((ServerPlayer) player, (CopperWaterBarrelTileEntity) tile, pos);
        }
    }

    @Override
    protected void addInformationOnShift(ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn)
    {
        tooltip.add(new TranslatableComponent("tooltip.mineria.water_barrel.ability").withStyle(ChatFormatting.GOLD, ChatFormatting.ITALIC).append(" : ").append(new TranslatableComponent("tooltip.mineria.water_barrel.small_inventory_ability")));
        tooltip.add(new TranslatableComponent("tooltip.mineria.water_barrel.view_capacity").withStyle(ChatFormatting.GRAY));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState)
    {
        return new CopperWaterBarrelTileEntity(pPos, pState);
    }
}
