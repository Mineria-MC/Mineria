package com.mineria.mod.common.items;

import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;


//@Mod.EventBusSubscriber(modid = Mineria.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MineriaSpawnEggItem extends SpawnEggItem
{
    protected static final List<MineriaSpawnEggItem> UNADDED_EGGS = new ArrayList<>();
    private final Lazy<? extends EntityType<?>> entityTypeSupplier;

    public MineriaSpawnEggItem(Supplier<? extends EntityType<? extends LivingEntity>> object, int primaryColorIn, int secondaryColorIn, Properties builder)
    {
        super(null, primaryColorIn, secondaryColorIn, builder);
        this.entityTypeSupplier = Lazy.of(object);
        UNADDED_EGGS.add(this);
    }

    @Override
    public EntityType<?> getType(CompoundTag nbt)
    {
        return this.entityTypeSupplier.get();
    }

    private static void initSpawnEggs()
    {
        final Map<EntityType<?>, SpawnEggItem> EGGS = /*ObfuscationReflectionHelper.getPrivateValue(SpawnEggItem.class, null, "field_195987_b")*/Map.of();
        DefaultDispenseItemBehavior dispenseBehaviour = new DefaultDispenseItemBehavior()
        {
            @Override
            protected ItemStack execute(BlockSource source, ItemStack stack)
            {
                Direction direction = source.getBlockState().getValue(DispenserBlock.FACING);
                EntityType<?> type = ((SpawnEggItem) stack.getItem()).getType(stack.getTag());
                type.spawn(source.getLevel(), stack, null, source.getPos().relative(direction), MobSpawnType.DISPENSER, direction != Direction.UP, false);
                stack.shrink(1);
                return stack;
            }
        };

        for(SpawnEggItem spawnEgg : UNADDED_EGGS)
        {
            EGGS.put(spawnEgg.getType(null), spawnEgg);
            DispenserBlock.registerBehavior(spawnEgg, dispenseBehaviour);
        }
    }

    @SubscribeEvent
    public static void registerEggs(RegistryEvent.Register<EntityType<?>> event)
    {
        initSpawnEggs();
    }
}
