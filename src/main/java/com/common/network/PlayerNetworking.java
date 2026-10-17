package com.common.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

import static com.common.effect.UndyingEffect.UndyingCharge;
import static com.common.events.BlockCounter.blockCounter;
import static com.common.events.DamageEvents.*;

public class PlayerNetworking {
    public static void initialize() {}
    public static void sendPlayerPowerData(ServerPlayer player) {
        UUID uuid = player.getUUID();
        double acceleration = AccelerationCharge.getOrDefault(uuid,0.0);
        int blocks = blockCounter.getOrDefault(uuid,0);
        double undying = UndyingCharge.getOrDefault(uuid,0.0);
        double inferno = InfernoHits.getOrDefault(uuid,0.0);
        double rage = RageHits.getOrDefault(uuid,0.0);


        ServerPlayNetworking.send(player, new PlayerPowerPayload(acceleration,blocks,undying,inferno,rage));

    }
}


