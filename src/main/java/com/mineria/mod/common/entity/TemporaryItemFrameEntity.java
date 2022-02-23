package com.mineria.mod.common.entity;

import com.mineria.mod.common.init.MineriaEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TemporaryItemFrameEntity extends ItemFrame
{
    private boolean itemSet;

    public TemporaryItemFrameEntity(EntityType<? extends TemporaryItemFrameEntity> type, Level world)
    {
        super(type, world);
    }

    @OnlyIn(Dist.CLIENT)
    public TemporaryItemFrameEntity(Level world, BlockPos pos, Direction direction)
    {
        this(MineriaEntities.TEMPORARY_ITEM_FRAME.get(), world);
        this.pos = pos;
        this.setDirection(direction);
    }

    public TemporaryItemFrameEntity(Level world, BlockPos pos, Direction direction, ItemStack item, boolean update)
    {
        this(MineriaEntities.TEMPORARY_ITEM_FRAME.get(), world);
        this.pos = pos;
        this.setDirection(direction);
        setItem(item, update);
    }

    @Override
    public void setItem(ItemStack item, boolean update)
    {
        super.setItem(item, update);
        this.itemSet = true;
    }

    @Override
    public void tick()
    {
        super.tick();
        if(itemSet && this.getItem().isEmpty() && this.isAlive()) this.discard();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt)
    {
        super.addAdditionalSaveData(nbt);
        nbt.putBoolean("ItemSet", this.itemSet);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt)
    {
        super.readAdditionalSaveData(nbt);
        this.itemSet = nbt.getBoolean("ItemSet");
    }
}
