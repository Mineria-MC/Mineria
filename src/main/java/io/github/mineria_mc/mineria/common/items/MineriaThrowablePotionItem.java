package io.github.mineria_mc.mineria.common.items;

import io.github.mineria_mc.mineria.common.entity.MineriaPotionEntity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class MineriaThrowablePotionItem extends MineriaPotionItem {
    private final boolean lingering;

    public MineriaThrowablePotionItem(Properties properties, boolean lingering) {
        super(properties);
        this.lingering = lingering;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        PotionUtils.addPotionTooltip(stack, tooltip, lingering ? 0.25F : 1.0F);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (lingering)
            world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.LINGERING_POTION_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
        else
            world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SPLASH_POTION_THROW, SoundSource.PLAYERS, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));

        ItemStack potionStack = player.getItemInHand(hand);

        if (!world.isClientSide) {
            MineriaPotionEntity entity = new MineriaPotionEntity(world, player);
            entity.setItem(potionStack);
            entity.shootFromRotation(player, player.getXRot(), player.getYRot(), -20.0F, 0.5F, 1.0F);
            world.addFreshEntity(entity);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.getAbilities().instabuild) {
            potionStack.shrink(1);
        }

        return InteractionResultHolder.sidedSuccess(potionStack, world.isClientSide());
    }
}
