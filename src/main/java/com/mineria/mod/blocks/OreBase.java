package com.mineria.mod.blocks;

import java.util.Random;

import com.mineria.mod.Mineria;
import com.mineria.mod.init.BlocksInit;
import com.mineria.mod.init.ItemsInit;
import net.minecraft.block.BlockOre;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class OreBase extends BlockOre
{
	private static boolean multipleQuantity = false;
	private static int minDrop;
	private static int maxDrop;
	
	public OreBase(String name, int harvestlevel, Material materialIn, float hardness, float resistance, SoundType sound)
	{
		super();
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(Mineria.mineriaTab);
		setHarvestLevel("pickaxe", harvestlevel);
		this.setHardness(hardness);
		this.setResistance(resistance);
		this.setSoundType(sound);
	}
	
	public OreBase(String name, int harvestlevel, Material material, float hardness, float resistance, SoundType sound, int minDrop, int maxDrop)
	{
		super();
		setUnlocalizedName(name);
		setRegistryName(name);
		setHarvestLevel("pickaxe", harvestlevel);
		setCreativeTab(Mineria.mineriaTab);
		this.setHardness(hardness);
		this.setResistance(resistance);
		this.setSoundType(sound);
		this.multipleQuantity = true;
		this.minDrop = minDrop;
		this.maxDrop = maxDrop;
	}
	
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		if(this == BlocksInit.lonsdaleite_ore)
		{
			return ItemsInit.lonsdaleite;
		}
		else
		{
			return Item.getItemFromBlock(this);
		}
	}
	
	public int quantityDropped(Random rand)
	{
		return this.multipleQuantity ? this.minDrop + rand.nextInt(this.maxDrop - this.minDrop) : 1;
	}
	
	@Override
	public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune)
	{
		Random rand = world instanceof World ? ((World)world).rand : new Random();
		
		if(this.getItemDropped(state, rand, fortune) != Item.getItemFromBlock(this))
		{
			if(this == BlocksInit.lonsdaleite_ore)
			{
				return MathHelper.getInt(rand, 4, 10);
			}
		}
		
		return 0;
	}
	
	@Override
	public int quantityDroppedWithBonus(int fortune, Random random)
    {
        if (fortune > 0 && Item.getItemFromBlock(this) != this.getItemDropped((IBlockState)this.getBlockState().getValidStates().iterator().next(), random, fortune))
        {
            int i = random.nextInt(fortune + 1) - 1;

            if (i < 0)
            {
                i = 0;
            }

            return this.quantityDropped(random) * (i + 1);
        }
        else
        {
            return this.quantityDropped(random);
        }
    }
}
