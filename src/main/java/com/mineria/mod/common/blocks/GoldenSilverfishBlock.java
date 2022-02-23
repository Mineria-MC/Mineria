package com.mineria.mod.common.blocks;

import com.google.common.collect.Maps;
import com.mineria.mod.common.entity.GoldenSilverfishEntity;
import com.mineria.mod.common.init.MineriaEntities;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

import java.util.Map;

public class GoldenSilverfishBlock extends Block
{
    private final Block mimickedBlock;
    private static final Map<Block, Block> normalToInfectedMap = Maps.newIdentityHashMap();

    public GoldenSilverfishBlock(Block blockIn)
    {
        super(BlockBehaviour.Properties.of(Material.CLAY).strength(0.0F, 0.75F));
        this.mimickedBlock = blockIn;
        normalToInfectedMap.put(blockIn, this);
    }

    public Block getMimickedBlock()
    {
        return this.mimickedBlock;
    }

    public static boolean canContainGoldenSilverfish(BlockState state)
    {
        return normalToInfectedMap.containsKey(state.getBlock());
    }

    private void spawnGoldenSilverfish(ServerLevel world, BlockPos pos)
    {
        GoldenSilverfishEntity goldenSilverfish = MineriaEntities.GOLDEN_SILVERFISH.get().create(world);
        goldenSilverfish.moveTo((double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, 0.0F, 0.0F);
        world.addFreshEntity(goldenSilverfish);
        goldenSilverfish.spawnAnim();
    }

    public void spawnAfterBreak(BlockState state, ServerLevel worldIn, BlockPos pos, ItemStack stack)
    {
        super.spawnAfterBreak(state, worldIn, pos, stack);
        if (worldIn.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS) && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, stack) == 0)
        {
            this.spawnGoldenSilverfish(worldIn, pos);
        }

    }

    public void wasExploded(Level worldIn, BlockPos pos, Explosion explosionIn)
    {
        if (worldIn instanceof ServerLevel)
        {
            this.spawnGoldenSilverfish((ServerLevel)worldIn, pos);
        }
    }

    public static BlockState infest(Block blockIn)
    {
        return normalToInfectedMap.get(blockIn).defaultBlockState();
    }
}