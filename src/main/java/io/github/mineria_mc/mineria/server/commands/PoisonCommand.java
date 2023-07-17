package io.github.mineria_mc.mineria.server.commands;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import io.github.mineria_mc.mineria.common.capabilities.MineriaCapabilities;
import io.github.mineria_mc.mineria.common.capabilities.TickingDataTypes;
import io.github.mineria_mc.mineria.common.effects.instances.PoisonMobEffectInstance;
import io.github.mineria_mc.mineria.common.effects.instances.PoisoningHiddenEffectInstance;
import io.github.mineria_mc.mineria.common.effects.util.PoisonSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PoisonCommand {
    private static final SimpleCommandExceptionType ERROR_APPLY_POISON_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.mineria.poison.apply.failed"));
    private static final SimpleCommandExceptionType ERROR_CREATE_POISON_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.mineria.poison.create.failed"));
    private static final SimpleCommandExceptionType ERROR_REMOVE_POISON_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.mineria.poison.remove.failed"));

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("poison").requires(source -> source.hasPermission(2))
                .then(Commands.literal("remove").executes(ctx -> removePoison(ctx.getSource(), ImmutableList.of(ctx.getSource().getPlayerOrException())))
                        .then(Commands.argument("targets", EntityArgument.entities()).executes(ctx -> removePoison(ctx.getSource(), EntityArgument.getEntities(ctx, "targets"))))
                )
                .then(Commands.literal("apply")
                        .then(Commands.argument("targets", EntityArgument.entities())
                                .then(Commands.argument("poisonSource", ResourceLocationArgument.id())
                                        .executes(ctx -> applyPoison(
                                                ctx.getSource(),
                                                EntityArgument.getEntities(ctx, "targets"),
                                                PoisonSource.byName(ResourceLocationArgument.getId(ctx, "poisonSource"))
                                        ))
                                )
                        )
                )
                .then(Commands.literal("create")
                        .then(Commands.argument("targets", EntityArgument.entities())
                                .then(Commands.argument("poisonSource", ResourceLocationArgument.id())
                                        .executes(ctx -> createPoison(
                                                ctx.getSource(),
                                                EntityArgument.getEntities(ctx, "targets"),
                                                PoisonSource.byName(ResourceLocationArgument.getId(ctx, "poisonSource")),
                                                0,
                                                30,
                                                0,
                                                true
                                        )).then(Commands.argument("potionClass", IntegerArgumentType.integer(0, 3))
                                                .executes(ctx -> createPoison(
                                                        ctx.getSource(),
                                                        EntityArgument.getEntities(ctx, "targets"),
                                                        PoisonSource.byName(ResourceLocationArgument.getId(ctx, "poisonSource")),
                                                        IntegerArgumentType.getInteger(ctx, "potionClass"),
                                                        30,
                                                        0,
                                                        true
                                                )).then(Commands.argument("duration", IntegerArgumentType.integer(1, 1000000))
                                                        .executes(ctx -> createPoison(
                                                                ctx.getSource(),
                                                                EntityArgument.getEntities(ctx, "targets"),
                                                                PoisonSource.byName(ResourceLocationArgument.getId(ctx, "poisonSource")),
                                                                IntegerArgumentType.getInteger(ctx, "potionClass"),
                                                                IntegerArgumentType.getInteger(ctx, "duration"),
                                                                0,
                                                                true
                                                        )).then(Commands.argument("amplifier", IntegerArgumentType.integer(0, 255))
                                                                .executes(ctx -> createPoison(
                                                                        ctx.getSource(),
                                                                        EntityArgument.getEntities(ctx, "targets"),
                                                                        PoisonSource.byName(ResourceLocationArgument.getId(ctx, "poisonSource")),
                                                                        IntegerArgumentType.getInteger(ctx, "potionClass"),
                                                                        IntegerArgumentType.getInteger(ctx, "duration"),
                                                                        IntegerArgumentType.getInteger(ctx, "amplifier"),
                                                                        true
                                                                )).then(Commands.argument("countPoisoning", BoolArgumentType.bool())
                                                                        .executes(ctx -> createPoison(ctx.getSource(),
                                                                                EntityArgument.getEntities(ctx, "targets"),
                                                                                PoisonSource.byName(ResourceLocationArgument.getId(ctx, "poisonSource")),
                                                                                IntegerArgumentType.getInteger(ctx, "potionClass"),
                                                                                IntegerArgumentType.getInteger(ctx, "duration"),
                                                                                IntegerArgumentType.getInteger(ctx, "amplifier"),
                                                                                BoolArgumentType.getBool(ctx, "countPoisoning"))
                                                                        ))
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
    }

    private static int applyPoison(CommandSourceStack executor, Collection<? extends Entity> targets, PoisonSource source) throws CommandSyntaxException {
        AtomicInteger ret = new AtomicInteger();

        for (Entity target : targets) {
            if (target instanceof LivingEntity living) {
                if(source.applyPoisoning(living)) {
                    ret.incrementAndGet();
                }
            }
        }

        int code = ret.get();
        if (code == 0) {
            throw ERROR_APPLY_POISON_FAILED.create();
        } else {
            if (targets.size() == 1) {
                executor.sendSuccess(() ->  Component.translatable("commands.mineria.poison.apply.success.single", Component.translatable(source.getTranslationKey()), targets.iterator().next().getDisplayName()), true);
            }
            else {
                executor.sendSuccess(() -> Component.translatable("commands.mineria.poison.apply.success.multiple", Component.translatable(source.getTranslationKey()), targets.size()), true);
            }

            return code;
        }
    }

    private static int createPoison(CommandSourceStack executor, Collection<? extends Entity> targets, PoisonSource source, int potionClass, int duration, int amplifier, boolean countPoisoning) throws CommandSyntaxException {
        AtomicInteger ret = new AtomicInteger();

        for (Entity target : targets) {
            if (target instanceof LivingEntity living) {
                PoisonMobEffectInstance.applyPoisonEffect(living, potionClass, duration * 20, amplifier, source);
                if(countPoisoning) {
                    living.getCapability(MineriaCapabilities.TICKING_DATA).ifPresent(cap -> cap.store(TickingDataTypes.POISON_EXPOSURE, source));
                }
                ret.incrementAndGet();
            }
        }

        int code = ret.get();
        if (code == 0) {
            throw ERROR_CREATE_POISON_FAILED.create();
        } else {
            if (targets.size() == 1) {
                executor.sendSuccess(() -> Component.translatable("commands.mineria.poison.create.success.single", Component.translatable(source.getTranslationKey()), targets.iterator().next().getDisplayName(), duration), true);
            }
            else {
                executor.sendSuccess(() -> Component.translatable("commands.mineria.poison.create.success.multiple", Component.translatable(source.getTranslationKey()), targets.size(), duration), true);
            }

            return code;
        }
    }

    private static int removePoison(CommandSourceStack executor, Collection<? extends Entity> targets) throws CommandSyntaxException {
        AtomicInteger ret = new AtomicInteger();

        for (Entity target : targets) {
            if (target instanceof LivingEntity living) {
                living.getCapability(MineriaCapabilities.TICKING_DATA).ifPresent(cap -> {
                    if ((living.hasEffect(MobEffects.POISON) && living.getEffect(MobEffects.POISON) instanceof PoisonMobEffectInstance) || living.getActiveEffects().stream().anyMatch(PoisoningHiddenEffectInstance.class::isInstance)) {
                        if (living.removeEffect(MobEffects.POISON)) {
                            ret.getAndIncrement();
                        }
                        List<MobEffect> toRemove = living.getActiveEffects().stream().filter(PoisoningHiddenEffectInstance.class::isInstance).map(MobEffectInstance::getEffect).toList();
                        for (MobEffect mobEffect : toRemove) {
                            if(living.removeEffect(mobEffect)) {
                                ret.getAndIncrement();
                            }
                        }
                    }
                });
            }
        }

        int code = ret.get();
        if (code == 0) {
            throw ERROR_REMOVE_POISON_FAILED.create();
        } else {
            if (targets.size() == 1)
                executor.sendSuccess(() -> Component.translatable("commands.mineria.poison.remove.success.single", targets.iterator().next().getDisplayName()), true);
            else
                executor.sendSuccess(() -> Component.translatable("commands.mineria.poison.remove.success.multiple", targets.size()), true);

            return code;
        }
    }
}
