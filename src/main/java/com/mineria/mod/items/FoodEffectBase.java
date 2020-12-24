package com.mineria.mod.items;

import com.mineria.mod.Mineria;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.apache.logging.log4j.util.TriConsumer;

public class FoodEffectBase extends ItemFood
{
	private final TriConsumer<ItemStack, World, EntityPlayer> consumer;

	public FoodEffectBase(String name, int amount, float saturation, boolean isWolfFood, TriConsumer<ItemStack, World, EntityPlayer> consumer)
	{
		super(amount, saturation, isWolfFood);
		setUnlocalizedName(name);
		setRegistryName(name);
		setAlwaysEdible();
		setCreativeTab(Mineria.mineriaTab);
		this.consumer = consumer;
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 16;
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.EAT;
    }
	
	@Override
	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player)
	{
		/*if(!worldIn.isRemote)
		{
			player.addPotionEffect(new PotionEffect(effect1.getPotion(), effect1.getDuration(), effect1.getAmplifier(), effect1.getIsAmbient(), effect1.doesShowParticles()));
			player.addPotionEffect(new PotionEffect(effect2.getPotion(), effect2.getDuration(), effect2.getAmplifier(), effect2.getIsAmbient(), effect2.doesShowParticles()));
			player.addPotionEffect(new PotionEffect(effect3.getPotion(), effect3.getDuration(), effect3.getAmplifier(), effect3.getIsAmbient(), effect3.doesShowParticles()));
		}*/
		consumer.accept(stack, worldIn, player);
	}
}
