package com.mineria.mod.common.recipe;

import com.mineria.mod.common.init.MineriaRecipeSerializers;
import net.minecraft.block.FlowerBlock;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SuspiciousStewItem;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.potion.Effect;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;

public class CustomSuspiciousStewRecipe extends SpecialRecipe
{
    public CustomSuspiciousStewRecipe(ResourceLocation location)
    {
        super(location);
    }

    @Override
    public boolean matches(CraftingInventory inv, World world)
    {
        ItemStack mushroom1 = ItemStack.EMPTY;
        ItemStack mushroom2 = ItemStack.EMPTY;
        boolean hasFlower = false;
        boolean hasBowl = false;

        for (int i = 0; i < inv.getContainerSize(); ++i)
        {
            ItemStack slotStack = inv.getItem(i);
            if (!slotStack.isEmpty())
            {
                if (slotStack.getItem().is(Tags.Items.MUSHROOMS) && mushroom1.isEmpty())
                {
                    mushroom1 = slotStack;
                } else if (slotStack.getItem().is(Tags.Items.MUSHROOMS) && !slotStack.sameItem(mushroom1) && mushroom2.isEmpty())
                {
                    mushroom2 = slotStack;
                } else if (slotStack.getItem().is(ItemTags.SMALL_FLOWERS) && !hasFlower)
                {
                    hasFlower = true;
                } else
                {
                    if (slotStack.getItem() != Items.BOWL || hasBowl)
                    {
                        return false;
                    }

                    hasBowl = true;
                }
            }
        }

        return hasFlower && !mushroom1.isEmpty() && !mushroom2.isEmpty() && hasBowl;
    }

    @Override
    public ItemStack assemble(CraftingInventory inv)
    {
        ItemStack flower = ItemStack.EMPTY;

        for (int i = 0; i < inv.getContainerSize(); ++i)
        {
            ItemStack slotStack = inv.getItem(i);
            if (!slotStack.isEmpty() && slotStack.getItem().is(ItemTags.SMALL_FLOWERS))
            {
                flower = slotStack;
                break;
            }
        }

        ItemStack stew = new ItemStack(Items.SUSPICIOUS_STEW, 1);
        if (flower.getItem() instanceof BlockItem && ((BlockItem) flower.getItem()).getBlock() instanceof FlowerBlock)
        {
            FlowerBlock flowerblock = (FlowerBlock) ((BlockItem) flower.getItem()).getBlock();
            Effect effect = flowerblock.getSuspiciousStewEffect();
            SuspiciousStewItem.saveMobEffect(stew, effect, flowerblock.getEffectDuration());
        }

        return stew;
    }

    @Override
    public boolean canCraftInDimensions(int gridWidth, int gridHeight)
    {
        return gridWidth >= 2 && gridHeight >= 2;
    }

    @Override
    public IRecipeSerializer<?> getSerializer()
    {
        return MineriaRecipeSerializers.CUSTOM_SUSPICIOUS_STEW.get();
    }
}
