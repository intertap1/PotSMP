package com.common.network;

import com.PotSMP;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record PlayerPowerPayload(double accelerationCharge, int blockCounter, double UndyingCharge, double InfernoHits, double RageHits) implements CustomPacketPayload {

    public static final Identifier PLAYER_POWER_ID = PotSMP.id("player_power");

    public static final CustomPacketPayload.Type<PlayerPowerPayload> TYPE = new CustomPacketPayload.Type<>(PLAYER_POWER_ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerPowerPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.DOUBLE,
            PlayerPowerPayload::accelerationCharge,

            ByteBufCodecs.VAR_INT,
            PlayerPowerPayload::blockCounter,

            ByteBufCodecs.DOUBLE,
            PlayerPowerPayload::UndyingCharge,

            ByteBufCodecs.DOUBLE,
            PlayerPowerPayload::InfernoHits,

            ByteBufCodecs.DOUBLE,
            PlayerPowerPayload::RageHits,

            PlayerPowerPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
