package com.mineria.mod.blocks.barrel;

@SuppressWarnings("deprecation")
public class BlockBarrel// extends BlockContainer
{
    /*
    private final int maxBuckets;

    public BlockBarrel(String name, int maxBuckets)
    {
        super(Material.WOOD);
        setRegistryName(name);
        setUnlocalizedName(name);
        setCreativeTab(Mineria.MINERIA_GROUP);
        setSoundType(SoundType.WOOD);
        setHardness(2.0F);
        setResistance(10.0F);
        this.maxBuckets = maxBuckets;
    }

    @Override
    public boolean isFullBlock(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
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
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        TileEntityBarrel te = (TileEntityBarrel)worldIn.getTileEntity(pos);
        Item heldItem = playerIn.getHeldItemMainhand().getItem();

        if(heldItem == Items.WATER_BUCKET)
        {
            if(te.setWatetBucket())
            {
                worldIn.playSound((double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
                if(!worldIn.isRemote)
                {
                    if(!playerIn.capabilities.isCreativeMode)
                    {
                        playerIn.getHeldItemMainhand().shrink(1);
                        playerIn.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Items.BUCKET));
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntity te = worldIn.getTileEntity(pos);

        if(te instanceof TileEntityBarrel)
        {
            TileEntityBarrel tileEntity = (TileEntityBarrel)te;

            if(tileEntity.shouldDrop())
            {
                ItemStack stack = new ItemStack(Item.getItemFromBlock(this));
                NBTTagCompound compound = new NBTTagCompound();
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                compound.setTag("BlockEntityTag", ((TileEntityBarrel) te).writeToNBT(nbttagcompound1));
                stack.setTagCompound(compound);

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
            TileEntityBarrel tileEntity = (TileEntityBarrel)worldIn.getTileEntity(pos);
            tileEntity.setDestroyedByCreativePlayer(player.capabilities.isCreativeMode);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced)
    {
        super.addInformation(stack, player, tooltip, advanced);
        NBTTagCompound nbttagcompound = stack.getTagCompound();

        if(nbttagcompound != null && nbttagcompound.hasKey("BlockEntityTag", 10))
        {
            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("BlockEntityTag");

            if(nbttagcompound1.hasKey("Water"))
            {
                tooltip.add(nbttagcompound1.getInteger("Water") + " Buckets / " + this.maxBuckets);
            }
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
    
     */
}
