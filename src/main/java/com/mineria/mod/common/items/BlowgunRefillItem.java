package com.mineria.mod.common.items;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.effects.PoisonSource;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlowgunRefillItem extends Item
{
    public BlowgunRefillItem()
    {
        super(new Properties().tab(Mineria.MINERIA_GROUP).stacksTo(16));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag)
    {
        boolean hasPoison = JarItem.containsPoisonSource(stack);
        PoisonSource source = JarItem.getPoisonSourceFromStack(stack);
        tooltip.add(new TranslationTextComponent("item.mineria.blowgun_refill.poison_source", hasPoison ? I18n.get(source.getTranslationKey()) : "no").withStyle(style -> hasPoison ? style.withColor(Color.fromRgb(source.getColor())) : style.withColor(TextFormatting.GRAY)));
    }
}
