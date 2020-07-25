package com.mineria.mod.items;

import java.util.Set;

import com.mineria.mod.Mineria;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;

@Deprecated
public class CustomSwordBase extends ItemTool
{
	public CustomSwordBase(String name, ToolMaterial materialIn, float attackDamageIn, float attackSpeedIn, int maxdamage, Set<Block> effectiveBlocksIn)
	{
		super(materialIn, effectiveBlocksIn);
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(Mineria.mineriaTab);
		this.attackDamage = attackDamageIn;
		this.attackSpeed = attackSpeedIn;
		setMaxDamage(maxdamage);
	}
	
	@Override
	public boolean canHarvestBlock(IBlockState blockIn)
    {
        return blockIn.getBlock() == Blocks.WEB;
    }
	
	public float getDestroySpeed(ItemStack stack, IBlockState state)
    {
        Block block = state.getBlock();

        if (block == Blocks.WEB)
        {
            return 15.0F;
        }
        else
        {
            Material material = state.getMaterial();
            return material != Material.PLANTS && material != Material.VINE && material != Material.CORAL && material != Material.LEAVES && material != Material.GOURD ? 1.0F : 1.5F;
        }
    }

}
