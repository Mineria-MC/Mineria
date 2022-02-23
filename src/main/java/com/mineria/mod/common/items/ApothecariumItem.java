package com.mineria.mod.common.items;

import com.mineria.mod.Mineria;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class ApothecariumItem extends Item
{
    public ApothecariumItem()
    {
        super(new Item.Properties().tab(Mineria.APOTHECARY_GROUP).stacksTo(1).rarity(Rarity.UNCOMMON));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltips, TooltipFlag flag)
    {
        tooltips.add(new TranslatableComponent("tooltip.mineria.apothecarium.wip").withStyle(style -> style.withColor(TextColor.fromRgb(0x5EFF8C)).withItalic(true)));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand)
    {
        Mineria.proxy.openApothecariumScreen(player);
        player.awardStat(Stats.ITEM_USED.get(this));
        return super.use(world, player, hand);
    }
}
