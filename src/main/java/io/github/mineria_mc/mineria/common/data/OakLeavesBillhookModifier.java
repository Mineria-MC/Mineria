package io.github.mineria_mc.mineria.common.data;

import io.github.mineria_mc.mineria.common.init.MineriaLootModifierSerializers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

public class OakLeavesBillhookModifier extends LootModifier {
    public static final Codec<OakLeavesBillhookModifier> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
                codecStart(instance).t1(),
                Codec.FLOAT.fieldOf("chance").forGetter(m -> m.chance),
                ItemStack.CODEC.fieldOf("drops").forGetter(m -> m.drops)
        ).apply(instance, OakLeavesBillhookModifier::new)
    );

    private final float chance;
    private final ItemStack drops;

    public OakLeavesBillhookModifier(LootItemCondition[] conditionsIn, float chance, ItemStack drops) {
        super(conditionsIn);
        this.chance = chance;
        this.drops = drops;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        RandomSource rand = context.getRandom();

        if (rand.nextFloat() < chance) {
            generatedLoot.add(drops);
        }

        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return MineriaLootModifierSerializers.OAK_LEAVES_BILLHOOK.get();
    }
}
