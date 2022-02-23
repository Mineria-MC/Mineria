package com.mineria.mod.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;

import java.util.Random;

public class StrychnosPlantBlock extends VineBlock
{
    public static final IntegerProperty AGE = BlockStateProperties.AGE_2;

    public StrychnosPlantBlock()
    {
        super(BlockBehaviour.Properties.of(Material.REPLACEABLE_PLANT).noCollission().randomTicks().strength(0.2F).sound(SoundType.VINE));
        registerDefaultState(this.stateDefinition.any().setValue(UP, false).setValue(NORTH, false).setValue(EAST, false).setValue(SOUTH, false).setValue(WEST, false).setValue(AGE, 0));
    }

    @Override
    public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, Random random)
    {
        if (worldIn.random.nextInt(8) == 0 && worldIn.isAreaLoaded(pos, 4))
        {
            Direction direction = Direction.getRandom(random);
            BlockPos above = pos.above();

            if (direction.getAxis().isHorizontal() && !state.getValue(getPropertyForFace(direction)))
            {
                if (this.canSpread(worldIn, pos))
                {
                    BlockPos newPos = pos.relative(direction);
                    BlockState newState = worldIn.getBlockState(newPos);

                    if (newState.isAir())
                    {
                        Direction clockWise = direction.getClockWise();
                        Direction counterClockWise = direction.getCounterClockWise();
                        boolean canClockWise = state.getValue(getPropertyForFace(clockWise));
                        boolean canCClockWise = state.getValue(getPropertyForFace(counterClockWise));
                        BlockPos supportBlock = newPos.relative(clockWise);
                        BlockPos supportBlock1 = newPos.relative(counterClockWise);

                        if (canClockWise && isAcceptableNeighbour(worldIn, supportBlock, clockWise))
                        {
                            worldIn.setBlock(newPos, this.defaultBlockState().setValue(getPropertyForFace(clockWise), true), 2);
                        } else if (canCClockWise && isAcceptableNeighbour(worldIn, supportBlock1, counterClockWise))
                        {
                            worldIn.setBlock(newPos, this.defaultBlockState().setValue(getPropertyForFace(counterClockWise), true), 2);
                        } else
                        {
                            Direction opposite = direction.getOpposite();
                            if (canClockWise && worldIn.isEmptyBlock(supportBlock) && isAcceptableNeighbour(worldIn, pos.relative(clockWise), opposite))
                            {
                                worldIn.setBlock(supportBlock, this.defaultBlockState().setValue(getPropertyForFace(opposite), true), 2);
                            } else if (canCClockWise && worldIn.isEmptyBlock(supportBlock1) && isAcceptableNeighbour(worldIn, pos.relative(counterClockWise), opposite))
                            {
                                worldIn.setBlock(supportBlock1, this.defaultBlockState().setValue(getPropertyForFace(opposite), true), 2);
                            } else if ((double) worldIn.random.nextFloat() < 0.05D && isAcceptableNeighbour(worldIn, newPos.above(), Direction.UP))
                            {
                                worldIn.setBlock(newPos, this.defaultBlockState().setValue(UP, true), 2);
                            }
                        }
                    } else if (isAcceptableNeighbour(worldIn, newPos, direction))
                    {
                        worldIn.setBlock(pos, state.setValue(getPropertyForFace(direction), true), 2);
                    }
                }
            } else
            {
                if (direction == Direction.UP && pos.getY() < 255)
                {
                    if (this.canSupportAtFace(worldIn, pos, direction))
                    {
                        worldIn.setBlock(pos, state.setValue(UP, true), 2);
                        return;
                    }

                    if (worldIn.isEmptyBlock(above))
                    {
                        if (!this.canSpread(worldIn, pos))
                        {
                            return;
                        }

                        BlockState updatedState = state;

                        for (Direction horizontalDir : Direction.Plane.HORIZONTAL)
                        {
                            if (random.nextBoolean() || !isAcceptableNeighbour(worldIn, above.relative(horizontalDir), Direction.UP))
                            {
                                updatedState = updatedState.setValue(getPropertyForFace(horizontalDir), false);
                            }
                        }

                        if (this.hasHorizontalConnection(updatedState))
                        {
                            worldIn.setBlock(above, updatedState, 2);
                        }

                        return;
                    }
                }

                if (pos.getY() > 0)
                {
                    BlockPos below = pos.below();
                    BlockState belowState = worldIn.getBlockState(below);
                    boolean air = belowState.isAir();

                    if (air || belowState.is(this))
                    {
                        BlockState stateToCopy = air ? this.defaultBlockState() : belowState;
                        BlockState copied = this.copyRandomFaces(state, stateToCopy, random);
                        if (stateToCopy != copied && this.hasHorizontalConnection(copied))
                        {
                            worldIn.setBlock(below, copied, 2);
                        }
                    }
                }
            }
        }
        
        if(worldIn.random.nextInt(8) == 0 && worldIn.isAreaLoaded(pos, 4))
        {
            int age = state.getValue(AGE);
            if(age < 2)
                worldIn.setBlock(pos, state.setValue(AGE, age + 1), 2);
        }
    }

    private BlockState copyRandomFaces(BlockState state, BlockState state1, Random random)
    {
        for (Direction direction : Direction.Plane.HORIZONTAL)
        {
            if (random.nextBoolean())
            {
                BooleanProperty booleanproperty = getPropertyForFace(direction);
                if (state.getValue(booleanproperty))
                {
                    state1 = state1.setValue(booleanproperty, true);
                }
            }
        }

        return state1;
    }

    private boolean hasHorizontalConnection(BlockState state)
    {
        return state.getValue(NORTH) || state.getValue(EAST) || state.getValue(SOUTH) || state.getValue(WEST);
    }

    private boolean canSpread(BlockGetter reader, BlockPos pos)
    {
        Iterable<BlockPos> iterable = BlockPos.betweenClosed(pos.getX() - 4, pos.getY() - 1, pos.getZ() - 4, pos.getX() + 4, pos.getY() + 1, pos.getZ() + 4);
        int j = 5;

        for (BlockPos blockpos : iterable)
        {
            if (reader.getBlockState(blockpos).is(this))
            {
                --j;
                if (j <= 0)
                {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean canSupportAtFace(BlockGetter world, BlockPos pos, Direction dir)
    {
        if (dir == Direction.DOWN)
        {
            return false;
        } else
        {
            BlockPos blockpos = pos.relative(dir);
            if (isAcceptableNeighbour(world, blockpos, dir))
            {
                return true;
            } else if (dir.getAxis() == Direction.Axis.Y)
            {
                return false;
            } else
            {
                BooleanProperty booleanproperty = PROPERTY_BY_DIRECTION.get(dir);
                BlockState blockstate = world.getBlockState(pos.above());
                return blockstate.is(this) && blockstate.getValue(booleanproperty);
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder.add(AGE));
    }
}
