package com.mineria.mod.common.items;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.effects.PoisonSource;
import com.mineria.mod.common.entity.JarEntity;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IDyeableArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class JarItem extends Item implements IDyeableArmorItem
{
    public JarItem()
    {
        super(new Properties().stacksTo(4).tab(Mineria.APOTHECARY_GROUP));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag)
    {
        boolean hasPoison = containsPoisonSource(stack);
        PoisonSource source = getPoisonSourceFromStack(stack);
        tooltip.add(new TranslationTextComponent("item.mineria.jar.poison_source", hasPoison ? I18n.get(source.getTranslationKey()) : "no").withStyle(style -> hasPoison ? style.withColor(Color.fromRgb(source.getColor())) : style.withColor(TextFormatting.GRAY)));
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand)
    {
        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SPLASH_POTION_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));

        ItemStack jarStack = player.getItemInHand(hand);

        if (!world.isClientSide)
        {
            if(containsPoisonSource(jarStack))
            {
                JarEntity jarEntity = new JarEntity(player, world);
                jarEntity.setItem(jarStack);
                jarEntity.shootFromRotation(player, player.xRot, player.yRot, -20.0F, 0.5F, 1.0F);
                world.addFreshEntity(jarEntity);
            }
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.abilities.instabuild)
        {
            jarStack.shrink(1);
        }

        return ActionResult.sidedSuccess(jarStack, world.isClientSide());
    }

    @Override
    public int getColor(ItemStack stack)
    {
        CompoundNBT nbt = stack.getTagElement("display");
        return nbt != null && nbt.contains("color", 99) ? nbt.getInt("color") : 16777215;
    }

    public static PoisonSource getPoisonSourceFromStack(ItemStack stack)
    {
        CompoundNBT nbt = stack.getOrCreateTag();
        return nbt.contains("PoisonSource") ? PoisonSource.byName(ResourceLocation.tryParse(nbt.getString("PoisonSource"))) : PoisonSource.UNKNOWN;
    }

    public static boolean containsPoisonSource(ItemStack stack)
    {
        CompoundNBT nbt = stack.getOrCreateTag();
        return nbt.contains("PoisonSource");
    }

    public static boolean isLingering(ItemStack stack)
    {
        CompoundNBT nbt = stack.getOrCreateTag();
        return nbt.contains("Lingering") && nbt.getBoolean("Lingering");
    }

    public static ItemStack addPoisonSourceToStack(ItemStack stack, PoisonSource source)
    {
        CompoundNBT nbt = stack.getOrCreateTag();
        nbt.putString("PoisonSource", source.getId().toString());
        return stack;
    }
}
