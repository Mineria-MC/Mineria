package com.mineria.mod.common.blocks;

import com.google.common.collect.Maps;
import com.mineria.mod.common.entity.GoldenSilverfishEntity;
import com.mineria.mod.common.init.MineriaEntities;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Map;

public class GoldenSilverfishBlock extends Block
{
    private final Block mimickedBlock;
    private static final Map<Block, Block> normalToInfectedMap = Maps.newIdentityHashMap();

    public GoldenSilverfishBlock(Block blockIn)
    {
        super(AbstractBlock.Properties.of(Material.CLAY).strength(0.0F, 0.75F));
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

    private void spawnGoldenSilverfish(ServerWorld world, BlockPos pos)
    {
        GoldenSilverfishEntity goldenSilverfish = MineriaEntities.GOLDEN_SILVERFISH.get().create(world);
        goldenSilverfish.moveTo((double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, 0.0F, 0.0F);
        world.addFreshEntity(goldenSilverfish);
        goldenSilverfish.spawnAnim();
    }

    public void spawnAfterBreak(BlockState state, ServerWorld worldIn, BlockPos pos, ItemStack stack)
    {
        super.spawnAfterBreak(state, worldIn, pos, stack);
        if (worldIn.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS) && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, stack) == 0)
        {
            this.spawnGoldenSilverfish(worldIn, pos);
        }

    }

    public void wasExploded(World worldIn, BlockPos pos, Explosion explosionIn)
    {
        if (worldIn instanceof ServerWorld)
        {
            this.spawnGoldenSilverfish((ServerWorld)worldIn, pos);
        }

    }

    public static BlockState infest(Block blockIn)
    {
        return normalToInfectedMap.get(blockIn).defaultBlockState();
    }
}