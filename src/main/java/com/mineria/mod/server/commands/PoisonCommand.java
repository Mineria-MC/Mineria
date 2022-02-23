package com.mineria.mod.server.commands;

import com.google.common.collect.ImmutableList;
import com.mineria.mod.common.capabilities.CapabilityRegistry;
import com.mineria.mod.common.effects.PoisonSource;
import com.mineria.mod.common.effects.instances.PoisonEffectInstance;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

public class PoisonCommand
{
    private static final SimpleCommandExceptionType ERROR_REMOVE_POISON_FAILED = new SimpleCommandExceptionType(new TranslatableComponent("commands.mineria.poison.remove.failed"));
    private static final SimpleCommandExceptionType ERROR_APPLY_POISON_FAILED = new SimpleCommandExceptionType(new TranslatableComponent("commands.mineria.poison.apply.failed"));

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("poison").requires(source -> source.hasPermission(2))
                .then(Commands.literal("remove").executes(ctx -> removePoison(ctx.getSource(), ImmutableList.of(ctx.getSource().getPlayerOrException())))
                        .then(Commands.argument("targets", EntityArgument.entities()).executes(ctx -> removePoison(ctx.getSource(), EntityArgument.getEntities(ctx, "targets"))))
                )
                .then(Commands.literal("apply")
                        .then(Commands.argument("targets", EntityArgument.entities())
                                .then(Commands.argument("poisonSource", ResourceLocationArgument.id()).executes(ctx -> addPoison(ctx.getSource(), EntityArgument.getEntities(ctx, "targets"), PoisonSource.byName(ResourceLocationArgument.getId(ctx, "poisonSource")), 0, 30, 0, true))
                                        .then(Commands.argument("potionClass", IntegerArgumentType.integer(0, 3)).executes(ctx -> addPoison(ctx.getSource(), EntityArgument.getEntities(ctx, "targets"), PoisonSource.byName(ResourceLocationArgument.getId(ctx, "poisonSource")), IntegerArgumentType.getInteger(ctx, "potionClass"), 30, 0, true))
                                                .then(Commands.argument("duration", IntegerArgumentType.integer(1, 1000000)).executes(ctx -> addPoison(ctx.getSource(), EntityArgument.getEntities(ctx, "targets"), PoisonSource.byName(ResourceLocationArgument.getId(ctx, "poisonSource")), IntegerArgumentType.getInteger(ctx, "potionClass"), IntegerArgumentType.getInteger(ctx, "duration"), 0, true))
                                                        .then(Commands.argument("amplifier", IntegerArgumentType.integer(0, 255)).executes(ctx -> addPoison(ctx.getSource(), EntityArgument.getEntities(ctx, "targets"), PoisonSource.byName(ResourceLocationArgument.getId(ctx, "poisonSource")), IntegerArgumentType.getInteger(ctx, "potionClass"), IntegerArgumentType.getInteger(ctx, "duration"), IntegerArgumentType.getInteger(ctx, "amplifier"), true))
                                                                .then(Commands.argument("countPoisoning", BoolArgumentType.bool()).executes(ctx -> addPoison(ctx.getSource(), EntityArgument.getEntities(ctx, "targets"), PoisonSource.byName(ResourceLocationArgument.getId(ctx, "poisonSource")), IntegerArgumentType.getInteger(ctx, "potionClass"), IntegerArgumentType.getInteger(ctx, "duration"), IntegerArgumentType.getInteger(ctx, "amplifier"), BoolArgumentType.getBool(ctx, "countPoisoning"))))
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
    }

    private static int addPoison(CommandSourceStack executor, Collection<? extends Entity> targets, PoisonSource source, int potionClass, int duration, int amplifier, boolean countPoisoning) throws CommandSyntaxException
    {
        AtomicInteger ret = new AtomicInteger();

        for(Entity target : targets)
        {
            if(target instanceof LivingEntity living)
            {
                living.getCapability(CapabilityRegistry.POISON_EXPOSURE).ifPresent(cap -> {
                    PoisonEffectInstance.applyPoisonEffect(living, potionClass, duration * 20, amplifier, source);
                    if(countPoisoning) cap.poison(source);
                    ret.incrementAndGet();
                });
            }
        }

        int code = ret.get();

        if(code == 0)
        {
            throw ERROR_APPLY_POISON_FAILED.create();
        } else
        {
            if (targets.size() == 1)
                executor.sendSuccess(new TranslatableComponent("commands.mineria.poison.add.success.single", new TranslatableComponent(source.getTranslationKey()), targets.iterator().next().getDisplayName(), duration), true);
            else
                executor.sendSuccess(new TranslatableComponent("commands.mineria.poison.add.success.multiple", new TranslatableComponent(source.getTranslationKey()), targets.size(), duration), true);

            return code;
        }
    }

    private static int removePoison(CommandSourceStack executor, Collection<? extends Entity> targets) throws CommandSyntaxException
    {
        AtomicInteger ret = new AtomicInteger();

        for(Entity target : targets)
        {
            if(target instanceof LivingEntity living)
            {
                living.getCapability(CapabilityRegistry.POISON_EXPOSURE).ifPresent(cap -> {
                    if(living.hasEffect(MobEffects.POISON) && living.getEffect(MobEffects.POISON) instanceof PoisonEffectInstance poison)
                    {
                        cap.removeFromMap(poison.getPoisonSource());
                        if(living.removeEffect(MobEffects.POISON)) ret.incrementAndGet();
                    }
                });
            }
        }

        int code = ret.get();
        if(code == 0)
        {
            throw ERROR_REMOVE_POISON_FAILED.create();
        }
        else
        {
            if (targets.size() == 1)
                executor.sendSuccess(new TranslatableComponent("commands.mineria.poison.remove.success.single", targets.iterator().next().getDisplayName()), true);
            else
                executor.sendSuccess(new TranslatableComponent("commands.mineria.poison.remove.success.multiple", targets.size()), true);

            return code;
        }
    }
}