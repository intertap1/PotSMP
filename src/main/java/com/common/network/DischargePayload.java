package com.common.network;

import com.PotSMP;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record DischargePayload(String playerName) implements CustomPacketPayload {

    public static final Identifier DISCHARGE_ID = PotSMP.id("discharge");

    public static final CustomPacketPayload.Type<DischargePayload> TYPE = new CustomPacketPayload.Type<>(DISCHARGE_ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, DischargePayload> CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8,
                    DischargePayload::playerName,
                    DischargePayload::new
            );
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
