package com.mineria.mod.common.blocks.barrel.golden;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.blocks.barrel.AbstractWaterBarrelBlock;
import com.mineria.mod.common.blocks.barrel.AbstractWaterBarrelTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
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

public class GoldenWaterBarrelBlock extends AbstractWaterBarrelBlock
{
    public static final IntegerProperty POTIONS = IntegerProperty.create("potions", 0, 4);

    public GoldenWaterBarrelBlock()
    {
        super(4.5F, 12, 2, 32);
        registerDefaultState(this.stateDefinition.any().setValue(POTIONS, 0));
    }

    @Override
    public void setPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
    {
        TileEntity tile = worldIn.getBlockEntity(pos);
        if(tile instanceof GoldenWaterBarrelTileEntity)
            ((GoldenWaterBarrelTileEntity) tile).reloadBlockState();
        super.setPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Override
    protected void interact(World world, BlockPos pos, BlockState state, AbstractWaterBarrelTileEntity tile, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
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
                ITextComponent message = new StringTextComponent(tile.getBuckets() == 0 ? "There is no Water stored." : (tile.getBuckets() > 1 ? "There are " + tile.getBuckets() + " Water Buckets." : "There is 1 Water Bucket stored.")).withStyle(TextFormatting.GREEN);
                player.displayClientMessage(message, true);
            }
            else
                NetworkHooks.openGui((ServerPlayerEntity) player, (GoldenWaterBarrelTileEntity) tile, pos);
        }
    }

    @Override
    protected void addInformationOnShift(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(new TranslationTextComponent("tooltip.mineria.water_barrel.ability").withStyle(TextFormatting.GOLD, TextFormatting.ITALIC).append(" : ").append(new TranslationTextComponent("tooltip.mineria.water_barrel.store_potions")));
        tooltip.add(new TranslationTextComponent("tooltip.mineria.water_barrel.view_capacity").withStyle(TextFormatting.GRAY));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(POTIONS);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new GoldenWaterBarrelTileEntity();
    }

    public static class BarrelBlockItem extends AbstractWaterBarrelBlock.WaterBarrelBlockItem<GoldenWaterBarrelBlock>
    {
        public BarrelBlockItem(GoldenWaterBarrelBlock barrel)
        {
            super(barrel, new Properties().tab(Mineria.MINERIA_GROUP).stacksTo(1));
        }

        @Override
        public CompoundNBT writeAdditional(CompoundNBT blockEntityTag)
        {
            blockEntityTag.putInt("Potions", 0);
            return blockEntityTag;
        }

        @Override
        public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn)
        {
            ItemStack stack = playerIn.getItemInHand(handIn);

            if(stack.getItem() instanceof GoldenWaterBarrelBlock.BarrelBlockItem)
            {
                if(stack.getTag() != null)
                {
                    if(stack.getTag().contains("BlockEntityTag"))
                    {
                        CompoundNBT blockEntityTag = stack.getTag().getCompound("BlockEntityTag");
                        if(blockEntityTag.contains("Potions"))
                            Mineria.LOGGER.debug("Potions : " + blockEntityTag.getInt("Potions"));
                        else
                            Mineria.LOGGER.debug("Null potions tag : " + blockEntityTag.contains("Potions"));
                    }
                    else
                        Mineria.LOGGER.debug("Null blockEntityTag : " + stack.getTag().contains("BlockEntityTag"));
                }
            }

            return super.use(worldIn, playerIn, handIn);
        }
    }
}
