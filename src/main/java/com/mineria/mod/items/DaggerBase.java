package com.mineria.mod.items;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mineria.mod.Mineria;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSword;

@Deprecated
public class DaggerBase extends ItemSword
{
	private final float attackDamage;
	private final float attackSpeed;
	private final Item.ToolMaterial material;

	public DaggerBase(String name, float attackDamage, float attackSpeed, ToolMaterial material)
	{
		super(material);
		this.attackDamage = attackDamage;
		this.attackSpeed = attackSpeed;
		this.material = material;
		setCreativeTab(Mineria.mineriaTab);
		setUnlocalizedName(name);
		setRegistryName(name);
	}

	@Override
	public float getAttackDamage()
	{
		return this.attackDamage;
	}

	public float getAttackSpeed()
	{
		return this.attackSpeed;
	}

	/*
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
	{
		stack.damageItem(1, attacker);
		return true;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving)
	{
		if ((double)state.getBlockHardness(worldIn, pos) != 0.0D)
		{
			stack.damageItem(2, entityLiving);
		}
		return true;
	}

	@Override
	public boolean canHarvestBlock(IBlockState blockIn)
    {
        return blockIn.getBlock() == Blocks.WEB;
    }

    @SideOnly(Side.CLIENT)
	@Override
	public boolean isFull3D()
	{
		return true;
	}

	@Override
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

	@Override
	public int getItemEnchantability()
	{
		return material.getEnchantability();
	}

	public String getToolMaterialName()
	{
		return this.material.toString();
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
	{
		ItemStack mat = this.material.getRepairItemStack();
		if (!mat.isEmpty() && net.minecraftforge.oredict.OreDictionary.itemMatches(mat, repair, false)) return true;
		return super.getIsRepairable(toRepair, repair);
	}*/

	@Override
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot)
	{
		Multimap<String, AttributeModifier> multimap = HashMultimap.<String, AttributeModifier>create();

		if (equipmentSlot == EntityEquipmentSlot.MAINHAND)
		{
			multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double)this.attackDamage, 0));
			multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", (double)this.attackSpeed, 0));
		}

		return multimap;
	}
}
