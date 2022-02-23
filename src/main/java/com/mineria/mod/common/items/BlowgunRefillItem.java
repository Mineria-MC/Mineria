package com.mineria.mod.common.items;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.effects.PoisonSource;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.world.item.Item.Properties;

public class BlowgunRefillItem extends Item
{
    public BlowgunRefillItem()
    {
        super(new Properties().tab(Mineria.MINERIA_GROUP).stacksTo(16));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag)
    {
        boolean hasPoison = JarItem.containsPoisonSource(stack);
        PoisonSource source = JarItem.getPoisonSourceFromStack(stack);
        tooltip.add(new TranslatableComponent("item.mineria.blowgun_refill.poison_source", hasPoison ? I18n.get(source.getTranslationKey()) : "no").withStyle(style -> hasPoison ? style.withColor(TextColor.fromRgb(source.getColor())) : style.withColor(ChatFormatting.GRAY)));
    }
}
