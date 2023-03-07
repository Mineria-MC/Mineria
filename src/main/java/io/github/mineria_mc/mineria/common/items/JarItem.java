package io.github.mineria_mc.mineria.common.items;

import io.github.mineria_mc.mineria.common.effects.util.PoisonSource;
import io.github.mineria_mc.mineria.common.entity.JarEntity;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.stats.Stats;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;

public class JarItem extends Item implements DyeableLeatherItem {
    public JarItem() {
        super(new Properties().stacksTo(4));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        boolean hasPoison = containsPoisonSource(stack);
        PoisonSource source = getPoisonSourceFromStack(stack);
        tooltip.add(Component.translatable("item.mineria.jar.poison_source", hasPoison ? I18n.get(source.getTranslationKey()) : "no").withStyle(style -> hasPoison ? style.withColor(TextColor.fromRgb(source.getColor())) : style.withColor(ChatFormatting.GRAY)));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SPLASH_POTION_THROW, SoundSource.PLAYERS, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));

        ItemStack jarStack = player.getItemInHand(hand);

        if (!world.isClientSide) {
            JarEntity jarEntity = new JarEntity(player, world);
            jarEntity.setItem(jarStack);
            jarEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), -20.0F, 0.5F, 1.0F);
            world.addFreshEntity(jarEntity);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.getAbilities().instabuild) {
            jarStack.shrink(1);
        }

        return InteractionResultHolder.sidedSuccess(jarStack, world.isClientSide());
    }

    @Override
    public int getColor(ItemStack stack) {
        CompoundTag nbt = stack.getTagElement("display");
        return nbt != null && nbt.contains("color", 99) ? nbt.getInt("color") : 16777215;
    }

    public static PoisonSource getPoisonSourceFromStack(ItemStack stack) {
        CompoundTag nbt = stack.getOrCreateTag();
        return nbt.contains("PoisonSource") ? PoisonSource.byName(ResourceLocation.tryParse(nbt.getString("PoisonSource"))) : PoisonSource.UNKNOWN;
    }

    public static boolean containsPoisonSource(ItemStack stack) {
        CompoundTag nbt = stack.getOrCreateTag();
        return nbt.contains("PoisonSource");
    }

    public static boolean isLingering(ItemStack stack) {
        CompoundTag nbt = stack.getOrCreateTag();
        return nbt.contains("Lingering") && nbt.getBoolean("Lingering");
    }

    public static ItemStack addPoisonSourceToStack(ItemStack stack, PoisonSource source) {
        CompoundTag nbt = stack.getOrCreateTag();
        nbt.putString("PoisonSource", source.getId().toString());
        return stack;
    }
}
