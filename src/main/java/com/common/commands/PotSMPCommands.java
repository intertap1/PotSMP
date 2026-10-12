package com.common.commands;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permissions;

import java.util.Collection;

public class PotSMPCommands {
    public static void register() {

        CommandRegistrationCallback.EVENT.register((dispatcher,registryAccess,environment) -> {
          dispatcher.register(Commands.literal("potadmin")
                  .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_MODERATOR))
                  .then(Commands.argument("RageBar", EntityArgument.players()).then(Commands.argument("message", MessageArgument.message()).executes(context -> {
                      Collection<ServerPlayer> players = EntityArgument.getPlayers(context, "targets");
                      if (!players.isEmpty()) {
                          MessageArgument.resolveChatMessage(context, "message", message -> rageBar((CommandSourceStack)context.getSource(), players, String.valueOf(message)));
                      }
                      return 1;
                  }))));
        });

    }
    private static void rageBar(final CommandSourceStack source, final Collection<ServerPlayer> players, final String message) {
        source.sendSuccess(() -> Component.literal("Called /command_with_custom_suggestions with value = %s".formatted(players)), false);
    }
}
