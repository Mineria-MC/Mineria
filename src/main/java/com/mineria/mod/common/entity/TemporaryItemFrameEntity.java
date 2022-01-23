package com.mineria.mod.common.entity;

import com.mineria.mod.common.init.MineriaEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TemporaryItemFrameEntity extends ItemFrameEntity
{
    private boolean itemSet;

    public TemporaryItemFrameEntity(EntityType<? extends TemporaryItemFrameEntity> type, World world)
    {
        super(type, world);
    }

    @OnlyIn(Dist.CLIENT)
    public TemporaryItemFrameEntity(World world, BlockPos pos, Direction direction)
    {
        this(MineriaEntities.TEMPORARY_ITEM_FRAME.get(), world);
        this.pos = pos;
        this.setDirection(direction);
    }

    public TemporaryItemFrameEntity(World world, BlockPos pos, Direction direction, ItemStack item, boolean update)
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
        if(itemSet && this.getItem().isEmpty() && this.isAlive()) this.remove();
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT nbt)
    {
        super.addAdditionalSaveData(nbt);
        nbt.putBoolean("ItemSet", this.itemSet);
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT nbt)
    {
        super.readAdditionalSaveData(nbt);
        this.itemSet = nbt.getBoolean("ItemSet");
    }
}
