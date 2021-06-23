package com.mineria.mod.blocks.barrel.copper;

import com.mineria.mod.blocks.barrel.AbstractWaterBarrelBlock;
import com.mineria.mod.blocks.barrel.AbstractWaterBarrelTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;

public class CopperWaterBarrelBlock extends AbstractWaterBarrelBlock
{
    public CopperWaterBarrelBlock()
    {
        super(3.5F, 10, 1, 16);
    }

    @Override
    protected void interact(World world, BlockPos pos, BlockState state, AbstractWaterBarrelTileEntity tile, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
    {
        Item heldItem = player.getHeldItem(hand).getItem();

        if(heldItem.equals(Items.WATER_BUCKET))
            addWaterBucket(world, pos, player, tile, hand);
        else if(heldItem.equals(Items.BUCKET))
            removeWaterBucket(world, pos, player, tile, hand);
        else
        {
            if(player.isSneaking())
            {
                ITextComponent message = new StringTextComponent(tile.getBuckets() == 0 ? "There is no Water stored." : (tile.getBuckets() > 1 ? "There are " + tile.getBuckets() + " Water Buckets." : "There is 1 Water Bucket stored.")).mergeStyle(TextFormatting.GREEN);
                player.sendStatusMessage(message, true);
            }
            else
                NetworkHooks.openGui((ServerPlayerEntity) player, (CopperWaterBarrelTileEntity) tile, pos);
        }
    }

    @Override
    protected void addInformationOnShift(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(new TranslationTextComponent("tooltip.mineria.water_barrel.ability").mergeStyle(TextFormatting.GOLD, TextFormatting.ITALIC).appendString(" : ").appendSibling(new TranslationTextComponent("tooltip.mineria.water_barrel.small_inventory_ability")));
        tooltip.add(new TranslationTextComponent("tooltip.mineria.water_barrel.view_capacity").mergeStyle(TextFormatting.GRAY));
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new CopperWaterBarrelTileEntity();
    }
}
