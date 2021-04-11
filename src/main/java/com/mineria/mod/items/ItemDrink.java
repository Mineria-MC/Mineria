package com.mineria.mod.items;

import com.mineria.mod.Mineria;
import com.mineria.mod.init.ItemsInit;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import org.apache.logging.log4j.util.BiConsumer;

public class ItemDrink extends ItemCustomFood
{
    public ItemDrink(BiConsumer<ItemStack, EntityPlayer> onFoodEaten)
    {
        super(0, 0, false, true, onFoodEaten);
        setCreativeTab(Mineria.MINERIA_TAB);
        setMaxStackSize(1);
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.DRINK;
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase living)
    {
        if (living instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) living;
            worldIn.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
            player.getFoodStats().addStats(this, stack);
            this.onFoodEaten(stack, worldIn, player);
            player.addStat(StatList.getObjectUseStats(this));

            if (player instanceof EntityPlayerMP)
            {
                CriteriaTriggers.CONSUME_ITEM.trigger((EntityPlayerMP)player, stack);
            }
        }

        if (living instanceof EntityPlayer && !((EntityPlayer)living).capabilities.isCreativeMode)
        {
            stack.shrink(1);
        }

        return stack.isEmpty() ? new ItemStack(ItemsInit.CUP) : stack;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        playerIn.setActiveHand(handIn);
        return ActionResult.newResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }
}
