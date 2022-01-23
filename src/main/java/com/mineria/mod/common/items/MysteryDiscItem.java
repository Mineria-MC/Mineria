package com.mineria.mod.common.items;

import com.mineria.mod.Mineria;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class MysteryDiscItem extends Item
{
    public MysteryDiscItem()
    {
        super(new Properties().tab(Mineria.MINERIA_GROUP).stacksTo(1).rarity(Rarity.RARE));
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand)
    {
        ItemStack stack = player.getItemInHand(hand);
        if(stack.getItem().equals(this))
        {
            ItemStack stackToAdd = new ItemStack(ItemTags.MUSIC_DISCS.getRandomElement(random));
            /*if(!player.addItem(stackToAdd))
                player.drop(stackToAdd, false);*/
            player.setItemInHand(hand, stackToAdd);

            return ActionResult.success(stackToAdd);
        }
        return super.use(world, player, hand);
    }
}
