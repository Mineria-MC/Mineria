package io.github.mineria_mc.mineria.common.items;

import io.github.mineria_mc.mineria.util.KeyboardHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class DiamondBarrelUpgradeItem extends Item {
    protected final boolean stackable;

    public DiamondBarrelUpgradeItem(boolean stackable) {
        super(new Properties().stacksTo(1));
        this.stackable = stackable;
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level level, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag advanced) {
        if(KeyboardHelper.isShiftKeyDown()) {
            tooltip.add(Component.translatable("tooltip.mineria.upgrade").withStyle(ChatFormatting.LIGHT_PURPLE).append(": ").append(Component.translatable("tooltip.mineria.".concat(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(this)).getPath()))));
            tooltip.add(Component.translatable("tooltip.mineria.only_for_diamond_fluid_barrel").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
            tooltip.add(Component.translatable(stackable ? "tooltip.mineria.stackable_effect" : "tooltip.mineria.non_stackable_effect").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        } else {
            tooltip.add(Component.translatable("tooltip.mineria.water_barrel.hold_shift").withStyle(ChatFormatting.GRAY));
        }
    }
}
