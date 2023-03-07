package io.github.mineria_mc.mineria.common.items;

import io.github.mineria_mc.mineria.common.effects.instances.ModdedMobEffectInstance;
import io.github.mineria_mc.mineria.common.effects.potions.MineriaPotion;
import io.github.mineria_mc.mineria.common.effects.util.PoisonSource;
import io.github.mineria_mc.mineria.common.init.MineriaItems;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class MineriaPotionItem extends PotionItem {
    public MineriaPotionItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity living) {
        Player player = living instanceof Player ? (Player) living : null;
        if (player instanceof ServerPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer) player, stack);
        }

        if (!world.isClientSide) {
            Potion potion = PotionUtils.getPotion(stack);
            if (potion instanceof MineriaPotion) {
                ((MineriaPotion) potion).applyEffects(stack, world, player, living);
            } else {
                for (MobEffectInstance effect : PotionUtils.getMobEffects(stack)) {
                    if (effect.getEffect().isInstantenous()) {
                        effect.getEffect().applyInstantenousEffect(player, player, living, effect.getAmplifier(), 1.0D);
                    } else {
                        living.addEffect(ModdedMobEffectInstance.copyEffect(effect));
                    }
                }
            }
        }

        if (player != null) {
            player.awardStat(Stats.ITEM_USED.get(this));
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }

        if (player == null || !player.getAbilities().instabuild) {
            if (stack.isEmpty()) {
                return new ItemStack(Items.GLASS_BOTTLE);
            }

            if (player != null) {
                player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
            }
        }

        return stack;
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        Potion potion = PotionUtils.getPotion(stack);
        return potion.getName(this.getDescriptionId().concat(potion instanceof MineriaPotion ? "." : ".effect."));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        Potion potion = PotionUtils.getPotion(stack);
        if (potion instanceof MineriaPotion mineriaPotion && mineriaPotion.effects().right().isPresent()) {
            PoisonSource poisonSource = mineriaPotion.effects().right().get();
            tooltip.add(Component.translatable("item.mineria.mineria_potion.poison_source_tooltip").withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.translatable(poisonSource.getTranslationKey()).withStyle(style -> style.withColor(TextColor.fromRgb(poisonSource.getColor()))));
        } else {
            PotionUtils.addPotionTooltip(stack, tooltip, 1.0F);
        }
    }

    public static Item getItemForPotion(Item item, Potion potion) {
        if (Objects.requireNonNull(ForgeRegistries.POTIONS.getKey(potion)).getNamespace().equals("mineria")) {
            if (item.equals(Items.POTION)) {
                return MineriaItems.MINERIA_POTION.get();
            }
            if (item.equals(Items.SPLASH_POTION)) {
                return MineriaItems.MINERIA_SPLASH_POTION.get();
            }
            if (item.equals(Items.LINGERING_POTION)) {
                return MineriaItems.MINERIA_LINGERING_POTION.get();
            }
        }
        return item;
    }
}
