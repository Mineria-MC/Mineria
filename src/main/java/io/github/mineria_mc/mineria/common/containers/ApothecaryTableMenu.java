package io.github.mineria_mc.mineria.common.containers;

import io.github.mineria_mc.mineria.common.blocks.apothecary_table.ApothecaryTableBlockEntity;
import io.github.mineria_mc.mineria.common.effects.util.PoisonSource;
import io.github.mineria_mc.mineria.common.init.MineriaMenuTypes;
import io.github.mineria_mc.mineria.common.init.MineriaCriteriaTriggers;
import io.github.mineria_mc.mineria.common.init.MineriaRecipeTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ApothecaryTableMenu extends MineriaMenu<ApothecaryTableBlockEntity> {
    private final ApothecaryTableBlockEntity apothecaryTable;
    private final ContainerData data;

    public ApothecaryTableMenu(int id, Inventory playerInv, ApothecaryTableBlockEntity tileEntity, ContainerData dataAccess) {
        super(MineriaMenuTypes.APOTHECARY_TABLE.get(), id, tileEntity);

        this.apothecaryTable = tileEntity;
        this.data = dataAccess;

        this.createPlayerInventorySlots(playerInv, 8, 84);
        this.addDataSlots(dataAccess);
    }

    public static ApothecaryTableMenu create(int id, Inventory playerInv, FriendlyByteBuf buffer) {
        return new ApothecaryTableMenu(id, playerInv, getTileEntity(ApothecaryTableBlockEntity.class, playerInv, buffer), new SimpleContainerData(3));
    }

    @Override
    protected void createInventorySlots(ApothecaryTableBlockEntity tile) {
        this.addSlot(new SlotItemHandler(tile.getInventory(), 0, 42, 35));
        this.addSlot(new SlotItemHandler(tile.getInventory(), 1, 84, 35));
        this.addSlot(new SlotItemHandler(tile.getInventory(), 2, 141, 35) {
            @Override
            public void onTake(@Nonnull Player player, @Nonnull ItemStack stack) {
                if (player instanceof ServerPlayer) {
                    MineriaCriteriaTriggers.USED_APOTHECARY_TABLE.trigger((ServerPlayer) player, stack);
                }

                super.onTake(player, stack);
            }
        });
    }

    @OnlyIn(Dist.CLIENT)
    public int getApplicationTime() {
        return data.get(0);
    }

    @OnlyIn(Dist.CLIENT)
    public int getTotalApplicationTime() {
        return tile.totalApplicationTime;
    }

    @Nullable
    @OnlyIn(Dist.CLIENT)
    public PoisonSource getPoisonSource() {
        return PoisonSource.byOrdinal(data.get(2));
    }

    @OnlyIn(Dist.CLIENT)
    public int getLiquidAmount() {
        return data.get(1);
    }

    public ApothecaryTableBlockEntity getTileEntity() {
        return this.apothecaryTable;
    }

    @Override
    protected StackTransferHandler getStackTransferHandler() {
        return new StackTransferHandler(2);
    }

    @Override
    protected int getIndexForStack(ItemStack stack) {
        if(findRecipeForStack(stack, MineriaRecipeTypes.APOTHECARY_TABLE_FILLING.get()) != null) {
            return 0;
        }
        if(findRecipeForStack(stack, MineriaRecipeTypes.APOTHECARY_TABLE.get()) != null) {
            return 1;
        }
        return -1;
    }

    @Nullable
    @Override
    protected RecipeType<?> getRecipeType() {
        return null;
    }
}
