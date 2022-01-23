package com.mineria.mod.common.items;

import com.mineria.mod.Mineria;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ApothecariumItem extends Item
{
    public ApothecariumItem()
    {
        super(new Item.Properties().tab(Mineria.APOTHECARY_GROUP).stacksTo(1).rarity(Rarity.UNCOMMON));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltips, ITooltipFlag flag)
    {
        tooltips.add(new TranslationTextComponent("tooltip.mineria.apothecarium.wip").withStyle(style -> style.withColor(Color.fromRgb(0x5EFF8C)).withItalic(true)));
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand)
    {
        Mineria.proxy.openApothecariumScreen(player);
        player.awardStat(Stats.ITEM_USED.get(this));
        return super.use(world, player, hand);
    }
}
