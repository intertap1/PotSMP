package com.common.events;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BlockCounter {
    public static final Map<UUID, BlockPos> lastBlock = new HashMap<>();
    public static final Map<UUID, Integer> blockCounter = new HashMap<>();
    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                UUID uuid = player.getUUID();

                BlockPos currentBlock = player.blockPosition().below();
                BlockPos previousBlock = lastBlock.put(uuid,currentBlock);

                if (previousBlock == null) {
                    return;
                }
                if (!currentBlock.equals(previousBlock)) {
                    int i = blockCounter.getOrDefault(uuid, 0);
                    i++;
                    blockCounter.put(uuid,i);
                }
            }
        });
    }
}
