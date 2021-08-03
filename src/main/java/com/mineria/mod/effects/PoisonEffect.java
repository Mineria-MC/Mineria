package com.mineria.mod.effects;

import com.google.common.collect.Lists;
import com.mineria.mod.init.ItemsInit;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.DamageSource;

import java.util.List;
import java.util.Random;

public class PoisonEffect extends Effect implements IPoisonEffect
{
    public PoisonEffect()
    {
        super(EffectType.HARMFUL, 5149489);
    }

    @Override
    public void performEffect(LivingEntity living, int amplifier)
    {
        if (living.getHealth() > 1.0F)
        {
            living.attackEntityFrom(DamageSource.MAGIC, getDamageToDeal(living.getHealth(), 0));
        }
    }

    @Override
    public void performEffect(LivingEntity living, int amplifier, int duration, int maxDuration, int potionClass)
    {
        if (living.getHealth() > 1.0F)
        {
            living.attackEntityFrom(DamageSource.MAGIC, getDamageToDeal(living.getHealth(), potionClass));
        }

        Random rand = new Random();

        if(doConvulsions(duration, maxDuration, potionClass) && !isImmune(living))
        {
            float x = rand.nextFloat() / 2;
            float z = rand.nextFloat() / 2;
            living.setMotion(living.getMotion().add(rand.nextBoolean() ? x : -x, 0.1F, rand.nextBoolean() ? z : -z));
        }
        else if(doSpasms(duration, maxDuration, potionClass) && !isImmune(living))
        {
            float x = rand.nextFloat() / 4;
            float z = rand.nextFloat() / 4;
            living.setMotion(living.getMotion().add(rand.nextBoolean() ? x : -x, living.isOnGround() ? 0.5 : -0.5, rand.nextBoolean() ? z : -z));
        }
    }

    public static boolean isImmune(LivingEntity living)
    {
        return living instanceof PlayerEntity && ((PlayerEntity) living).abilities.allowFlying;
    }

    private float getDamageToDeal(float health, int potionClass)
    {
        switch (potionClass)
        {
            case 0:
            case 2:
                return health - 2 < 1 ? 1 : 2;
            case 1:
                return 1;
            case 3:
                return health > 10 ? 2 : (health - 4 < 1 ? health - 1 : health - 4); // if health > 10 we deal 1 heart of dmg, else we deal 2 hearts of dmg (we assert that the player stays at half-a-heart).
        }
        return 0;
    }

    // TODO "Utilisiation incontrôlée des bras"
    @Override
    public boolean doSpasms(int duration, int maxDuration, int potionClass)
    {
        switch (potionClass)
        {
            case 0:
                return maxDuration - duration >= 2400; // If the potion is applied for 2 minutes (20 * 60 * 2)
            case 1:
                return maxDuration - duration >= 3600; // If the potion is applied for 3 minutes (20 * 60 * 3)
            case 2:
                return maxDuration - duration >= 1200; // If the potion is applied for 1 minutes (20 * 60)
        }
        return false;
    }

    @Override
    public boolean doConvulsions(int duration, int maxDuration, int potionClass)
    {
        return potionClass == 2 && maxDuration - duration >= 3600;
    }

    @Override
    public boolean isReady(int duration, int amplifier)
    {
        return isReady(duration, amplifier, 0);
    }

    @Override
    public boolean isReady(int duration, int amplifier, int potionClass)
    {
        switch (potionClass)
        {
            case 0:
                return duration % 60 == 0; // 3 seconds delay
            case 1:
                return duration % 40 == 0; // 2 seconds delay
            case 2:
                return duration % 30 == 0; // 1.5 seconds delay
            case 3:
                return duration % 20 == 0; // 1-second delay
        }
        return false;
    }

    @Override
    public List<ItemStack> getCurativeItems()
    {
        return Lists.newArrayList(new ItemStack(Items.MILK_BUCKET), new ItemStack(ItemsInit.MIRACLE_ANTI_POISON));
    }

    @Override
    public List<ItemStack> getCurativeItems(int potionClass, int amplifier, int maxDuration, int duration, PoisonSource source)
    {
        List<ItemStack> items = Lists.newArrayList(new ItemStack(ItemsInit.MIRACLE_ANTI_POISON));

        switch (potionClass)
        {
            case 0:
                items.add(new ItemStack(Items.MILK_BUCKET));
                break;
            case 1:
                if(maxDuration < 6000)
                    items.add(new ItemStack(ItemsInit.NAUSEOUS_ANTI_POISON));
                if(maxDuration < 3600)
                    items.add(new ItemStack(ItemsInit.CHARCOAL_ANTI_POISON));
                if(maxDuration < 2400)
                    items.add(new ItemStack(ItemsInit.MILK_ANTI_POISON));
                if(maxDuration < 1200)
                    items.add(new ItemStack(Items.MILK_BUCKET));
                if(maxDuration > 600 && maxDuration < 6000)
                    items.add(new ItemStack(ItemsInit.ANTI_POISON));
                if(maxDuration - duration > 1200) // after 1 minute
                {
                    if(maxDuration - duration < 6000) // before 5 minutes
                        items.add(new ItemStack(ItemsInit.CATHOLICON));
                }
                else
                    items.add(new ItemStack(ItemsInit.MANDRAKE_ROOT_TEA));
                break;
            case 2:
                if(maxDuration < 3600)
                    items.add(new ItemStack(ItemsInit.NAUSEOUS_ANTI_POISON));
                if(maxDuration < 2400)
                    items.add(new ItemStack(ItemsInit.CHARCOAL_ANTI_POISON));
                if(maxDuration < 1200)
                    items.add(new ItemStack(ItemsInit.MILK_ANTI_POISON));
                if(maxDuration < 600)
                    items.add(new ItemStack(Items.MILK_BUCKET));
                if(maxDuration > 600 && maxDuration < 6000)
                    items.add(new ItemStack(ItemsInit.ANTI_POISON));
                if(maxDuration - duration > 1200)
                {
                    if(maxDuration - duration < 6000)
                        items.add(new ItemStack(ItemsInit.CATHOLICON));
                }
                break;
            case 3:
                if(maxDuration < 1200)
                    items.add(new ItemStack(ItemsInit.NAUSEOUS_ANTI_POISON));
                break;
        }

        if(source == PoisonSource.MANDRAKE && maxDuration < 6000)
            items.add(new ItemStack(ItemsInit.MANDRAKE_ROOT_TEA));

        return items;
    }
}
