package com.mineria.mod.common.blocks.barrel;

import com.mineria.mod.common.init.MineriaBlocks;
import com.mineria.mod.common.init.MineriaCriteriaTriggers;
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
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.*;
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
        super(AbstractBlock.Properties.of(Material.WOOD).sound(SoundType.WOOD).strength(hardness, resistance).harvestLevel(harvestLevel).harvestTool(ToolType.AXE));
        this.initialCapacity = initialCapacity;
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state)
    {
        return PushReaction.BLOCK;
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
    {
        if(!worldIn.isClientSide)
        {
            TileEntity tileAtPos = worldIn.getBlockEntity(pos);

            if(tileAtPos instanceof AbstractWaterBarrelTileEntity)
                interact(worldIn, pos, state, (AbstractWaterBarrelTileEntity) tileAtPos, player, hand, hit);
        }

        return ActionResultType.SUCCESS;
    }

    protected void interact(World world, BlockPos pos, BlockState state, AbstractWaterBarrelTileEntity tile, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
    {
        Item heldItem = player.getItemInHand(hand).getItem();

        if(heldItem.equals(Items.WATER_BUCKET))
            addWaterBucket(this, world, pos, player, tile, hand);
        else if(heldItem.equals(Items.BUCKET))
            removeWaterBucket(this, world, pos, player, tile, hand);
        else
        {
            ITextComponent message = new StringTextComponent(tile.getBuckets() == 0 ? "There is no Water stored." : (tile.getBuckets() > 1 ? "There are " + tile.getBuckets() + " Water Buckets." : "There is 1 Water Bucket stored.")).withStyle(TextFormatting.GREEN);
            player.displayClientMessage(message, true);
        }
    }

    protected static void addWaterBucket(AbstractWaterBarrelBlock block, World world, BlockPos pos, PlayerEntity player, AbstractWaterBarrelTileEntity tile, Hand hand)
    {
        if(tile.addFluid())
        {
            world.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
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

    protected static void removeWaterBucket(AbstractWaterBarrelBlock block, World world, BlockPos pos, PlayerEntity player, AbstractWaterBarrelTileEntity tile, Hand hand)
    {
        if(tile.removeFluid())
        {
            world.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);

            if(!player.isCreative())
            {
                player.getItemInHand(hand).shrink(1);
                ItemStack stackToAdd = new ItemStack(Items.WATER_BUCKET);
                if(!player.addItem(stackToAdd))
                    player.drop(stackToAdd, false);
            }
        }
    }

    @Override
    public void playerWillDestroy(World worldIn, BlockPos pos, BlockState state, PlayerEntity player)
    {
        TileEntity tile = worldIn.getBlockEntity(pos);

        if (tile instanceof AbstractWaterBarrelTileEntity)
        {
            AbstractWaterBarrelTileEntity barrel = (AbstractWaterBarrelTileEntity)tile;
            barrel.setDestroyedByCreativePlayer(player.isCreative());

            if(barrel.shouldDrop())
            {
                ItemStack stack = new ItemStack(this);
                CompoundNBT compound = new CompoundNBT();
                CompoundNBT blockEntityTag = new CompoundNBT();
                compound.put("BlockEntityTag", barrel.save(blockEntityTag));
                stack.setTag(compound);

                popResource(worldIn, pos, stack);
            }
        }

        super.playerWillDestroy(worldIn, pos, state, player);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        CompoundNBT stackTag = stack.getTag();

        if(stackTag != null && stackTag.contains("BlockEntityTag", 10))
        {
            CompoundNBT blockEntityTag = stackTag.getCompound("BlockEntityTag");

            if(blockEntityTag.contains("Buckets") && blockEntityTag.contains("Capacity"))
                if(blockEntityTag.getInt("Buckets") >= 0)
                    tooltip.add(new StringTextComponent(blockEntityTag.getInt("Buckets") + " " + I18n.get("tooltip.mineria.buckets") + " / " + this.initialCapacity).withStyle(TextFormatting.GRAY));
        }

        if(KeyboardHelper.isShiftKeyDown())
        {
            tooltip.add(new TranslationTextComponent("tooltip.mineria.water_barrel.use").setStyle(Style.EMPTY.withColor(Color.fromRgb(ColorHelper.PackedColor.color(255, 31, 255, 244)))));
            addInformationOnShift(stack, worldIn, tooltip, flagIn);
        }
        else
            tooltip.add(new TranslationTextComponent("tooltip.mineria.water_barrel.hold_shift").withStyle(TextFormatting.GRAY));
    }

    protected abstract void addInformationOnShift(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn);

    @Override
    public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public boolean addDestroyEffects(BlockState state, World world, BlockPos pos, ParticleManager manager)
    {
        return false;
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player)
    {
        return MineriaBlocks.getItemFromBlock(this).getDefaultInstance();
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
        public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> stacks)
        {
            if(this.allowdedIn(group))
                stacks.add(getDefaultInstance());
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
