package com.mineria.mod.common.effects.potions;

import com.google.common.collect.ImmutableList;
import com.mineria.mod.common.effects.CustomEffectInstance;
import com.mineria.mod.common.effects.PoisonSource;
import com.mineria.mod.common.items.MineriaPotionItem;
import com.mojang.datafixers.util.Either;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class MineriaPotion extends Potion
{
    private final Either<ImmutableList<EffectInstance>, PoisonSource> effects;

    public MineriaPotion(String name, EffectInstance... effects)
    {
        super(name);
        this.effects = Either.left(ImmutableList.copyOf(effects));
    }

    public MineriaPotion(String name, PoisonSource source)
    {
        super(name);
        this.effects = Either.right(source);
    }

    @Deprecated
    @Override
    public List<EffectInstance> getEffects()
    {
        return this.effects.left().orElse(ImmutableList.of());
    }

    @Override
    public boolean hasInstantEffects()
    {
        if(this.effects.left().isPresent())
        {
            for(EffectInstance effect : this.effects.left().get())
            {
                if(effect.getEffect().isInstantenous())
                {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public String getName(String translationKey)
    {
        return super.getName(translationKey.concat(this.effects.left().isPresent() ? "effect." : "poison_source."));
    }

    public void applyEffects(ItemStack stack, World world, @Nullable PlayerEntity player, LivingEntity living)
    {
        this.effects.ifLeft(effectInstances -> {
            for (EffectInstance effect : effectInstances)
            {
                if (effect.getEffect().isInstantenous())
                {
                    effect.getEffect().applyInstantenousEffect(player, player, living, effect.getAmplifier(), 1.0D);
                } else
                {
                    living.addEffect(CustomEffectInstance.copyEffect(effect));
                }
            }
        });
        this.effects.ifRight(poisonSource -> poisonSource.poison(living));
    }

    public boolean showInItemGroup(ItemGroup group, MineriaPotionItem potionItem)
    {
        return true;
    }

    public int getColor()
    {
        return this.effects.right().isPresent() ? this.effects.right().get().getColor() : -1;
    }

    public Either<ImmutableList<EffectInstance>, PoisonSource> effects()
    {
        return this.effects;
    }

    public static int getColor(ItemStack stack)
    {
        CompoundNBT nbt = stack.getTag();
        if (nbt != null && nbt.contains("CustomPotionColor", 99))
            return nbt.getInt("CustomPotionColor");
        else
        {
            Potion potion = PotionUtils.getPotion(stack);
            if(potion == Potions.EMPTY)
                return 16253176;
            else if(potion instanceof MineriaPotion && ((MineriaPotion) potion).getColor() != -1)
                return ((MineriaPotion) potion).getColor();
            else
                return PotionUtils.getColor(PotionUtils.getMobEffects(stack));
        }
    }
}
