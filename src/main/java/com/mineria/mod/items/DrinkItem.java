package com.mineria.mod.items;

import com.mineria.mod.Mineria;
import com.mineria.mod.init.ItemsInit;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DrinkHelper;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.apache.logging.log4j.util.TriConsumer;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class DrinkItem extends Item
{
    private final Item container;
    private final TriConsumer<ItemStack, World, LivingEntity> onFoodEaten;

    public DrinkItem(Item.Properties properties)
    {
        this(properties, ItemsInit.CUP, (stack, world, living) -> {});
    }

    public DrinkItem(Item.Properties properties, TriConsumer<ItemStack, World, LivingEntity> onFoodEaten)
    {
        this(properties, ItemsInit.CUP, onFoodEaten);
    }

    public DrinkItem(Item.Properties properties, Item container)
    {
        this(properties, container, (stack, world, living) -> {});
    }

    public DrinkItem(Item.Properties properties, Item container, TriConsumer<ItemStack, World, LivingEntity> onFoodEaten)
    {
        super(properties);
        this.container = container;
        this.onFoodEaten = onFoodEaten;
    }

    @Override
    public UseAction getUseAction(ItemStack stack)
    {
        return UseAction.DRINK;
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving)
    {
        if (entityLiving instanceof ServerPlayerEntity)
        {
            ServerPlayerEntity serverplayerentity = (ServerPlayerEntity)entityLiving;
            CriteriaTriggers.CONSUME_ITEM.trigger(serverplayerentity, stack);
            serverplayerentity.addStat(Stats.ITEM_USED.get(this));
        }

        if(!worldIn.isRemote) entityLiving.curePotionEffects(stack);
        if(!worldIn.isRemote) onFoodEaten.accept(stack, worldIn, entityLiving);

        try
        {
            ObfuscationReflectionHelper.findMethod(LivingEntity.class, "func_213349_a", ItemStack.class, World.class, LivingEntity.class).invoke(entityLiving, stack, worldIn, entityLiving);
        }
        catch (IllegalAccessException | InvocationTargetException e)
        {
            Mineria.LOGGER.error("Couldn't apply effect for DrinkItem " + this.getRegistryName().toString());
            e.printStackTrace();
        }

        if (entityLiving instanceof PlayerEntity && !((PlayerEntity)entityLiving).abilities.isCreativeMode)
        {
            stack.shrink(1);
        }

        return stack.isEmpty() ? new ItemStack(this.container) : stack;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        return DrinkHelper.startDrinking(worldIn, playerIn, handIn);
    }
}
