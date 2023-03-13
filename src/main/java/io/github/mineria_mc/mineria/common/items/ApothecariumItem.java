package io.github.mineria_mc.mineria.common.items;

import io.github.mineria_mc.mineria.Mineria;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ApothecariumItem extends Item {
    public ApothecariumItem() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level world, List<Component> tooltips, @Nonnull TooltipFlag flag) {
        tooltips.add(Component.translatable("tooltip.mineria.apothecarium.wip").withStyle(style -> style.withColor(TextColor.fromRgb(0x5EFF8C)).withItalic(true)));
    }

    @Nonnull
    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level world, @Nonnull Player player, @Nonnull InteractionHand hand) {
        Mineria.getProxy().openApothecariumScreen(player);
        player.awardStat(Stats.ITEM_USED.get(this));
        return super.use(world, player, hand);
    }
}
