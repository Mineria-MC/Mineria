package com.mineria.mod.blocks.barrel;

import com.mineria.mod.blocks.MineriaBlock;
import com.mineria.mod.util.KeyboardHelper;
import com.mineria.mod.util.MineriaUtils;
import mcp.MethodsReturnNonnullByDefault;
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
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Random;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings("deprecation")
public abstract class AbstractBlockWaterBarrel extends MineriaBlock implements ITileEntityProvider
{
    protected final int capacity;

    public AbstractBlockWaterBarrel(int capacity, float hardness, float resistance, int harvestLevel)
    {
        super(Material.WOOD, new BlockProperties().hardnessAndResistance(hardness, resistance).creativeTab(null).sounds(SoundType.WOOD).harvestLevel(harvestLevel, "axe"));
        this.capacity = capacity;
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

            if(tileAtPos instanceof AbstractTileEntityWaterBarrel)
                executeAction(worldIn, pos, state, (AbstractTileEntityWaterBarrel) tileAtPos, player, hand, facing);
        }

        return true;
    }

    /**
     * Override this method instead of #onBlockActivated
     *
     * @param world the world of the block
     * @param pos the pos of the block
     * @param state the state of the block
     * @param tile the barrel tile entity
     * @param player the player that activated the block
     * @param hand the hand used to activate the block
     * @param facing the face of the block
     */
    protected void executeAction(World world, BlockPos pos, IBlockState state, AbstractTileEntityWaterBarrel tile, EntityPlayer player, EnumHand hand, EnumFacing facing)
    {
        Item heldItem = player.getHeldItem(hand).getItem();

        if(heldItem.equals(Items.WATER_BUCKET))
            addWaterBucket(world, pos, player, tile, hand);
        else if(heldItem.equals(Items.BUCKET))
            removeWaterBucket(world, pos, player, tile, hand);
        else
        {
            ITextComponent message = new TextComponentString(tile.getBuckets() == 0 ? "There is no Water stored." : (tile.getBuckets() > 1 ? "There are " + tile.getBuckets() + " Water Buckets." : "There is 1 Water Bucket stored"));
            message.setStyle(message.getStyle().setColor(TextFormatting.GREEN));
            player.sendStatusMessage(message, true);
        }
    }

    protected void addWaterBucket(World world, BlockPos pos, EntityPlayer player, AbstractTileEntityWaterBarrel tile, EnumHand hand)
    {
        if(tile.increaseFluidBuckets())
        {
            world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
            if(!player.capabilities.isCreativeMode)
            {
                player.getHeldItem(hand).shrink(1);
                player.setHeldItem(hand, new ItemStack(Items.BUCKET));
            }
        }
    }

    protected void removeWaterBucket(World world, BlockPos pos, EntityPlayer player, AbstractTileEntityWaterBarrel tile, EnumHand hand)
    {
        if(tile.decreaseFluidBuckets())
        {
            world.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);

            if(!player.capabilities.isCreativeMode)
            {
                player.getHeldItem(hand).shrink(1);
                ItemStack stackToAdd = new ItemStack(Items.WATER_BUCKET);
                if(!player.addItemStackToInventory(stackToAdd))
                    player.dropItem(stackToAdd, false);
            }
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntity tile = worldIn.getTileEntity(pos);

        if(tile instanceof AbstractTileEntityWaterBarrel)
        {
            AbstractTileEntityWaterBarrel barrel = (AbstractTileEntityWaterBarrel)tile;

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
        if (worldIn.getTileEntity(pos) instanceof AbstractTileEntityWaterBarrel)
        {
            AbstractTileEntityWaterBarrel barrel = (AbstractTileEntityWaterBarrel)worldIn.getTileEntity(pos);
            barrel.setDestroyedByCreativePlayer(player.capabilities.isCreativeMode);
        }
        super.onBlockHarvested(worldIn, pos, state, player);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced)
    {
        super.addInformation(stack, world, tooltip, advanced);
        NBTTagCompound stackTag = stack.getTagCompound();

        if(stackTag != null && stackTag.hasKey("BlockEntityTag", 10))
        {
            NBTTagCompound blockEntityTag = stackTag.getCompoundTag("BlockEntityTag");

            if(blockEntityTag.hasKey("Buckets"))
                if(blockEntityTag.getInteger("Buckets") >= 0)
                    tooltip.add(blockEntityTag.getInteger("Buckets") + " " + I18n.format("tooltip.water_barrel.buckets") + " / " + this.capacity);
        }
        if(KeyboardHelper.isShiftKeyDown())
        {
            tooltip.add(TextFormatting.AQUA + I18n.format("tooltip.water_barrel.use"));
            addInformationOnShift(stack, tooltip, advanced);
        }
        else
            tooltip.add(TextFormatting.GRAY + I18n.format("tooltip.hold_shift"));
    }

    protected abstract void addInformationOnShift(ItemStack stack, List<String> tooltip, ITooltipFlag advanced);

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    public static class ItemBlockBarrel<T extends AbstractBlockWaterBarrel> extends ItemBlock
    {
        protected final T barrel;

        public ItemBlockBarrel(T barrel)
        {
            super(barrel);
            setMaxStackSize(1);
            this.barrel = barrel;
        }

        @Override
        public ItemStack getDefaultInstance()
        {
            NBTTagCompound stackTag = new NBTTagCompound();
            NBTTagCompound blockEntityTag = new NBTTagCompound();
            blockEntityTag.setInteger("Buckets", this.barrel.capacity < 0 ? -1 : 0);
            blockEntityTag.setInteger("Capacity", this.barrel.capacity);
            stackTag.setTag("BlockEntityTag", writeAdditional(blockEntityTag));
            return MineriaUtils.make(new ItemStack(this), stack -> stack.setTagCompound(stackTag));
        }

        protected NBTTagCompound writeAdditional(NBTTagCompound blockEntityTag)
        {
            return blockEntityTag;
        }
    }
}
