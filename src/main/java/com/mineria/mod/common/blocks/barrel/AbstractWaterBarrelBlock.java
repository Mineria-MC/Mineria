package com.mineria.mod.common.blocks.barrel;

import com.mineria.mod.common.init.MineriaBlocks;
import com.mineria.mod.common.init.MineriaCriteriaTriggers;
import com.mineria.mod.util.KeyboardHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.FastColor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nullable;
import java.util.List;

@SuppressWarnings("deprecation")
public abstract class AbstractWaterBarrelBlock extends Block implements EntityBlock
{
    protected final int initialCapacity;

    public AbstractWaterBarrelBlock(float hardness, float resistance, int initialCapacity)
    {
        super(BlockBehaviour.Properties.of(Material.WOOD).sound(SoundType.WOOD).strength(hardness, resistance));
        this.initialCapacity = initialCapacity;
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state)
    {
        return PushReaction.BLOCK;
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        if(!worldIn.isClientSide)
        {
            BlockEntity tileAtPos = worldIn.getBlockEntity(pos);

            if(tileAtPos instanceof AbstractWaterBarrelTileEntity)
                interact(worldIn, pos, state, (AbstractWaterBarrelTileEntity) tileAtPos, player, hand, hit);
        }

        return InteractionResult.SUCCESS;
    }

    protected void interact(Level world, BlockPos pos, BlockState state, AbstractWaterBarrelTileEntity tile, Player player, InteractionHand hand, BlockHitResult hit)
    {
        Item heldItem = player.getItemInHand(hand).getItem();

        if(heldItem.equals(Items.WATER_BUCKET))
            addWaterBucket(this, world, pos, player, tile, hand);
        else if(heldItem.equals(Items.BUCKET))
            removeWaterBucket(this, world, pos, player, tile, hand);
        else
        {
            Component message = new TextComponent(tile.getBuckets() == 0 ? "There is no Water stored." : (tile.getBuckets() > 1 ? "There are " + tile.getBuckets() + " Water Buckets." : "There is 1 Water Bucket stored.")).withStyle(ChatFormatting.GREEN);
            player.displayClientMessage(message, true);
        }
    }

    protected static void addWaterBucket(AbstractWaterBarrelBlock block, Level world, BlockPos pos, Player player, AbstractWaterBarrelTileEntity tile, InteractionHand hand)
    {
        if(tile.addFluid())
        {
            world.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
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

    protected static void removeWaterBucket(AbstractWaterBarrelBlock block, Level world, BlockPos pos, Player player, AbstractWaterBarrelTileEntity tile, InteractionHand hand)
    {
        if(tile.removeFluid())
        {
            world.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);

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
    public void playerWillDestroy(Level worldIn, BlockPos pos, BlockState state, Player player)
    {
        BlockEntity tile = worldIn.getBlockEntity(pos);

        if (tile instanceof AbstractWaterBarrelTileEntity)
        {
            AbstractWaterBarrelTileEntity barrel = (AbstractWaterBarrelTileEntity)tile;
            barrel.setDestroyedByCreativePlayer(player.isCreative());

            if(barrel.shouldDrop())
            {
                ItemStack stack = new ItemStack(MineriaBlocks.getItemFromBlock(this));
                CompoundTag compound = new CompoundTag();
                CompoundTag blockEntityTag = new CompoundTag();
                compound.put("BlockEntityTag", barrel.save(blockEntityTag));
                stack.setTag(compound);

                popResource(worldIn, pos, stack);
            }
        }

        super.playerWillDestroy(worldIn, pos, state, player);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn)
    {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        CompoundTag stackTag = stack.getTag();

        if(stackTag != null && stackTag.contains("BlockEntityTag", 10))
        {
            CompoundTag blockEntityTag = stackTag.getCompound("BlockEntityTag");

            if(blockEntityTag.contains("Buckets") && blockEntityTag.contains("Capacity"))
                if(blockEntityTag.getInt("Buckets") >= 0)
                    tooltip.add(new TextComponent(blockEntityTag.getInt("Buckets") + " " + I18n.get("tooltip.mineria.buckets") + " / " + this.initialCapacity).withStyle(ChatFormatting.GRAY));
        }

        if(KeyboardHelper.isShiftKeyDown())
        {
            tooltip.add(new TranslatableComponent("tooltip.mineria.water_barrel.use").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(FastColor.ARGB32.color(255, 31, 255, 244)))));
            addInformationOnShift(stack, worldIn, tooltip, flagIn);
        }
        else
            tooltip.add(new TranslatableComponent("tooltip.mineria.water_barrel.hold_shift").withStyle(ChatFormatting.GRAY));
    }

    protected abstract void addInformationOnShift(ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn);

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public ItemStack getPickBlock(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player)
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
        public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> stacks)
        {
            if(this.allowdedIn(group))
                stacks.add(getDefaultInstance());
        }

        @Override
        public ItemStack getDefaultInstance()
        {
            CompoundTag compound = new CompoundTag();
            CompoundTag blockEntityTag = new CompoundTag();
            blockEntityTag.putInt("Buckets", this.barrel.initialCapacity < 0 ? -1 : 0);
            blockEntityTag.putInt("Capacity", this.barrel.initialCapacity);
            compound.put("BlockEntityTag", writeAdditional(blockEntityTag));
            return Util.make(new ItemStack(this), stack -> stack.setTag(compound));
        }

        public CompoundTag writeAdditional(CompoundTag blockEntityTag)
        {
            return blockEntityTag;
        }
    }
}
