package com.mineria.mod.items;

import com.mineria.mod.Mineria;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.apache.logging.log4j.util.BiConsumer;

public class ItemCustomFood extends ItemFood
{
	private final BiConsumer<ItemStack, EntityPlayer> onFoodEaten;
	private final int maxUseDuration;

	public ItemCustomFood(int amount, float saturation, boolean isWolfFood, boolean alwaysEdible, BiConsumer<ItemStack, EntityPlayer> onFoodEaten)
	{
		this(amount, saturation, isWolfFood, alwaysEdible, 32, onFoodEaten);
	}

	public ItemCustomFood(int amount, float saturation, boolean isWolfFood, boolean alwaysEdible, int maxUseDuration, BiConsumer<ItemStack, EntityPlayer> onFoodEaten)
	{
		super(amount, saturation, isWolfFood);
		if(alwaysEdible)
			setAlwaysEdible();
		setCreativeTab(Mineria.MINERIA_TAB);
		this.maxUseDuration = maxUseDuration;
		this.onFoodEaten = onFoodEaten;
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return maxUseDuration;
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.EAT;
    }
	
	@Override
	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player)
	{
		if(!worldIn.isRemote)
			onFoodEaten.accept(stack, player);
	}
}
