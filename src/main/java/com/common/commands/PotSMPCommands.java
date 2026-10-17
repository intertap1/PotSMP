package com.common.commands;

import com.common.effect.UndyingEffect;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permissions;

import java.util.Map;
import java.util.UUID;

import static com.common.data.PotionAffinityData.PotionAffinityLevel;
import static com.common.effect.UndyingEffect.UndyingCharge;
import static com.common.events.BlockCounter.blockCounter;
import static com.common.events.DamageEvents.*;


public class PotSMPCommands {
    public static void register() {

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            var root = Commands.literal("potadmin")
                    .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_MODERATOR));
            root.then(doubleStatCommands(Commands.literal("rageBar"),"RageBar", RageHits))
            .then(doubleStatCommands(Commands.literal("infernoHits"), "InfernoHits", (InfernoHits)))
            .then(doubleStatCommands(Commands.literal("accelerationCharge"), "AccelerationCharge", (AccelerationCharge)))
            .then(doubleStatCommands(Commands.literal("undyingCharge"), "UndyingCharge", (UndyingCharge)))
            .then(integerStatCommands(Commands.literal("blockCounter"), "BlockCounter", (blockCounter)))
            .then(integerStatCommands(Commands.literal("potionAffinity"), "PotionAffinity", (PotionAffinityLevel)));
            dispatcher.register(root);
        });

    }
    private static LiteralArgumentBuilder<CommandSourceStack> doubleStatCommands(LiteralArgumentBuilder<CommandSourceStack> command, String name, Map<UUID, Double> values) {
        command.then(Commands.literal("get")
                .then(Commands.argument("player", EntityArgument.player())
                        .executes(context -> {
                            ServerPlayer player = EntityArgument.getPlayer(context, "player");
                            double value = values.getOrDefault(player.getUUID(), 0.0);
                            context.getSource().sendSuccess(() -> Component.literal(player.getName().getString() + "'s " + name + ": " + value), false);
                            return 1;
                        })));
        command.then(Commands.literal("set")
                .then(Commands.argument("player", EntityArgument.player())
                        .then(Commands.argument("value", DoubleArgumentType.doubleArg()
                        ).executes(context -> {ServerPlayer player = EntityArgument.getPlayer(context, "player");
                            double value = DoubleArgumentType.getDouble(context, "value");
                            values.put(player.getUUID(), value);
                            context.getSource().sendSuccess(() -> Component.literal("Set " + player.getName().getString() + "'s " + name + " to " + value), true);
                            return 1;
                        }))));
        command.then(Commands.literal("reset")
                .then(Commands.argument("player", EntityArgument.player())
                        .executes(context -> {
                            ServerPlayer player = EntityArgument.getPlayer(context, "player");
                            values.remove(player.getUUID());
                            context.getSource().sendSuccess(() -> Component.literal("Reset " + player.getName().getString() + "'s " + name), true);
                            return 1;
                        })));
        return command;
    }
    private static LiteralArgumentBuilder<CommandSourceStack> integerStatCommands(LiteralArgumentBuilder<CommandSourceStack> command, String name, Map<UUID, Integer> values) {
        command.then(Commands.literal("get")
                .then(Commands.argument("player", EntityArgument.player())
                        .executes(context -> {
                            ServerPlayer player = EntityArgument.getPlayer(context, "player");
                            int value = values.getOrDefault(player.getUUID(), 0);
                            context.getSource().sendSuccess(() -> Component.literal(player.getName().getString() + "'s " + name + ": " + value), false);
                            return 1;
                        })));
        command.then(Commands.literal("set")
                .then(Commands.argument("player", EntityArgument.player())
                        .then(Commands.argument("value", IntegerArgumentType.integer(0))
                                .executes(context -> {ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                int value = IntegerArgumentType.getInteger(context, "value");
                                values.put(player.getUUID(), value);
                                context.getSource().sendSuccess(() -> Component.literal("Set " + player.getName().getString() + "'s " + name + " to " + value), true);
                                return 1;
                            }))));
        command.then(Commands.literal("reset")
                .then(Commands.argument("player", EntityArgument.player())
                        .executes(context -> {
                            ServerPlayer player = EntityArgument.getPlayer(context, "player");
                            values.remove(player.getUUID());
                            context.getSource().sendSuccess(() -> Component.literal("Reset " + player.getName().getString() + "'s " + name), true);
                            return 1;
                        })));
        return command;
    }
}
