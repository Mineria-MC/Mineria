package io.github.mineria_mc.mineria.common.effects.potions;

import com.google.common.collect.ImmutableList;
import io.github.mineria_mc.mineria.common.effects.instances.ModdedMobEffectInstance;
import io.github.mineria_mc.mineria.common.effects.util.PoisonSource;
import com.mojang.datafixers.util.Either;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class MineriaPotion extends Potion {
    protected final Either<ImmutableList<MobEffectInstance>, PoisonSource> effects;

    public MineriaPotion(String name, MobEffectInstance... effects) {
        this(name, Either.left(ImmutableList.copyOf(effects)));
    }

    public MineriaPotion(String name, PoisonSource source) {
        this(name, Either.right(source));
    }

    public MineriaPotion(String name, Either<ImmutableList<MobEffectInstance>, PoisonSource> effects) {
        super(name);
        this.effects = effects;
    }

    @Nonnull
    @Deprecated
    @Override
    public List<MobEffectInstance> getEffects() {
        return this.effects.left().orElse(ImmutableList.of());
    }

    @Override
    public boolean hasInstantEffects() {
        if (this.effects.left().isPresent()) {
            for (MobEffectInstance effect : this.effects.left().get()) {
                if (effect.getEffect().isInstantenous()) {
                    return true;
                }
            }
        }

        return false;
    }

    @Nonnull
    @Override
    public String getName(String translationKey) {
        return super.getName(translationKey.concat(this.effects.left().isPresent() ? "effect." : "poison_source."));
    }

    public void applyEffects(ItemStack stack, Level world, @Nullable Player player, LivingEntity living) {
        this.effects.ifLeft(effectInstances -> {
            for (MobEffectInstance effect : effectInstances) {
                if (effect.getEffect().isInstantenous()) {
                    effect.getEffect().applyInstantenousEffect(player, player, living, effect.getAmplifier(), 1.0D);
                } else {
                    living.addEffect(ModdedMobEffectInstance.copyEffect(effect));
                }
            }
        }).ifRight(poisonSource -> poisonSource.applyPoisoning(living));
    }

    public boolean showInItemGroup(CreativeModeTab group, Item potionItem) {
        return true;
    }

    public int getColor() {
        return this.effects.map(list -> -1, PoisonSource::getColor);
    }

    public Either<ImmutableList<MobEffectInstance>, PoisonSource> effects() {
        return this.effects;
    }

    public static int getColor(ItemStack stack) {
        CompoundTag nbt = stack.getTag();
        if (nbt != null && nbt.contains("CustomPotionColor", 99)) {
            return nbt.getInt("CustomPotionColor");
        }
        else {
            Potion potion = PotionUtils.getPotion(stack);
            if (potion == Potions.EMPTY) {
                return 16253176;
            }
            else if (potion instanceof MineriaPotion && ((MineriaPotion) potion).getColor() != -1) {
                return ((MineriaPotion) potion).getColor();
            }
            else {
                return PotionUtils.getColor(PotionUtils.getMobEffects(stack));
            }
        }
    }
}
