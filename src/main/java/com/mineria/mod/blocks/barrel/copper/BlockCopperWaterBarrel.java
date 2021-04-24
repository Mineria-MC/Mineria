package com.mineria.mod.blocks.barrel.copper;

import com.mineria.mod.Mineria;
import com.mineria.mod.blocks.barrel.AbstractBlockWaterBarrel;
import com.mineria.mod.blocks.barrel.AbstractTileEntityWaterBarrel;
import com.mineria.mod.util.GuiHandler;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockCopperWaterBarrel extends AbstractBlockWaterBarrel
{
    public BlockCopperWaterBarrel()
    {
        super(16, 3.5F, 10, 1);
    }

    @Override
    protected void executeAction(World world, BlockPos pos, IBlockState state, AbstractTileEntityWaterBarrel tile, EntityPlayer player, EnumHand hand, EnumFacing facing)
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
                ITextComponent message = new TextComponentString(tile.getBuckets() == 0 ? "There is no Water stored." : (tile.getBuckets() > 1 ? "There are " + tile.getBuckets() + " Water Buckets." : "There is 1 Water Bucket stored"));
                message.setStyle(message.getStyle().setColor(TextFormatting.GREEN));
                player.sendStatusMessage(message, true);
            }
            else
                player.openGui(Mineria.INSTANCE, GuiHandler.GUI_COPPER_BARREL, world, pos.getX(), pos.getY(), pos.getZ());
        }
    }

    @Override
    protected void addInformationOnShift(ItemStack stack, List<String> tooltip, ITooltipFlag advanced)
    {
        tooltip.add(TextFormatting.GOLD.toString().concat(TextFormatting.ITALIC.toString()).concat(I18n.format("tooltip.water_barrel.ability").concat(" : ").concat(I18n.format("tooltip.water_barrel.small_inventory_ability"))));
        tooltip.add(I18n.format("tooltip.water_barrel.view_capacity"));
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityCopperWaterBarrel();
    }
}