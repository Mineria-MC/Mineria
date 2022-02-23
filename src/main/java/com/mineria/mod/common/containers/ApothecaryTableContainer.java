package com.mineria.mod.common.containers;

import com.mineria.mod.common.blocks.apothecary_table.ApothecaryTableTileEntity;
import com.mineria.mod.common.effects.PoisonSource;
import com.mineria.mod.common.init.MineriaContainerTypes;
import com.mineria.mod.common.init.MineriaCriteriaTriggers;
import com.mineria.mod.common.init.MineriaRecipeSerializers;
import com.mineria.mod.common.recipe.AbstractApothecaryTableRecipe;
import com.mineria.mod.util.FunctionalIntReferenceHolder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nullable;

public class ApothecaryTableContainer extends MineriaContainer<ApothecaryTableTileEntity>
{
    private final ApothecaryTableTileEntity apothecaryTable;
    private final FunctionalIntReferenceHolder applicationTime;
    private final FunctionalIntReferenceHolder liquidAmount;
    private final FunctionalIntReferenceHolder poisonSource;
//    private IIntArray data; TODOLTR May fix this later

    public ApothecaryTableContainer(int id, Inventory playerInv, ApothecaryTableTileEntity tileEntity)
    {
        super(MineriaContainerTypes.APOTHECARY_TABLE.get(), id, tileEntity);

        this.apothecaryTable = tileEntity;

        this.createPlayerInventorySlots(playerInv, 8, 84);
        this.addDataSlot(applicationTime = new FunctionalIntReferenceHolder(() -> tileEntity.applicationTime, value -> tileEntity.applicationTime = value));
        this.addDataSlot(liquidAmount = new FunctionalIntReferenceHolder(tileEntity::getLiquidAmount, tileEntity::setLiquidAmount));
        this.addDataSlot(poisonSource = new FunctionalIntReferenceHolder(tileEntity::getPoisonSourceOrdinal, tileEntity::setPoisonSourceFromOrdinal));
    }

    /*public ApothecaryTableContainer(int id, PlayerInventory playerInv, ApothecaryTableTileEntity tileEntity, IIntArray data)
    {
        super(MineriaContainerTypes.APOTHECARY_TABLE.get(), id, tileEntity);

        this.apothecaryTable = tileEntity;
        this.data = data;

        this.createPlayerInventorySlots(playerInv, 8, 84);
        this.addDataSlots(data);
    }*/

    public static ApothecaryTableContainer create(int id, Inventory playerInv, FriendlyByteBuf buffer)
    {
        return new ApothecaryTableContainer(id, playerInv, getTileEntity(ApothecaryTableTileEntity.class, playerInv, buffer)/*, new IntArray(3)*/);
    }

    @Override
    protected void createInventorySlots(ApothecaryTableTileEntity tile)
    {
        this.addSlot(new SlotItemHandler(tile.getInventory(), 0, 42, 35));
        this.addSlot(new SlotItemHandler(tile.getInventory(), 1, 84, 35));
        this.addSlot(new SlotItemHandler(tile.getInventory(), 2, 141, 35) {
            @Override
            public void onTake(Player player, ItemStack stack)
            {
                if(player instanceof ServerPlayer)
                {
                    MineriaCriteriaTriggers.USED_APOTHECARY_TABLE.trigger((ServerPlayer) player, stack);
                }

                super.onTake(player, stack);
            }
        });
    }

    @OnlyIn(Dist.CLIENT)
    public int getApplicationTime()
    {
        return applicationTime.get()/*data.get(0)*/;
    }

    @OnlyIn(Dist.CLIENT)
    public int getTotalApplicationTime()
    {
        return tile.totalApplicationTime;
    }

    @Nullable
    @OnlyIn(Dist.CLIENT)
    public PoisonSource getPoisonSource()
    {
        return PoisonSource.byOrdinal(poisonSource.get()/*data.get(2)*/);
    }

    @OnlyIn(Dist.CLIENT)
    public int getLiquidAmount()
    {
        return liquidAmount.get()/*data.get(1)*/;
    }

    public ApothecaryTableTileEntity getTileEntity()
    {
        return this.apothecaryTable;
    }

    @Override
    protected StackTransferHandler getStackTransferHandler()
    {
        return new StackTransferHandler(2);
    }

    @Override
    protected int getIndexForRecipe(ItemStack stack)
    {
        return getIndexForRecipe(stack, MineriaRecipeSerializers.APOTHECARY_TABLE_TYPE, AbstractApothecaryTableRecipe.class) == -1 ? -1 : 1;
    }
}
