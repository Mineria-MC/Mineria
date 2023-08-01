package io.github.mineria_mc.mineria.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import io.github.mineria_mc.mineria.common.capabilities.ki.IKiCapability;
import io.github.mineria_mc.mineria.common.capabilities.MineriaCapabilities;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public class KiCommand {
    private static final SimpleCommandExceptionType ERROR_KI_CAPABILITY = new SimpleCommandExceptionType(Component.translatable("commands.mineria.ki.no_capability"));
    private static final String GET_SUCCESS = "commands.mineria.ki.get";
    private static final String GET_MAX_SUCCESS = "commands.mineria.ki.get_max";
    private static final String GET_LEVEL_SUCCESS = "commands.mineria.ki.get_level";

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("ki").requires(s -> s.hasPermission(2))
                .then(Commands.literal("get")
                        .executes(context -> {
                            Player target = context.getSource().getPlayerOrException();
                            return sendMessage(context.getSource(),
                                    GET_SUCCESS,
                                    getKiCapability(target).getKi(),
                                    target.getDisplayName()
                            );
                        })
                        .then(Commands.argument("target", EntityArgument.player())
                                .executes(context -> {
                                    Player target = EntityArgument.getPlayer(context, "target");
                                    return sendMessage(context.getSource(),
                                            GET_SUCCESS,
                                            getKiCapability(target).getKi(),
                                            target.getDisplayName()
                                    );
                                })
                        )
                )
                .then(Commands.literal("get_max")
                        .executes(context -> {
                            Player target = context.getSource().getPlayerOrException();
                            return sendMessage(context.getSource(),
                                    GET_MAX_SUCCESS,
                                    target.getDisplayName(),
                                    getKiCapability(target).getMaxKi()
                            );
                        })
                        .then(Commands.argument("target", EntityArgument.player())
                                .executes(context -> {
                                    Player target = EntityArgument.getPlayer(context, "target");
                                    return sendMessage(context.getSource(),
                                            GET_MAX_SUCCESS,
                                            target.getDisplayName(),
                                            getKiCapability(target).getMaxKi()
                                    );
                                })
                        )
                )
                .then(Commands.literal("get_level")
                        .executes(context -> {
                            Player target = context.getSource().getPlayerOrException();
                            return sendMessage(context.getSource(),
                                    GET_LEVEL_SUCCESS,
                                    target.getDisplayName(),
                                    getKiCapability(target).getKiLevel()
                            );
                        })
                        .then(Commands.argument("target", EntityArgument.player())
                                .executes(context -> {
                                    Player target = EntityArgument.getPlayer(context, "target");
                                    return sendMessage(context.getSource(),
                                            GET_LEVEL_SUCCESS,
                                            target.getDisplayName(),
                                            getKiCapability(target).getKiLevel()
                                    );
                                })
                        )
                )
                .then(Commands.literal("set")
                        .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                                .executes(context -> setKi(context.getSource(),
                                        IntegerArgumentType.getInteger(context, "amount"),
                                        context.getSource().getPlayerOrException()
                                ))
                                .then(Commands.argument("target", EntityArgument.player())
                                        .executes(context -> setKi(context.getSource(),
                                                IntegerArgumentType.getInteger(context, "amount"),
                                                EntityArgument.getPlayer(context, "target")
                                        ))
                                )
                        )
                )
                .then(Commands.literal("set_level")
                        .then(Commands.argument("level", IntegerArgumentType.integer(1, IKiCapability.MAX_LEVEL))
                                .executes(context -> setKiLevel(context.getSource(),
                                        IntegerArgumentType.getInteger(context, "level"),
                                        context.getSource().getPlayerOrException()
                                ))
                                .then(Commands.argument("target", EntityArgument.player())
                                        .executes(context -> setKiLevel(context.getSource(),
                                                IntegerArgumentType.getInteger(context, "level"),
                                                EntityArgument.getPlayer(context, "target")
                                        ))
                                )
                        )
                )
                .then(Commands.literal("add")
                        .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                                .executes(context -> {
                                    Player player = context.getSource().getPlayerOrException();
                                    return setKi(context.getSource(),
                                            getKiCapability(player).getKi() + IntegerArgumentType.getInteger(context, "amount"),
                                            player
                                    );
                                })
                                .then(Commands.argument("target", EntityArgument.player())
                                        .executes(context -> {
                                            Player target = EntityArgument.getPlayer(context, "target");
                                            return setKi(context.getSource(),
                                                    getKiCapability(target).getKi() + IntegerArgumentType.getInteger(context, "amount"),
                                                    target
                                            );
                                        })
                                )
                        )
                )
                .then(Commands.literal("remove")
                        .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                                .executes(context -> {
                                    Player player = context.getSource().getPlayerOrException();
                                    return setKi(context.getSource(),
                                            getKiCapability(player).getKi() - IntegerArgumentType.getInteger(context, "amount"),
                                            player
                                    );
                                })
                                .then(Commands.argument("target", EntityArgument.player())
                                        .executes(context -> {
                                            Player target = EntityArgument.getPlayer(context, "target");
                                            return setKi(context.getSource(),
                                                    getKiCapability(target).getKi() - IntegerArgumentType.getInteger(context, "amount"),
                                                    target
                                            );
                                        })
                                )
                        )
                )
                .then(Commands.literal("fill")
                        .executes(context -> {
                            Player player = context.getSource().getPlayerOrException();
                            return setKi(context.getSource(),
                                    getKiCapability(player).getMaxKi(),
                                    player
                            );
                        })
                        .then(Commands.argument("target", EntityArgument.player())
                                .executes(context -> {
                                    Player target = EntityArgument.getPlayer(context, "target");
                                    return setKi(context.getSource(),
                                            getKiCapability(target).getMaxKi(),
                                            target
                                    );
                                })
                        )
                )
                .then(Commands.literal("empty")
                        .executes(context -> setKi(context.getSource(),
                                0,
                                context.getSource().getPlayerOrException()
                        ))
                        .then(Commands.argument("target", EntityArgument.player())
                                .executes(context -> setKi(context.getSource(),
                                        0,
                                        EntityArgument.getPlayer(context, "target")
                                ))
                        ))
        );
    }

    private static int setKi(CommandSourceStack source, int amount, Player target) throws CommandSyntaxException {
        getKiCapability(target).setKi(amount);
        int finalKi = getKiCapability(target).getKi();
        source.sendSuccess(() -> Component.translatable("commands.mineria.ki.set", target.getDisplayName(), finalKi), true);
        return 1;
    }

    private static int setKiLevel(CommandSourceStack source, int level, Player target) throws CommandSyntaxException {
        getKiCapability(target).setKiLevel(level);
        int finalLevel = getKiCapability(target).getKiLevel();
        source.sendSuccess(() -> Component.translatable("commands.mineria.ki.set_level", target.getDisplayName(), finalLevel), true);
        return 1;
    }

    private static int sendMessage(CommandSourceStack source, String componentKey, Object... args) {
        source.sendSuccess(() -> Component.translatable(componentKey, args), true);
        return 1;
    }

    private static IKiCapability getKiCapability(Player player) throws CommandSyntaxException {
        return player.getCapability(MineriaCapabilities.KI).orElseThrow(ERROR_KI_CAPABILITY::create);
    }
}
