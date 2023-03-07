package io.github.mineria_mc.mineria.common.items;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.InteractionHand;
import net.minecraft.util.StringUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class MagicPotionItem extends Item {
    public MagicPotionItem() {
        super(new Properties().rarity(Rarity.RARE).stacksTo(1));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity living) {
        Player player = living instanceof Player ? (Player) living : null;
        if (player instanceof ServerPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer) player, stack);
        }

        if (!world.isClientSide) {
            living.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 20 * 30, 5));
            living.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 20 * 30, 4));
        }

        if (player != null) {
            player.awardStat(Stats.ITEM_USED.get(this));
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }

        return stack;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        return ItemUtils.startUsingInstantly(world, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("potion.withDuration",
                Component.translatable("potion.withAmplifier",
                        Component.translatable(MobEffects.DAMAGE_BOOST.getDescriptionId()),
                        Component.translatable("potion.potency.5")),
                StringUtil.formatTickDuration(30 * 20)).withStyle(ChatFormatting.BLUE)
        );
        tooltip.add(Component.translatable("potion.withDuration",
                Component.translatable("potion.withAmplifier",
                        Component.translatable(MobEffects.ABSORPTION.getDescriptionId()),
                        Component.translatable("potion.potency.4")),
                StringUtil.formatTickDuration(30 * 20)).withStyle(ChatFormatting.BLUE)
        );
    }
}
