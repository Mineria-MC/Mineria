package io.github.mineria_mc.mineria.data;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.data.OakLeavesBillhookModifier;
import io.github.mineria_mc.mineria.common.init.MineriaItems;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

public class MineriaGLMProvider extends GlobalLootModifierProvider {
    public MineriaGLMProvider(PackOutput output) {
        super(output, Mineria.MODID);
    }

    @Override
    protected void start() {
        add("oak_leaves_billhook", new OakLeavesBillhookModifier(new LootItemCondition[] {
                new MatchTool(ItemPredicate.Builder.item().of(MineriaItems.BILLHOOK.get()).build()),
                new LootItemBlockStatePropertyCondition.Builder(Blocks.OAK_LEAVES).build()
        }, 0.5f, new ItemStack(Items.APPLE)));
    }
}
