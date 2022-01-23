package com.mineria.mod.common.effects;

import com.google.common.collect.Lists;
import com.mineria.mod.common.init.MineriaItems;
import com.mineria.mod.util.VanillaOverride;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.DamageSource;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class PoisonEffect extends Effect implements IPoisonEffect, VanillaOverride
{
    private static final UUID CONVULSION_SLOWDOWN_UUID = UUID.fromString("660f9ea9-a977-4647-8b56-51d752401bf2");

    public PoisonEffect()
    {
        super(EffectType.HARMFUL, 5149489);
        setVanillaName("poison");
    }

    @Deprecated
    @Override
    public void applyEffectTick(LivingEntity living, int amplifier)
    {
        if (living.getHealth() > 1.0F)
        {
            living.hurt(DamageSource.MAGIC, getDamageToDeal(living.getHealth(), 0));
        }
    }

    @Override
    public void applyEffectTick(LivingEntity living, int amplifier, int duration, int maxDuration, int potionClass)
    {
        if (living.getHealth() > 1.0F && potionClass < 3)
        {
            living.hurt(DamageSource.MAGIC, getDamageToDeal(living.getHealth(), potionClass));
        }

        Random rand = new Random();

        if(doConvulsions(duration, maxDuration, potionClass) && !isImmune(living))
        {
            float x = rand.nextFloat() / 2;
            float z = rand.nextFloat() / 2;
            living.setDeltaMovement(living.getDeltaMovement().add(rand.nextBoolean() ? x : -x, 0.1F, rand.nextBoolean() ? z : -z));
            addMovementSpeedModifier(living, amplifier);
        }
        else if(doSpasms(duration, maxDuration, potionClass) && !isImmune(living))
        {
            float x = rand.nextFloat() / 4;
            float z = rand.nextFloat() / 4;
            living.setDeltaMovement(living.getDeltaMovement().add(rand.nextBoolean() ? x : -x, living.isOnGround() ? 0.5 : -0.5, rand.nextBoolean() ? z : -z));
        }
    }

    public static boolean isImmune(LivingEntity living)
    {
        return living instanceof PlayerEntity && ((PlayerEntity) living).abilities.mayfly;
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
                return health > 10 ? 2 : 1;
        }
        return 0;
    }

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

    @Deprecated
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier)
    {
        return isDurationEffectTick(duration, amplifier, 0);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier, int potionClass)
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

    @Deprecated
    @Override
    public List<ItemStack> getCurativeItems()
    {
        return Lists.newArrayList(new ItemStack(Items.MILK_BUCKET), new ItemStack(Items.HONEY_BOTTLE), new ItemStack(MineriaItems.MIRACLE_ANTI_POISON), new ItemStack(MineriaItems.NAUSEOUS_ANTI_POISON), new ItemStack(MineriaItems.MILK_ANTI_POISON), new ItemStack(MineriaItems.CATHOLICON), new ItemStack(MineriaItems.ANTI_POISON), new ItemStack(MineriaItems.CHARCOAL_ANTI_POISON));
    }

    @Override
    public List<ItemStack> getCurativeItems(int potionClass, int amplifier, int maxDuration, int duration, PoisonSource source)
    {
        List<ItemStack> items = Lists.newArrayList(new ItemStack(MineriaItems.MIRACLE_ANTI_POISON));

        switch (potionClass)
        {
            case 0:
                items.add(new ItemStack(Items.MILK_BUCKET));
                items.add(new ItemStack(Items.HONEY_BOTTLE));
                items.add(new ItemStack(MineriaItems.NAUSEOUS_ANTI_POISON));
                items.add(new ItemStack(MineriaItems.MILK_ANTI_POISON));
                items.add(new ItemStack(MineriaItems.ANTI_POISON));
                items.add(new ItemStack(MineriaItems.CATHOLICON));
                items.add(new ItemStack(MineriaItems.CHARCOAL_ANTI_POISON));
                break;
            case 1:
                if(maxDuration < 6000)
                    items.add(new ItemStack(MineriaItems.NAUSEOUS_ANTI_POISON));
                if(maxDuration < 3600)
                    items.add(new ItemStack(MineriaItems.CHARCOAL_ANTI_POISON));
                if(maxDuration < 2400)
                    items.add(new ItemStack(MineriaItems.MILK_ANTI_POISON));
                if(maxDuration < 1200)
                {
                    items.add(new ItemStack(Items.HONEY_BOTTLE));
                    items.add(new ItemStack(Items.MILK_BUCKET));
                }
                if(maxDuration > 600 && maxDuration < 6000)
                    items.add(new ItemStack(MineriaItems.ANTI_POISON));
                if(maxDuration - duration > 600 && maxDuration - duration < 6000) // after 30 seconds and before 5 minutes
                    items.add(new ItemStack(MineriaItems.CATHOLICON));
                if(maxDuration - duration > 1200 && maxDuration - duration < 2400) // after 1 minute and before 2 minutes
                {
                    items.add(new ItemStack(MineriaItems.RHUBARB_TEA));
                    items.add(new ItemStack(MineriaItems.SENNA_TEA));
                }
                else
                    items.add(new ItemStack(MineriaItems.MANDRAKE_ROOT_TEA));
                break;
            case 2:
                if(maxDuration < 3600)
                    items.add(new ItemStack(MineriaItems.NAUSEOUS_ANTI_POISON));
                if(maxDuration < 2400)
                    items.add(new ItemStack(MineriaItems.CHARCOAL_ANTI_POISON));
                if(maxDuration < 1200)
                    items.add(new ItemStack(MineriaItems.MILK_ANTI_POISON));
                if(maxDuration < 600)
                {
                    items.add(new ItemStack(Items.HONEY_BOTTLE));
                    items.add(new ItemStack(Items.MILK_BUCKET));
                }
                if(maxDuration > 600 && maxDuration < 6000)
                    items.add(new ItemStack(MineriaItems.ANTI_POISON));
                if(maxDuration - duration > 600 && maxDuration - duration < 6000)
                    items.add(new ItemStack(MineriaItems.CATHOLICON));
                if(maxDuration - duration > 1200 && maxDuration - duration < 2400)
                    items.add(new ItemStack(MineriaItems.SENNA_TEA));
                break;
            case 3:
                if(maxDuration < 1200)
                    items.add(new ItemStack(MineriaItems.NAUSEOUS_ANTI_POISON));
                break;
        }

        if(source == PoisonSource.MANDRAKE && maxDuration - duration < 6000)
            items.add(new ItemStack(MineriaItems.MANDRAKE_ROOT_TEA));

        return items;
    }

    private void addMovementSpeedModifier(LivingEntity living, int amplifier)
    {
        ModifiableAttributeInstance attributeInstance = living.getAttribute(Attributes.MOVEMENT_SPEED);
        if(attributeInstance != null)
        {
            AttributeModifier modifier = new AttributeModifier(CONVULSION_SLOWDOWN_UUID, this::getDescriptionId, -15, AttributeModifier.Operation.MULTIPLY_TOTAL);
            if(!attributeInstance.hasModifier(modifier))
            {
                attributeInstance.addPermanentModifier(new AttributeModifier(modifier.getId(), this.getDescriptionId() + " " + amplifier, this.getAttributeModifierValue(amplifier, modifier), modifier.getOperation()));
            }
        }
    }

    public void removeMovementSpeedModifier(LivingEntity living)
    {
        ModifiableAttributeInstance attributeInstance = living.getAttribute(Attributes.MOVEMENT_SPEED);
        if(attributeInstance != null)
        {
            attributeInstance.removePermanentModifier(CONVULSION_SLOWDOWN_UUID);
        }
    }
}
