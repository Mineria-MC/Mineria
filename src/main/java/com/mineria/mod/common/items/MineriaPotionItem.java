package com.mineria.mod.common.items;

import com.mineria.mod.common.effects.CustomEffectInstance;
import com.mineria.mod.common.effects.PoisonSource;
import com.mineria.mod.common.effects.potions.MineriaPotion;
import com.mineria.mod.common.init.MineriaItems;
import com.mineria.mod.common.init.MineriaPotions;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraftforge.fmllegacy.RegistryObject;

import javax.annotation.Nullable;
import java.util.List;

public class MineriaPotionItem extends PotionItem
{
    public MineriaPotionItem(Properties properties)
    {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity living)
    {
        Player player = living instanceof Player ? (Player) living : null;
        if (player instanceof ServerPlayer)
        {
            CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer) player, stack);
        }

        if (!world.isClientSide)
        {
            Potion potion = PotionUtils.getPotion(stack);
            if(potion instanceof MineriaPotion)
            {
                ((MineriaPotion) potion).applyEffects(stack, world, player, living);
            }
            else
            {
                for (MobEffectInstance effect : PotionUtils.getMobEffects(stack))
                {
                    if (effect.getEffect().isInstantenous())
                    {
                        effect.getEffect().applyInstantenousEffect(player, player, living, effect.getAmplifier(), 1.0D);
                    } else
                    {
                        living.addEffect(CustomEffectInstance.copyEffect(effect));
                    }
                }
            }
        }

        if (player != null)
        {
            player.awardStat(Stats.ITEM_USED.get(this));
            if (!player.getAbilities().instabuild)
            {
                stack.shrink(1);
            }
        }

        if (player == null || !player.getAbilities().instabuild)
        {
            if (stack.isEmpty())
            {
                return new ItemStack(Items.GLASS_BOTTLE);
            }

            if (player != null)
            {
                player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
            }
        }

        return stack;
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items)
    {
        if(this.allowdedIn(group))
        {
            MineriaPotions.POTIONS.getEntries().stream().map(RegistryObject::get).forEach(potion -> {
                if (potion instanceof MineriaPotion)
                {
                    if (((MineriaPotion) potion).showInItemGroup(group, this))
                    {
                        items.add(PotionUtils.setPotion(new ItemStack(this), potion));
                    }
                }
                else
                    items.add(PotionUtils.setPotion(new ItemStack(this), potion));
            });
        }
    }

    @Override
    public String getDescriptionId(ItemStack stack)
    {
        Potion potion = PotionUtils.getPotion(stack);
        return potion.getName(this.getDescriptionId().concat(potion instanceof MineriaPotion ? "." : ".effect."));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag)
    {
        Potion potion = PotionUtils.getPotion(stack);
        if(potion instanceof MineriaPotion && ((MineriaPotion) potion).effects().right().isPresent())
        {
            PoisonSource poisonSource = ((MineriaPotion) potion).effects().right().get();
            tooltip.add(new TranslatableComponent("item.mineria.mineria_potion.poison_source_tooltip").withStyle(ChatFormatting.GRAY));
            tooltip.add(new TranslatableComponent(poisonSource.getTranslationKey()).withStyle(style -> style.withColor(TextColor.fromRgb(poisonSource.getColor()))));
        }
        else PotionUtils.addPotionTooltip(stack, tooltip, 1.0F);
    }

    public static Item getItemForPotion(Item item, Potion potion)
    {
        if(potion.getRegistryName().getNamespace().equals("mineria"))
        {
            if(item.equals(Items.POTION)) return MineriaItems.MINERIA_POTION;
            if(item.equals(Items.SPLASH_POTION)) return MineriaItems.MINERIA_SPLASH_POTION;
            if(item.equals(Items.LINGERING_POTION)) return MineriaItems.MINERIA_LINGERING_POTION;
        }
        return item;
    }
}
