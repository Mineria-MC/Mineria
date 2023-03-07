package io.github.mineria_mc.mineria.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;

import java.util.function.Supplier;

public class FruitPlantBlock extends PlantBlock implements BonemealableBlock {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_1;
    protected final Supplier<Item> fruit;
    private final int chance;

    public FruitPlantBlock(Supplier<Item> fruit, int chance, boolean isBushBlock) {
        super(BlockBehaviour.Properties.of(Material.PLANT).noCollission().randomTicks().strength(isBushBlock ? 0.5F : 0).sound(SoundType.GRASS).isSuffocating((a, b, c) -> false).isViewBlocking((a, b, c) -> false), isBushBlock);
        this.fruit = fruit;
        this.chance = chance;
        registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (state.getValue(AGE) == 1) {
            popResource(worldIn, pos, new ItemStack(fruit.get()));
            worldIn.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + worldIn.random.nextFloat() * 0.4F);
            worldIn.setBlock(pos, state.setValue(AGE, 0), 2);
            return InteractionResult.sidedSuccess(worldIn.isClientSide);
        }
        return InteractionResult.PASS;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource rand) {
        if (world.random.nextInt(this.chance) == 0 && world.isAreaLoaded(pos, 4)) {
            int age = state.getValue(AGE);
            if (age == 0)
                world.setBlock(pos, state.setValue(AGE, 1), 2);
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader worldIn, BlockPos pos, BlockState state, boolean isClient) {
        return state.getValue(AGE) < 1;
    }

    @Override
    public boolean isBonemealSuccess(Level worldIn, RandomSource rand, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel worldIn, RandomSource rand, BlockPos pos, BlockState state) {
        worldIn.setBlock(pos, state.setValue(AGE, 1), 2);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(AGE);
    }
}
