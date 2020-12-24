package com.mineria.mod.items;

import com.mineria.mod.Mineria;
import com.mineria.mod.init.ItemsInit;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import org.apache.logging.log4j.util.TriConsumer;

public class DrinkBase extends ItemFood
{
    private final TriConsumer<ItemStack, World, EntityPlayer> consumer;

    public DrinkBase(String name, TriConsumer<ItemStack, World, EntityPlayer> consumer)
    {
        super(0, 0, false);
        setRegistryName(name);
        setUnlocalizedName(name);
        setCreativeTab(Mineria.mineriaTab);
        setMaxStackSize(1);
        setAlwaysEdible();
        this.consumer = consumer;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 32;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.DRINK;
    }

    @Override
    protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player)
    {
        this.consumer.accept(stack, worldIn, player);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
    {
        if (entityLiving instanceof EntityPlayer)
        {
            EntityPlayer entityplayer = (EntityPlayer) entityLiving;
            worldIn.playSound((EntityPlayer) null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
            entityplayer.getFoodStats().addStats(this, stack);
            this.onFoodEaten(stack, worldIn, entityplayer);
            entityplayer.addStat(StatList.getObjectUseStats(this));

            if (entityplayer instanceof EntityPlayerMP)
            {
                CriteriaTriggers.CONSUME_ITEM.trigger((EntityPlayerMP)entityplayer, stack);
            }
        }

        if (entityLiving instanceof EntityPlayer && !((EntityPlayer)entityLiving).capabilities.isCreativeMode)
        {
            stack.shrink(1);
        }

        return stack.isEmpty() ? new ItemStack(ItemsInit.cup) : stack;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        playerIn.setActiveHand(handIn);
        return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }
}
