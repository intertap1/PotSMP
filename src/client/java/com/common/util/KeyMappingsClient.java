package com.common.util;

import com.PotSMP;
import com.common.network.DischargePayload;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class KeyMappingsClient implements ClientModInitializer {
    private KeyMapping accelerationDischargeKey;


    @Override
    public void onInitializeClient() {
        KeyMapping.Category category = KeyMapping.Category.register(Identifier.fromNamespaceAndPath("pot_smp", "potion_abilities"));
        accelerationDischargeKey = KeyMappingHelper.registerKeyMapping(
                new KeyMapping(
                        "key.PotSMP.acceleration_discharge",
                        InputConstants.Type.KEYSYM,
                        InputConstants.KEY_J,
                        category
                )
        );


        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (accelerationDischargeKey.consumeClick()) {
                if (client.player == null) {
                    continue;
            }
                ClientPlayNetworking.send(
                        new DischargePayload(client.player.getName().getString()));
                client.player.sendSystemMessage(Component.literal("Key press detected in the world"));
            }
        });
    }
}
