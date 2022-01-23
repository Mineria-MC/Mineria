package com.mineria.mod.common.items;

import com.mineria.mod.common.effects.CustomEffectInstance;
import com.mineria.mod.common.effects.PoisonSource;
import com.mineria.mod.common.effects.potions.MineriaPotion;
import com.mineria.mod.common.init.MineriaItems;
import com.mineria.mod.common.init.MineriaPotions;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.stats.Stats;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nullable;
import java.util.List;

public class MineriaPotionItem extends PotionItem
{
    public MineriaPotionItem(Properties properties)
    {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, World world, LivingEntity living)
    {
        PlayerEntity player = living instanceof PlayerEntity ? (PlayerEntity) living : null;
        if (player instanceof ServerPlayerEntity)
        {
            CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayerEntity) player, stack);
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
                for (EffectInstance effect : PotionUtils.getMobEffects(stack))
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
            if (!player.abilities.instabuild)
            {
                stack.shrink(1);
            }
        }

        if (player == null || !player.abilities.instabuild)
        {
            if (stack.isEmpty())
            {
                return new ItemStack(Items.GLASS_BOTTLE);
            }

            if (player != null)
            {
                player.inventory.add(new ItemStack(Items.GLASS_BOTTLE));
            }
        }

        return stack;
    }

    @Override
    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items)
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
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag)
    {
        Potion potion = PotionUtils.getPotion(stack);
        if(potion instanceof MineriaPotion && ((MineriaPotion) potion).effects().right().isPresent())
        {
            PoisonSource poisonSource = ((MineriaPotion) potion).effects().right().get();
            tooltip.add(new TranslationTextComponent("item.mineria.mineria_potion.poison_source_tooltip").withStyle(TextFormatting.GRAY));
            tooltip.add(new TranslationTextComponent(poisonSource.getTranslationKey()).withStyle(style -> style.withColor(Color.fromRgb(poisonSource.getColor()))));
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
