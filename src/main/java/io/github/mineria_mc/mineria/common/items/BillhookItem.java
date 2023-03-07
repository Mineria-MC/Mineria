package io.github.mineria_mc.mineria.common.items;

import io.github.mineria_mc.mineria.common.init.MineriaBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

public class BillhookItem extends Item {
    public BillhookItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean mineBlock(@Nonnull ItemStack stack, Level worldIn, @Nonnull BlockState state, @Nonnull BlockPos pos, @Nonnull LivingEntity entityLiving) {
        if (!worldIn.isClientSide) {
            if (state.is(MineriaBlocks.Tags.MINEABLE_WITH_BILLHOOK)) {
                stack.hurtAndBreak(1, entityLiving, (entity) -> entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
            }
        }
        return super.mineBlock(stack, worldIn, state, pos, entityLiving);
    }
}
