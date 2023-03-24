package io.github.mineria_mc.mineria.common.items;

import io.github.mineria_mc.mineria.common.effects.util.PoisonSource;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class BlowgunRefillItem extends Item {
    public BlowgunRefillItem() {
        super(new Properties().stacksTo(16));
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level world, List<Component> tooltip, @Nonnull TooltipFlag flag) {
        boolean hasPoison = JarItem.containsPoisonSource(stack);
        PoisonSource source = JarItem.getPoisonSourceFromStack(stack);
        tooltip.add(Component.translatable("item.mineria.blowgun_refill.poison_source", hasPoison ? Component.translatable(source.getTranslationKey()) : "no").withStyle(style -> hasPoison ? style.withColor(TextColor.fromRgb(source.getColor())) : style.withColor(ChatFormatting.GRAY)));
    }
}
