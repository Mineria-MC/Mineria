package com.mineria.mod.items;

import com.google.common.collect.Lists;
import com.mineria.mod.Mineria;
import com.mineria.mod.capabilities.CapabilityRegistry;
import com.mineria.mod.init.ItemsInit;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.UseAction;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DrinkHelper;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.apache.logging.log4j.util.TriConsumer;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class DrinkItem extends Item
{
    private final Item container;
    private final TriConsumer<ItemStack, World, LivingEntity> onFoodEaten;

    public DrinkItem(Item.Properties properties)
    {
        this(properties, ItemsInit.CUP);
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
        entityLiving.getCapability(CapabilityRegistry.INGESTED_FOOD_CAP).ifPresent(cap -> cap.foodIngested(stack.getItem()));

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

    // TODO Serialize CooldownTracker
    public static void lockLaxativeDrinks(LivingEntity living)
    {
        if(living instanceof PlayerEntity)
        {
            PlayerEntity player = (PlayerEntity) living;
            getLaxativeDrinks().forEach(item -> player.getCooldownTracker().setCooldown(item, 20 * 60 * 10));
        }
    }

    public static void unlockLaxativeDrinks(LivingEntity living)
    {
        if(living instanceof PlayerEntity)
        {
            PlayerEntity player = (PlayerEntity) living;
            getLaxativeDrinks().forEach(item -> player.getCooldownTracker().removeCooldown(item));
        }
    }

    public static List<Item> getLaxativeDrinks()
    {
        return Lists.newArrayList(ItemsInit.CHARCOAL_ANTI_POISON, ItemsInit.CATHOLICON, ItemsInit.RHUBARB_TEA, ItemsInit.SENNA_TEA);
    }

    public static Map<Item, Long> getMaxDigestionTime()
    {
        return Util.make(new HashMap<>(), map -> {
            map.put(ItemsInit.ELDERBERRY_TEA, (long) (20 * 45));
            map.put(ItemsInit.STRYCHNOS_NUX_VOMICA_TEA, (long) (20 * 60 * 5));
        });
    }
}
