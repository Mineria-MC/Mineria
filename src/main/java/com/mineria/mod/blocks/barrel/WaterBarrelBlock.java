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
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.*;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.List;

@SuppressWarnings("deprecation")
public class WaterBarrelBlock extends Block
{
    private final int maxBuckets;

    public WaterBarrelBlock(int maxBuckets)
    {
        super(AbstractBlock.Properties.create(Material.WOOD).sound(SoundType.WOOD).hardnessAndResistance(2.0F, 3.0F).harvestLevel(0).harvestTool(ToolType.AXE));
        this.maxBuckets = maxBuckets;
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

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new WaterBarrelTileEntity(this.maxBuckets);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        WaterBarrelTileEntity te = (WaterBarrelTileEntity)worldIn.getTileEntity(pos);
        Item heldItem = player.getHeldItem(handIn).getItem();

        if(heldItem == Items.WATER_BUCKET)
        {
            if(te.increaseWaterBuckets())
            {
                worldIn.playSound((double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F, false);

                if(!worldIn.isRemote)
                {
                    if(!player.abilities.isCreativeMode)
                    {
                        player.getHeldItemMainhand().shrink(1);
                        player.setHeldItem(handIn, new ItemStack(Items.BUCKET));
                    }
                }
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player)
    {
        if (worldIn.getTileEntity(pos) instanceof WaterBarrelTileEntity)
        {
            WaterBarrelTileEntity tileEntity = (WaterBarrelTileEntity)worldIn.getTileEntity(pos);
            tileEntity.setDestroyedByCreativePlayer(player.abilities.isCreativeMode);
        }

        super.onBlockHarvested(worldIn, pos, state, player);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        CompoundNBT compound = stack.getTag();

        if(compound != null && compound.contains("BlockEntityTag", 10))
        {
            CompoundNBT compound1 = compound.getCompound("BlockEntityTag");

            if(compound1.contains("Water"))
                if(compound1.getInt("Water") >= 0)
                    tooltip.add(new StringTextComponent(compound1.getInt("Water") + " " + I18n.format("tooltip.mineria.buckets") + " / " + this.maxBuckets).mergeStyle(TextFormatting.GRAY));
        }

        if(KeyboardHelper.isShiftKeyDown())
        {
            tooltip.add(new TranslationTextComponent("tooltip.mineria.water_barrel.use").setStyle(Style.EMPTY.setColor(Color.fromInt(ColorHelper.PackedColor.packColor(255, 31, 255, 244)))));
        }

        else
        {
            tooltip.add(new TranslationTextComponent("tooltip.mineria.water_barrel.hold_shift").mergeStyle(TextFormatting.GRAY));
        }
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        TileEntity te = worldIn.getTileEntity(pos);

        if(te instanceof WaterBarrelTileEntity)
        {
            WaterBarrelTileEntity tileEntity = (WaterBarrelTileEntity)te;

            if(tileEntity.shouldDrop())
            {
                ItemStack stack = new ItemStack(BlocksInit.getItemFromBlock(this));
                CompoundNBT compound = new CompoundNBT();
                CompoundNBT nbttagcompound1 = new CompoundNBT();
                compound.put("BlockEntityTag", ((WaterBarrelTileEntity) te).write(nbttagcompound1));
                stack.setTag(compound);

                spawnAsEntity(worldIn, pos, stack);
            }
        }

        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public boolean addDestroyEffects(BlockState state, World world, BlockPos pos, ParticleManager manager)
    {
        return false;
    }

    public static class WaterBarrelBlockItem extends BlockItem
    {
        private final int maxBuckets;

        public WaterBarrelBlockItem(WaterBarrelBlock blockIn, Properties builder)
        {
            super(blockIn, builder);
            this.maxBuckets = blockIn.maxBuckets;
        }

        @Override
        public ItemStack getDefaultInstance()
        {
            CompoundNBT compound = new CompoundNBT();
            CompoundNBT blockEntityTag = new CompoundNBT();
            blockEntityTag.putInt("Water", this.maxBuckets < 0 ? -1 : 0);
            compound.put("BlockEntityTag", blockEntityTag);
            return Util.make(new ItemStack(this), stack -> stack.setTag(compound));
        }
    }
}
