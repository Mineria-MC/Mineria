package com.mineria.mod.common.items;

import com.mineria.mod.Mineria;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;

import net.minecraft.world.item.Item.Properties;

public class MysteryDiscItem extends Item
{
    public MysteryDiscItem()
    {
        super(new Properties().tab(Mineria.MINERIA_GROUP).stacksTo(1).rarity(Rarity.RARE));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand)
    {
        ItemStack stack = player.getItemInHand(hand);
        if(stack.getItem().equals(this))
        {
            ItemStack stackToAdd = new ItemStack(ItemTags.MUSIC_DISCS.getRandomElement(world.getRandom()));
            /*if(!player.addItem(stackToAdd))
                player.drop(stackToAdd, false);*/
            player.setItemInHand(hand, stackToAdd);

            return InteractionResultHolder.success(stackToAdd);
        }
        return super.use(world, player, hand);
    }
}
