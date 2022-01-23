package com.mineria.mod.common.items;

import com.mineria.mod.Mineria;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.item.UseAction;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DrinkHelper;
import net.minecraft.util.Hand;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class MagicPotionItem extends Item
{
    public MagicPotionItem()
    {
        super(new Properties().tab(Mineria.APOTHECARY_GROUP).rarity(Rarity.RARE).stacksTo(1));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, World world, LivingEntity living)
    {
        PlayerEntity player = living instanceof PlayerEntity ? (PlayerEntity) living : null;
        if (player instanceof ServerPlayerEntity)
        {
            CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayerEntity) player, stack);
        }

        if(!world.isClientSide)
        {
            living.addEffect(new EffectInstance(Effects.DAMAGE_BOOST, 20 * 30, 5));
            living.addEffect(new EffectInstance(Effects.ABSORPTION, 20 * 30, 4));
        }

        if (player != null)
        {
            player.awardStat(Stats.ITEM_USED.get(this));
            if (!player.abilities.instabuild)
            {
                stack.shrink(1);
            }
        }

        return stack;
    }

    @Override
    public int getUseDuration(ItemStack stack)
    {
        return 32;
    }

    @Override
    public UseAction getUseAnimation(ItemStack stack)
    {
        return UseAction.DRINK;
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand)
    {
        return DrinkHelper.useDrink(world, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag)
    {
        tooltip.add(new TranslationTextComponent("potion.withDuration",
                new TranslationTextComponent("potion.withAmplifier",
                        new TranslationTextComponent(Effects.DAMAGE_BOOST.getDescriptionId()),
                        new TranslationTextComponent("potion.potency.5")),
                StringUtils.formatTickDuration(30 * 20)).withStyle(TextFormatting.BLUE)
        );
        tooltip.add(new TranslationTextComponent("potion.withDuration",
                new TranslationTextComponent("potion.withAmplifier",
                        new TranslationTextComponent(Effects.ABSORPTION.getDescriptionId()),
                        new TranslationTextComponent("potion.potency.4")),
                StringUtils.formatTickDuration(30 * 20)).withStyle(TextFormatting.BLUE)
        );
    }
}
