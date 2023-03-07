package io.github.mineria_mc.mineria.common.recipe;

import io.github.mineria_mc.mineria.common.init.MineriaRecipeSerializers;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SuspiciousStewItem;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.Tags;

import javax.annotation.Nonnull;

public class CustomSuspiciousStewRecipe extends CustomRecipe {
    public CustomSuspiciousStewRecipe(ResourceLocation location, CraftingBookCategory category) {
        super(location, category);
    }

    @Override
    public boolean matches(CraftingContainer inv, Level world) {
        ItemStack mushroom1 = ItemStack.EMPTY;
        ItemStack mushroom2 = ItemStack.EMPTY;
        boolean hasFlower = false;
        boolean hasBowl = false;

        for (int i = 0; i < inv.getContainerSize(); ++i) {
            ItemStack slotStack = inv.getItem(i);
            if (!slotStack.isEmpty()) {
                if (slotStack.is(Tags.Items.MUSHROOMS) && mushroom1.isEmpty()) {
                    mushroom1 = slotStack;
                } else if (slotStack.is(Tags.Items.MUSHROOMS) && !slotStack.sameItem(mushroom1) && mushroom2.isEmpty()) {
                    mushroom2 = slotStack;
                } else if (slotStack.is(ItemTags.SMALL_FLOWERS) && !hasFlower) {
                    hasFlower = true;
                } else {
                    if (slotStack.getItem() != Items.BOWL || hasBowl) {
                        return false;
                    }

                    hasBowl = true;
                }
            }
        }

        return hasFlower && !mushroom1.isEmpty() && !mushroom2.isEmpty() && hasBowl;
    }

    @Override
    public ItemStack assemble(CraftingContainer inv) {
        ItemStack flower = ItemStack.EMPTY;

        for (int i = 0; i < inv.getContainerSize(); ++i) {
            ItemStack slotStack = inv.getItem(i);
            if (!slotStack.isEmpty() && slotStack.is(ItemTags.SMALL_FLOWERS)) {
                flower = slotStack;
                break;
            }
        }

        ItemStack stew = new ItemStack(Items.SUSPICIOUS_STEW, 1);
        if (flower.getItem() instanceof BlockItem && ((BlockItem) flower.getItem()).getBlock() instanceof FlowerBlock flowerblock) {
            MobEffect effect = flowerblock.getSuspiciousEffect();
            SuspiciousStewItem.saveMobEffect(stew, effect, flowerblock.getEffectDuration());
        }

        return stew;
    }

    @Override
    public boolean canCraftInDimensions(int gridWidth, int gridHeight) {
        return gridWidth >= 2 && gridHeight >= 2;
    }

    @Nonnull
    @Override
    public RecipeSerializer<?> getSerializer() {
        return MineriaRecipeSerializers.CUSTOM_SUSPICIOUS_STEW.get();
    }
}
