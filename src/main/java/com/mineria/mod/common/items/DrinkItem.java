package com.mineria.mod.common.items;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.init.MineriaItems;
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
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.apache.logging.log4j.util.TriConsumer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DrinkItem extends Item
{
    private final DrinkItem.Properties drinkProperties;

    public DrinkItem(Item.Properties properties, DrinkItem.Properties drinkProperties)
    {
        super(properties);
        this.drinkProperties = drinkProperties;
//        if(drinkProperties.maxDigestionTime >= 0) DrinkItem.MAX_DIGESTION_TIME_MAP.put(this, drinkProperties.maxDigestionTime);
    }

    public DrinkItem(Item.Properties properties)
    {
        this(properties, new DrinkItem.Properties());
    }

    @Override
    public UseAction getUseAnimation(ItemStack stack)
    {
        return UseAction.DRINK;
    }

    private static Method ADD_EAT_EFFECT;
    @Override
    public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entityLiving)
    {
        if (entityLiving instanceof ServerPlayerEntity)
        {
            ServerPlayerEntity serverplayerentity = (ServerPlayerEntity)entityLiving;
            CriteriaTriggers.CONSUME_ITEM.trigger(serverplayerentity, stack);
            serverplayerentity.awardStat(Stats.ITEM_USED.get(this));
        }

        if(!worldIn.isClientSide && this.drinkProperties.immediateCureEffects) entityLiving.curePotionEffects(stack);
        if(!worldIn.isClientSide) this.drinkProperties.onFoodEaten.accept(stack, worldIn, entityLiving);
//        entityLiving.getCapability(CapabilityRegistry.INGESTED_FOOD_CAP).ifPresent(cap -> cap.foodIngested(stack.getItem()));

        try
        {
            if(ADD_EAT_EFFECT == null)
                ADD_EAT_EFFECT = ObfuscationReflectionHelper.findMethod(LivingEntity.class, "func_213349_a", ItemStack.class, World.class, LivingEntity.class);
            ADD_EAT_EFFECT.invoke(entityLiving, stack, worldIn, entityLiving);
        }
        catch (IllegalAccessException | InvocationTargetException e)
        {
            Mineria.LOGGER.error("Couldn't apply effect for DrinkItem " + this.getRegistryName().toString());
            e.printStackTrace();
        }

        boolean survivalPlayer = entityLiving instanceof PlayerEntity && !((PlayerEntity)entityLiving).abilities.instabuild;

        if (survivalPlayer)
        {
            stack.shrink(1);
        }

        ItemStack containerStack = new ItemStack(this.drinkProperties.container);

        if(stack.isEmpty())
            return containerStack;
        else
        {
            if(survivalPlayer)
            {
                PlayerEntity player = (PlayerEntity) entityLiving;
                if(!player.inventory.add(containerStack))
                    player.drop(containerStack, false);
            }
            return stack;
        }
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        return DrinkHelper.useDrink(worldIn, playerIn, handIn);
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack)
    {
        return new ItemStack(this.drinkProperties.container);
    }

    public static void lockLaxativeDrinks(LivingEntity living)
    {
        if(living instanceof PlayerEntity)
        {
            PlayerEntity player = (PlayerEntity) living;
            MineriaItems.Tags.LAXATIVE_DRINKS.getValues().forEach(item -> player.getCooldowns().addCooldown(item, 20 * 60 * 10));
        }
    }

    public static void unlockLaxativeDrinks(LivingEntity living)
    {
        if(living instanceof PlayerEntity)
        {
            PlayerEntity player = (PlayerEntity) living;
            MineriaItems.Tags.LAXATIVE_DRINKS.getValues().forEach(player.getCooldowns()::removeCooldown);
        }
    }

    public static class Properties
    {
        private Item container = MineriaItems.CUP;
        private TriConsumer<ItemStack, World, LivingEntity> onFoodEaten = (stack, world, living) -> {};
        private boolean immediateCureEffects = true;
//        private long maxDigestionTime = -1;

        public Properties container(Item item)
        {
            this.container = item;
            return this;
        }

        public Properties onFoodEaten(TriConsumer<ItemStack, World, LivingEntity> onFoodEaten)
        {
            this.onFoodEaten = onFoodEaten;
            return this;
        }

        public Properties doNotImmediateCureEffects()
        {
            this.immediateCureEffects = false;
            return this;
        }

        /*public Properties maxDigestionTime(long maxDigestionTime)
        {
            this.maxDigestionTime = maxDigestionTime;
            return this;
        }*/
    }
}
