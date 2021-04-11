package com.mineria.mod.blocks;

import com.mineria.mod.Mineria;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nullable;
import java.util.Random;

public class MineriaBlock extends Block
{
	private final boolean beaconBase;
	private final boolean multipleDrop;
	private final int minDrop;
	private final int maxDrop;
	private final boolean multipleXp;
	private final int minXp;
	private final int maxXp;
	@Nullable
	private final Item itemDropped;

	public MineriaBlock(Material materialIn, float hardness, float resistance, int harvestLevel, SoundType sound)
	{
		this(materialIn, new BlockProperties().hardnessAndResistance(hardness, resistance).harvestLevel(harvestLevel, "pickaxe").sounds(sound));
	}

	public MineriaBlock(Material materialIn, float hardness, float resistance, int harvestLevel, String harvestTool, SoundType sound)
	{
		this(materialIn, new BlockProperties().hardnessAndResistance(hardness, resistance).harvestLevel(harvestLevel, harvestTool).sounds(sound));
	}

	protected MineriaBlock(Material material, BlockProperties properties)
	{
		super(material);
		if(properties.tab != null) setCreativeTab(properties.tab);
		setHardness(properties.hardness);
		setResistance(properties.resistance);
		setHarvestLevel(properties.harvestTool, properties.harvestLevel);
		setSoundType(properties.sound);
		this.beaconBase = properties.beaconBase;
		this.multipleDrop = properties.multipleDrop;
		this.minDrop = properties.minDrop;
		this.maxDrop = properties.maxDrop;
		this.multipleXp = properties.multipleXp;
		this.minXp = properties.minXp;
		this.maxXp = properties.maxXp;
		this.itemDropped = properties.itemDropped;
	}

	@Override
	public boolean isBeaconBase(IBlockAccess world, BlockPos pos, BlockPos beacon)
	{
		return beaconBase;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return itemDropped == null ? super.getItemDropped(state, rand, fortune) : itemDropped;
	}

	@Override
	public int quantityDropped(Random rand)
	{
		return this.multipleDrop ? this.minDrop + rand.nextInt(this.maxDrop - this.minDrop) : 1;
	}

	@Override
	public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune)
	{
		return multipleXp ? MathHelper.getInt(new Random(), minXp, maxXp) : 0;
	}

	@Override
	public int quantityDroppedWithBonus(int fortune, Random random)
	{
		if (fortune > 0 && Item.getItemFromBlock(this) != this.getItemDropped(this.getBlockState().getValidStates().iterator().next(), random, fortune))
		{
			int chance = random.nextInt(fortune + 1) - 1;

			if (chance < 0)
				chance = 0;

			return this.quantityDropped(random) * (chance + 1);
		}
		else
			return this.quantityDropped(random);
	}

	public static class BlockProperties
	{
		@Nullable
		private CreativeTabs tab = Mineria.MINERIA_TAB;
		private float hardness;
		private float resistance;
		private int harvestLevel = -1;
		private String harvestTool = "";
		private SoundType sound = SoundType.STONE;
		private boolean beaconBase;
		private boolean multipleDrop;
		private int minDrop;
		private int maxDrop;
		private boolean multipleXp;
		private int minXp;
		private int maxXp;
		@Nullable
		private Item itemDropped;

		public BlockProperties creativeTab(@Nullable CreativeTabs tab)
		{
			this.tab = tab;
			return this;
		}

		public BlockProperties hardnessAndResistance(float hardness, float resistance)
		{
			this.hardness = hardness;
			this.resistance = resistance;
			return this;
		}

		public BlockProperties harvestLevel(int harvestLevel, String harvestTool)
		{
			this.harvestLevel = harvestLevel;
			this.harvestTool = harvestTool;
			return this;
		}

		public BlockProperties sounds(SoundType sound)
		{
			this.sound = sound;
			return this;
		}

		public BlockProperties beaconBase()
		{
			this.beaconBase = true;
			return this;
		}

		public BlockProperties multipleDrop(int minDrop, int maxDrop)
		{
			this.multipleDrop = true;
			this.minDrop = minDrop;
			this.maxDrop = maxDrop;
			return this;
		}

		public BlockProperties multipleXp(int minXp, int maxXp)
		{
			this.multipleXp = true;
			this.minXp = minXp;
			this.maxXp = maxXp;
			return this;
		}

		public BlockProperties customDrop(Item drop)
		{
			this.itemDropped = drop;
			return this;
		}

		public MineriaBlock build(Material material)
		{
			return new MineriaBlock(material, this);
		}
	}
}