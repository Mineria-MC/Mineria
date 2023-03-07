package io.github.mineria_mc.mineria.common.items;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITagManager;

import javax.annotation.Nonnull;
import java.util.Optional;

public class MysteryDiscItem extends Item {
    public MysteryDiscItem() {
        super(new Properties().stacksTo(1).rarity(Rarity.RARE));
    }

    @Nonnull
    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level world, Player player, @Nonnull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem().equals(this)) {
            ITagManager<Item> itemTags = ForgeRegistries.ITEMS.tags();
            if(itemTags != null) {
                Optional<Item> disc = itemTags.getTag(ItemTags.MUSIC_DISCS).getRandomElement(world.getRandom());
                if(disc.isPresent()) {
                    ItemStack stackToAdd = new ItemStack(disc.get());
                    player.setItemInHand(hand, stackToAdd);

                    return InteractionResultHolder.success(stackToAdd);
                }
            }
        }
        return super.use(world, player, hand);
    }
}
