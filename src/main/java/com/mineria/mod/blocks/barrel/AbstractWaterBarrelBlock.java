package com.mineria.mod.blocks.barrel;

import com.mineria.mod.init.BlocksInit;
import com.mineria.mod.util.KeyboardHelper;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.*;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.List;

@SuppressWarnings("deprecation")
public abstract class AbstractWaterBarrelBlock extends Block
{
    protected final int initialCapacity;

    public AbstractWaterBarrelBlock(float hardness, float resistance, int harvestLevel, int initialCapacity)
    {
        super(AbstractBlock.Properties.create(Material.WOOD).sound(SoundType.WOOD).hardnessAndResistance(hardness, resistance).harvestLevel(harvestLevel).harvestTool(ToolType.AXE));
        this.initialCapacity = initialCapacity;
    }

    @Override
    public PushReaction getPushReaction(BlockState state)
    {
        return PushReaction.BLOCK;
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
    {
        if(!worldIn.isRemote)
        {
            TileEntity tileAtPos = worldIn.getTileEntity(pos);

            if(tileAtPos instanceof AbstractWaterBarrelTileEntity)
                interact(worldIn, pos, state, (AbstractWaterBarrelTileEntity) tileAtPos, player, hand, hit);
        }

        return ActionResultType.SUCCESS;
    }

    protected void interact(World world, BlockPos pos, BlockState state, AbstractWaterBarrelTileEntity tile, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
    {
        Item heldItem = player.getHeldItem(hand).getItem();

        if(heldItem.equals(Items.WATER_BUCKET))
            addWaterBucket(world, pos, player, tile, hand);
        else if(heldItem.equals(Items.BUCKET))
            removeWaterBucket(world, pos, player, tile, hand);
        else
        {
            ITextComponent message = new StringTextComponent(tile.getBuckets() == 0 ? "There is no Water stored." : (tile.getBuckets() > 1 ? "There are " + tile.getBuckets() + " Water Buckets." : "There is 1 Water Bucket stored.")).mergeStyle(TextFormatting.GREEN);
            player.sendStatusMessage(message, true);
        }
    }

    protected static void addWaterBucket(World world, BlockPos pos, PlayerEntity player, AbstractWaterBarrelTileEntity tile, Hand hand)
    {
        if(tile.addFluid())
        {
            world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
            if(!player.isCreative())
            {
                player.getHeldItem(hand).shrink(1);
                player.setHeldItem(hand, new ItemStack(Items.BUCKET));
            }
        }
    }

    protected static void removeWaterBucket(World world, BlockPos pos, PlayerEntity player, AbstractWaterBarrelTileEntity tile, Hand hand)
    {
        if(tile.removeFluid())
        {
            world.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);

            if(!player.isCreative())
            {
                player.getHeldItem(hand).shrink(1);
                ItemStack stackToAdd = new ItemStack(Items.WATER_BUCKET);
                if(!player.addItemStackToInventory(stackToAdd))
                    player.dropItem(stackToAdd, false);
            }
        }
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player)
    {
        TileEntity tile = worldIn.getTileEntity(pos);

        if (tile instanceof AbstractWaterBarrelTileEntity)
        {
            AbstractWaterBarrelTileEntity barrel = (AbstractWaterBarrelTileEntity)tile;
            barrel.setDestroyedByCreativePlayer(player.isCreative());

            if(barrel.shouldDrop())
            {
                ItemStack stack = new ItemStack(BlocksInit.getItemFromBlock(this));
                CompoundNBT compound = new CompoundNBT();
                CompoundNBT blockEntityTag = new CompoundNBT();
                compound.put("BlockEntityTag", barrel.write(blockEntityTag));
                stack.setTag(compound);

                spawnAsEntity(worldIn, pos, stack);
            }
        }

        super.onBlockHarvested(worldIn, pos, state, player);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        CompoundNBT stackTag = stack.getTag();

        if(stackTag != null && stackTag.contains("BlockEntityTag", 10))
        {
            CompoundNBT blockEntityTag = stackTag.getCompound("BlockEntityTag");

            if(blockEntityTag.contains("Buckets") && blockEntityTag.contains("Capacity"))
                if(blockEntityTag.getInt("Buckets") >= 0)
                    tooltip.add(new StringTextComponent(blockEntityTag.getInt("Buckets") + " " + I18n.format("tooltip.mineria.buckets") + " / " + this.initialCapacity).mergeStyle(TextFormatting.GRAY));
        }

        if(KeyboardHelper.isShiftKeyDown())
        {
            tooltip.add(new TranslationTextComponent("tooltip.mineria.water_barrel.use").setStyle(Style.EMPTY.setColor(Color.fromInt(ColorHelper.PackedColor.packColor(255, 31, 255, 244)))));
            addInformationOnShift(stack, worldIn, tooltip, flagIn);
        }
        else
            tooltip.add(new TranslationTextComponent("tooltip.mineria.water_barrel.hold_shift").mergeStyle(TextFormatting.GRAY));
    }

    protected abstract void addInformationOnShift(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn);

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public boolean addDestroyEffects(BlockState state, World world, BlockPos pos, ParticleManager manager)
    {
        return false;
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player)
    {
        return BlocksInit.getItemFromBlock(this).getDefaultInstance();
    }

    public static class WaterBarrelBlockItem<T extends AbstractWaterBarrelBlock> extends BlockItem
    {
        protected final T barrel;

        public WaterBarrelBlockItem(T barrel, Properties builder)
        {
            super(barrel, builder);
            this.barrel = barrel;
        }

        @Override
        public ItemStack getDefaultInstance()
        {
            CompoundNBT compound = new CompoundNBT();
            CompoundNBT blockEntityTag = new CompoundNBT();
            blockEntityTag.putInt("Buckets", this.barrel.initialCapacity < 0 ? -1 : 0);
            blockEntityTag.putInt("Capacity", this.barrel.initialCapacity);
            compound.put("BlockEntityTag", writeAdditional(blockEntityTag));
            return Util.make(new ItemStack(this), stack -> stack.setTag(compound));
        }

        public CompoundNBT writeAdditional(CompoundNBT blockEntityTag)
        {
            return blockEntityTag;
        }
    }
}
