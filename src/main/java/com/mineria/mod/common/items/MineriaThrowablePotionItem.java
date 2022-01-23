package com.mineria.mod.common.items;

import com.mineria.mod.common.entity.MineriaPotionEntity;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class MineriaThrowablePotionItem extends MineriaPotionItem
{
    private final boolean lingering;

    public MineriaThrowablePotionItem(Properties properties, boolean lingering)
    {
        super(properties);
        this.lingering = lingering;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag)
    {
        PotionUtils.addPotionTooltip(stack, tooltip, lingering ? 0.25F : 1.0F);
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand)
    {
        if(lingering)
            world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.LINGERING_POTION_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
        else
            world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SPLASH_POTION_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));

        ItemStack potionStack = player.getItemInHand(hand);

        if (!world.isClientSide)
        {
            MineriaPotionEntity entity = new MineriaPotionEntity(world, player);
            entity.setItem(potionStack);
            entity.shootFromRotation(player, player.xRot, player.yRot, -20.0F, 0.5F, 1.0F);
            world.addFreshEntity(entity);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.abilities.instabuild)
        {
            potionStack.shrink(1);
        }

        return ActionResult.sidedSuccess(potionStack, world.isClientSide());
    }
}
