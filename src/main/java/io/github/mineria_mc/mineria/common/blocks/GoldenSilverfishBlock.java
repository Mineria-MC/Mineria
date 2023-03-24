package io.github.mineria_mc.mineria.common.blocks;

import com.google.common.collect.Maps;
import io.github.mineria_mc.mineria.common.entity.GoldenSilverfishEntity;
import io.github.mineria_mc.mineria.common.init.MineriaEntities;
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

@SuppressWarnings("deprecation")
public class GoldenSilverfishBlock extends Block {
    private final Block mimickedBlock;
    private static final Map<Block, Block> normalToInfectedMap = Maps.newIdentityHashMap();

    public GoldenSilverfishBlock(Block block) {
        super(BlockBehaviour.Properties.of(Material.CLAY).strength(0.0F, 0.75F));
        this.mimickedBlock = block;
        normalToInfectedMap.put(block, this);
    }

    public Block getMimickedBlock() {
        return this.mimickedBlock;
    }

    public static boolean canContainGoldenSilverfish(BlockState state) {
        return normalToInfectedMap.containsKey(state.getBlock());
    }

    private void spawnGoldenSilverfish(ServerLevel world, BlockPos pos) {
        GoldenSilverfishEntity goldenSilverfish = MineriaEntities.GOLDEN_SILVERFISH.get().create(world);
        goldenSilverfish.moveTo((double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, 0.0F, 0.0F);
        world.addFreshEntity(goldenSilverfish);
        goldenSilverfish.spawnAnim();
    }

    @Override
    public void spawnAfterBreak(BlockState state, ServerLevel worldIn, BlockPos pos, ItemStack stack, boolean dropExperience) {
        super.spawnAfterBreak(state, worldIn, pos, stack, dropExperience);
        if (worldIn.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS) && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, stack) == 0) {
            this.spawnGoldenSilverfish(worldIn, pos);
        }

    }

    @Override
    public void wasExploded(Level worldIn, BlockPos pos, Explosion explosionIn) {
        if (worldIn instanceof ServerLevel) {
            this.spawnGoldenSilverfish((ServerLevel) worldIn, pos);
        }
    }

    public static BlockState infest(Block blockIn) {
        return normalToInfectedMap.get(blockIn).defaultBlockState();
    }
}