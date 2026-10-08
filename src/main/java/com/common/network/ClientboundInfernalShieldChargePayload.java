package com.common.network;

import com.PotSMP;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record ClientboundInfernalShieldChargePayload(boolean enabled, int id) implements CustomPacketPayload {
        public static final Identifier INFERNO_SHIELD_ID = PotSMP.id("inferno_shield");

        public static final CustomPacketPayload.Type<ClientboundInfernalShieldChargePayload> TYPE = new CustomPacketPayload.Type<>(INFERNO_SHIELD_ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundInfernalShieldChargePayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            ClientboundInfernalShieldChargePayload::enabled,

            ByteBufCodecs.VAR_INT,
            ClientboundInfernalShieldChargePayload::id,
            ClientboundInfernalShieldChargePayload::new
            );
            @Override
            public Type<? extends CustomPacketPayload> type() {
                return TYPE;
            }
}