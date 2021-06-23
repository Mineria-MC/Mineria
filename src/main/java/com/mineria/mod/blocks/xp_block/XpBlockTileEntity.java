package com.mineria.mod.blocks.xp_block;

import com.mineria.mod.init.ItemsInit;
import com.mineria.mod.init.TileEntitiesInit;
import com.mineria.mod.util.CustomItemStackHandler;
import com.mineria.mod.util.MineriaLockableTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class XpBlockTileEntity extends MineriaLockableTileEntity
{
    public XpBlockTileEntity()
    {
        super(TileEntitiesInit.XP_BLOCK.get(), new CustomItemStackHandler(1));
    }

    @Override
    protected ITextComponent getDefaultName()
    {
        return new TranslationTextComponent("tile_entity.mineria.xp_block");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player)
    {
        return new XpBlockContainer(id, player, this);
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 16;
    }

    private void spawnXp(PlayerEntity player)
    {
        ItemStack stack = new ItemStack(ItemsInit.XP_ORB);
        ItemStack stack1 = this.inventory.getStackInSlot(0);
        if (stack1.getCount() < this.getInventoryStackLimit())
        {
            if (player.abilities.isCreativeMode)
            {
                if (stack1.isEmpty())
                {
                    this.inventory.setStackInSlot(0, stack.copy());
                } else if (stack1.isItemEqual(stack))
                {
                    stack1.grow(1);
                }
            } else if (player.experienceTotal > 0)
            {
                player.giveExperiencePoints(-1);
                if (stack1.isEmpty())
                {
                    this.inventory.setStackInSlot(0, stack.copy());
                } else if (stack1.isItemEqual(stack))
                {
                    stack1.grow(1);
                }
            }
        }
    }

    public static void executeProcedure(BlockPos pos, World world, PlayerEntity player)
    {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof XpBlockTileEntity)
            ((XpBlockTileEntity) tileEntity).spawnXp(player);
    }
}