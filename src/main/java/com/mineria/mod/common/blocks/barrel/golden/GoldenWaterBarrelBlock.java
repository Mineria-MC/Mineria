package com.mineria.mod.common.blocks.barrel.golden;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.blocks.barrel.AbstractWaterBarrelBlock;
import com.mineria.mod.common.blocks.barrel.AbstractWaterBarrelTileEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;

public class GoldenWaterBarrelBlock extends AbstractWaterBarrelBlock
{
    public static final IntegerProperty POTIONS = IntegerProperty.create("potions", 0, 4);

    public GoldenWaterBarrelBlock()
    {
        super(4.5F, 12, 32);
        registerDefaultState(this.stateDefinition.any().setValue(POTIONS, 0));
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
    {
        BlockEntity tile = worldIn.getBlockEntity(pos);
        if(tile instanceof GoldenWaterBarrelTileEntity)
            ((GoldenWaterBarrelTileEntity) tile).reloadBlockState();
        super.setPlacedBy(worldIn, pos, state, placer, stack);
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
                NetworkHooks.openGui((ServerPlayer) player, (GoldenWaterBarrelTileEntity) tile, pos);
        }
    }

    @Override
    protected void addInformationOnShift(ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn)
    {
        tooltip.add(new TranslatableComponent("tooltip.mineria.water_barrel.ability").withStyle(ChatFormatting.GOLD, ChatFormatting.ITALIC).append(" : ").append(new TranslatableComponent("tooltip.mineria.water_barrel.store_potions")));
        tooltip.add(new TranslatableComponent("tooltip.mineria.water_barrel.view_capacity").withStyle(ChatFormatting.GRAY));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(POTIONS);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState)
    {
        return new GoldenWaterBarrelTileEntity(pPos, pState);
    }

    public static class BarrelBlockItem extends AbstractWaterBarrelBlock.WaterBarrelBlockItem<GoldenWaterBarrelBlock>
    {
        public BarrelBlockItem(GoldenWaterBarrelBlock barrel)
        {
            super(barrel, new Properties().tab(Mineria.MINERIA_GROUP).stacksTo(1));
        }

        @Override
        public CompoundTag writeAdditional(CompoundTag blockEntityTag)
        {
            blockEntityTag.putInt("Potions", 0);
            return blockEntityTag;
        }

        @Override
        public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn)
        {
            ItemStack stack = playerIn.getItemInHand(handIn);

            if(stack.getItem() instanceof GoldenWaterBarrelBlock.BarrelBlockItem)
            {
                if(stack.getTag() != null)
                {
                    if(stack.getTag().contains("BlockEntityTag"))
                    {
                        CompoundTag blockEntityTag = stack.getTag().getCompound("BlockEntityTag");
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
