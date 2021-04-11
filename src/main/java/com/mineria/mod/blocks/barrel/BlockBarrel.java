package com.mineria.mod.blocks.barrel;

import com.mineria.mod.blocks.MineriaBlock;
import com.mineria.mod.util.KeyboardHelper;
import com.mineria.mod.util.MineriaUtils;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

@SuppressWarnings("deprecation")
public class BlockBarrel extends MineriaBlock implements ITileEntityProvider
{
    private final int maxBuckets;

    public BlockBarrel(int maxBuckets)
    {
        super(Material.WOOD, new BlockProperties().hardnessAndResistance(2, 10).creativeTab(null).sounds(SoundType.WOOD).harvestLevel(0, "axe"));
        this.maxBuckets = maxBuckets;
    }

    @SideOnly(Side.CLIENT)
    public boolean hasCustomBreakingProgress(IBlockState state)
    {
        return true;
    }

    @Override
    public EnumPushReaction getMobilityFlag(IBlockState state)
    {
        return EnumPushReaction.BLOCK;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if(!worldIn.isRemote)
        {
            TileEntity tileAtPos = worldIn.getTileEntity(pos);

            if(tileAtPos instanceof TileEntityBarrel)
            {
                TileEntityBarrel tile = (TileEntityBarrel) tileAtPos;
                Item heldItem = player.getHeldItem(hand).getItem();

                if(heldItem.equals(Items.WATER_BUCKET))
                {
                    if(tile.increaseWaterBuckets())
                    {
                        worldIn.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        if(!player.capabilities.isCreativeMode)
                        {
                            player.getHeldItem(hand).shrink(1);
                            player.setHeldItem(hand, new ItemStack(Items.BUCKET));
                        }
                    }
                }
                else if(heldItem.equals(Items.BUCKET))
                {
                    if(tile.decreaseWaterBuckets())
                    {
                        worldIn.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);

                        if(!player.capabilities.isCreativeMode)
                        {
                            player.getHeldItem(hand).shrink(1);
                            ItemStack stackToAdd = new ItemStack(Items.WATER_BUCKET);
                            if(!player.addItemStackToInventory(stackToAdd))
                                player.dropItem(stackToAdd, false);
                        }
                    }
                }
                else
                {
                    ITextComponent text = new TextComponentString(tile.getWaterBuckets() == 0 ? "There is no Water stored." : (tile.getWaterBuckets() > 1 ? "There are " + tile.getWaterBuckets() + " Water Buckets." : "There is 1 Water Bucket stored"));
                    text.setStyle(text.getStyle().setColor(TextFormatting.AQUA));
                    player.sendStatusMessage(text, true);
                }
            }
        }

        return true;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntity tile = worldIn.getTileEntity(pos);

        if(tile instanceof TileEntityBarrel)
        {
            TileEntityBarrel barrel = (TileEntityBarrel)tile;

            if(barrel.shouldDrop())
            {
                ItemStack stack = new ItemStack(this);
                NBTTagCompound stackTag = new NBTTagCompound();
                NBTTagCompound blockEntityTag = new NBTTagCompound();
                stackTag.setTag("BlockEntityTag", barrel.writeToNBT(blockEntityTag));
                stack.setTagCompound(stackTag);

                spawnAsEntity(worldIn, pos, stack);
            }
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public int quantityDropped(Random random)
    {
        return 0;
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
    {
        if (worldIn.getTileEntity(pos) instanceof TileEntityBarrel)
        {
            TileEntityBarrel barrel = (TileEntityBarrel)worldIn.getTileEntity(pos);
            barrel.setDestroyedByCreativePlayer(player.capabilities.isCreativeMode);
        }
        super.onBlockHarvested(worldIn, pos, state, player);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced)
    {
        super.addInformation(stack, player, tooltip, advanced);
        NBTTagCompound stackTag = stack.getTagCompound();

        if(stackTag != null && stackTag.hasKey("BlockEntityTag", 10))
        {
            NBTTagCompound blockEntityTag = stackTag.getCompoundTag("BlockEntityTag");

            if(blockEntityTag.hasKey("Water"))
                if(blockEntityTag.getInteger("Water") >= 0)
                    tooltip.add(blockEntityTag.getInteger("Water") + " " + I18n.format("water_barrel.buckets") + " / " + this.maxBuckets);
        }
        if(KeyboardHelper.isShiftKeyDown())
        {
            tooltip.add(TextFormatting.AQUA + I18n.format("water_barrel.use"));
        }
        else
        {
            tooltip.add(I18n.format("tooltip.hold_shift"));
        }
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityBarrel(this.maxBuckets);
    }

    public static class ItemBlockBarrel extends ItemBlock
    {
        private final int maxBuckets;

        public ItemBlockBarrel(Block block, int maxBuckets)
        {
            super(block);
            setMaxStackSize(1);
            this.maxBuckets = maxBuckets;
        }

        @Override
        public ItemStack getDefaultInstance()
        {
            NBTTagCompound stackTag = new NBTTagCompound();
            NBTTagCompound blockEntityTag = new NBTTagCompound();
            blockEntityTag.setInteger("Water", this.maxBuckets < 0 ? -1 : 0);
            stackTag.setTag("BlockEntityTag", blockEntityTag);
            return MineriaUtils.make(new ItemStack(this), stack -> stack.setTagCompound(stackTag));
        }
    }
}
