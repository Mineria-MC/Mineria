package io.github.mineria_mc.mineria.common.init;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.world.feature.decorators.LeavePlantTreeDecorator;
import io.github.mineria_mc.mineria.common.world.feature.decorators.TrunkPlantTreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MineriaTreeDecoratorTypes {
    public static final DeferredRegister<TreeDecoratorType<?>> TREE_DECORATORS = DeferredRegister.create(ForgeRegistries.TREE_DECORATOR_TYPES, Mineria.MODID);

    public static final RegistryObject<TreeDecoratorType<?>> TRUNK_PLANT = TREE_DECORATORS.register("trunk_plant", () -> new TreeDecoratorType<>(TrunkPlantTreeDecorator.CODEC));
    public static final RegistryObject<TreeDecoratorType<?>> LEAVE_PLANT = TREE_DECORATORS.register("leave_plant", () -> new TreeDecoratorType<>(LeavePlantTreeDecorator.CODEC));
}
