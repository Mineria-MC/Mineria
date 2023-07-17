package io.github.mineria_mc.mineria.data;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MineriaLootTableProvider extends LootTableProvider {
    public MineriaLootTableProvider(PackOutput output) {
        super(output, Set.of(), List.of(new SubProviderEntry(MineriaBlockLoot::new, LootContextParamSets.BLOCK)));
    }

    @Override
    protected void validate(@Nonnull Map<ResourceLocation, LootTable> map, @Nonnull ValidationContext ctx) {
    }
}
