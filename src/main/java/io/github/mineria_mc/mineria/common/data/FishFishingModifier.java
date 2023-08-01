package io.github.mineria_mc.mineria.common.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.mineria_mc.mineria.common.init.MineriaLootModifierSerializers;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Map;

public class FishFishingModifier implements IGlobalLootModifier {
    public static final Codec<FishFishingModifier> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(Codec.unboundedMap(ItemStack.CODEC, Codec.INT).fieldOf("chances").forGetter(m -> m.chances))
            .apply(instance, FishFishingModifier::new));

    private final Map<ItemStack, Integer> chances;

    public FishFishingModifier(Map<ItemStack, Integer> chances) {
        this.chances = chances;
    }

    @Override
    public @NotNull ObjectArrayList<ItemStack> apply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        if(context.getQueriedLootTableId() != BuiltInLootTables.FISHING_FISH) {
            return generatedLoot;
        }

        int sumOfChances = chances.values().stream().reduce(Integer::sum).orElse(0);
        RandomSource rand = context.getRandom();
        int chance = rand.nextInt(100 + sumOfChances);
        if(chance < 100) {
            return generatedLoot;
        }
        ObjectArrayList<ItemStack> res = new ObjectArrayList<>();
        int k = chance - 100;
        Iterator<Map.Entry<ItemStack, Integer>> it = chances.entrySet().stream().sorted(Map.Entry.comparingByValue(Integer::compareTo)).iterator();
        while (it.hasNext()) {
            Map.Entry<ItemStack, Integer> entry = it.next();
            if((k -= entry.getValue()) < 0) {
                res.add(entry.getKey().copy());
            }
        }
        return res;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return MineriaLootModifierSerializers.FISH_FISHING.get();
    }
}
