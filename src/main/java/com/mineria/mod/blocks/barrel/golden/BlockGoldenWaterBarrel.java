package com.mineria.mod.blocks.barrel.golden;

import com.mineria.mod.Mineria;
import com.mineria.mod.blocks.barrel.AbstractBlockWaterBarrel;
import com.mineria.mod.blocks.barrel.AbstractTileEntityWaterBarrel;
import com.mineria.mod.util.GuiHandler;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockGoldenWaterBarrel extends AbstractBlockWaterBarrel
{
    protected static final PropertyInteger POTIONS = PropertyInteger.create("potions", 0, 4);

    public BlockGoldenWaterBarrel()
    {
        super(32, 4.5F, 12, 2);
        setDefaultState(this.blockState.getBaseState().withProperty(POTIONS, 0));
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
                player.openGui(Mineria.INSTANCE, GuiHandler.GUI_GOLDEN_BARREL, world, pos.getX(), pos.getY(), pos.getZ());
        }
    }

    @Override
    protected void addInformationOnShift(ItemStack stack, List<String> tooltip, ITooltipFlag advanced)
    {
        tooltip.add(TextFormatting.GOLD.toString().concat(TextFormatting.ITALIC.toString()).concat(I18n.format("tooltip.water_barrel.ability")).concat(" : ").concat(I18n.format("tooltip.water_barrel.store_potions")));
        tooltip.add(I18n.format("tooltip.water_barrel.view_capacity"));
    }

    public static void setState(int potions, World worldIn, BlockPos pos)
    {
        IBlockState state = worldIn.getBlockState(pos);
        TileEntity tile = worldIn.getTileEntity(pos);

        worldIn.setBlockState(pos, state.withProperty(POTIONS, potions), 3);

        if (tile != null)
        {
            tile.validate();
            worldIn.setTileEntity(pos, tile);
        }
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState();
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityGoldenWaterBarrel();
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, POTIONS);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        TileEntity tile = worldIn.getTileEntity(pos);

        if(tile instanceof TileEntityGoldenWaterBarrel)
            ((TileEntityGoldenWaterBarrel)tile).reloadBlockState();
    }

    public static class ItemBlockBarrel extends AbstractBlockWaterBarrel.ItemBlockBarrel<BlockGoldenWaterBarrel>
    {
        public ItemBlockBarrel(BlockGoldenWaterBarrel barrel)
        {
            super(barrel);
            this.addPropertyOverride(new ResourceLocation("potions"), (stack, world, entity) -> {
                if(stack.getTagCompound() != null)
                {
                    if(stack.getTagCompound().hasKey("BlockEntityTag"))
                    {
                        NBTTagCompound blockEntityTag = stack.getTagCompound().getCompoundTag("BlockEntityTag");
                        if(blockEntityTag.hasKey("Potions"))
                            return blockEntityTag.getInteger("Potions");
                    }
                }
                return 0;
            });
        }

        @Override
        protected NBTTagCompound writeAdditional(NBTTagCompound blockEntityTag)
        {
            blockEntityTag.setInteger("Potions", 0);
            return blockEntityTag;
        }
    }
}
