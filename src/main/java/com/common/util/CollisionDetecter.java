package com.common.util;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import java.util.UUID;

import static com.PotSMP.CONFIG;
import static com.common.events.BlockCounter.blockCounter;
import static com.common.network.PlayerNetworking.sendPlayerPowerData;

public class CollisionDetecter {

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                UUID uuid = player.getUUID();
                if (!(blockCounter.getOrDefault(uuid,0) == 250)) {
                    continue;
                }
                for (Entity entity : player.level().getEntities(
                        player, player.getBoundingBox(), entity -> entity != player
                )) {
                    if (player.getBoundingBox().intersects(entity.getBoundingBox())) {
                        onCollision(player,entity);
                        player.sendSystemMessage(Component.literal("You have collided with: " + entity.getName()).withColor(TextColor.DARK_PURPLE));
                    }
                }
            }
        });
    }
    private static void onCollision(ServerPlayer player, Entity entity) {
        UUID playerUUID = player.getUUID();
        int blocks = blockCounter.getOrDefault(playerUUID, 0);
        if (blocks == 250) {
            blockCounter.put(playerUUID, 125);
            sendPlayerPowerData(player);
            entity.hurt(entity.damageSources().lightningBolt(), CONFIG.AccelerationCollisionDamage());
            double dx = entity.getX() - player.getX();
            double dz = entity.getZ() - player.getZ();
            entity.push(dx, 0.5, dz);
            entity.hurtMarked = true;
        }
    }
}
