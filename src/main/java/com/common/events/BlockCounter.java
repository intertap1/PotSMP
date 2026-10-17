package com.common.events;

import com.common.effect.AccelerationEffect;
import com.common.effect.RegisterEffects;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.common.network.PlayerNetworking.sendPlayerPowerData;

public class BlockCounter {
    public static final Map<UUID, BlockPos> lastBlock = new HashMap<>();
    public static final Map<UUID, Integer> blockCounter = new HashMap<>();
    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                UUID uuid = player.getUUID();
                if (player.hasEffect(RegisterEffects.ACCELERATION)) {
                    BlockPos currentBlock = player.blockPosition().below();
                    BlockPos previousBlock = lastBlock.put(uuid,currentBlock);

                    if (previousBlock == null) {
                        return;
                    }
                    if (currentBlock.getX() != previousBlock.getX() || currentBlock.getZ() != previousBlock.getZ()) {
                        int i = blockCounter.getOrDefault(uuid, 0);
                        i++;
                        i = Math.min(i,250);
                        blockCounter.put(uuid,i);
                        sendPlayerPowerData(player);
                        AccelerationEffect.updateAccelerationAttributes(player, i);
                        AccelerationEffect.sendHotbarMessage(player,i);
                    }
                }
            }
        });
    }
}
