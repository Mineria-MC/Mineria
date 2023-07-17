package io.github.mineria_mc.mineria.common.blocks;

import io.github.mineria_mc.mineria.common.init.MineriaItems;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class LyciumChinenseBlock extends FruitPlantBlock {
    public LyciumChinenseBlock() {
        super(MineriaItems.GOJI, 5, true);
    }

    @Override
    public InteractionResult use(BlockState state, @NotNull Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (state.getValue(AGE) == 1) {
            popResource(worldIn, pos, new ItemStack(fruit.get()));
            worldIn.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + worldIn.random.nextFloat() * 0.4F);
            worldIn.setBlock(pos, Blocks.DEAD_BUSH.defaultBlockState(), 2);
            return InteractionResult.sidedSuccess(worldIn.isClientSide);
        }
        return InteractionResult.PASS;
    }
}
