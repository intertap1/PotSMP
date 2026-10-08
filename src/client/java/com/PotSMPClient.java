package com;

import com.common.config.PotClientConfig;
import com.common.effect.RegisterEffects;
import com.common.network.ClientboundInfernalShieldChargePayload;
import com.mojang.blaze3d.platform.Window;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.Entity;

import java.util.HashSet;
import java.util.Set;

public class PotSMPClient implements ClientModInitializer {
    public static final PotClientConfig CONFIG = PotClientConfig.createAndLoad();
    private static final Set<Integer> INFERNO_ENTITIES = new HashSet<>();

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            setDefaultConfig();
            RegisterPayloadReceiver();
            createInfernoParticles(client);
        });


    }

    public void setDefaultConfig() {
        // -1 is the default config value
        if (CONFIG.CooldownXOffset() == -1) {
            CONFIG.CooldownXOffset(0.5);
        }
        if (CONFIG.CooldownYOffset() == -1) {
            CONFIG.CooldownYOffset(0.9f);
        }
    }

    public void createInfernoParticles(Minecraft client) {
        for (int entityId : INFERNO_ENTITIES) {
            assert client.level != null;
            Entity entity = client.level.getEntity(entityId);

            if (entity == null) {
                continue;
            }
            int i = 0;
            double radius = 1.5;
            long time = client.level.getGameTime();
            if ((time % 20) < 2) {
                for (double phi = 0; phi < Math.PI; phi += Math.PI / 6) {
                    for (double theta = 0; theta < Math.PI * 2; theta += Math.PI / 6) {
                        double x = entity.getX() + (radius * Math.sin(phi) * Math.cos(theta));
                        double y = entity.getY() + (radius * Math.cos(phi));
                        double z = entity.getZ() + (radius * Math.sin(phi) * Math.sin(theta));
                        SimpleParticleType particle = (i % 2 == 0) ? ParticleTypes.FLAME : ParticleTypes.SMOKE;
                        client.level.addParticle(particle,
                                x,
                                y + 1,
                                z,
                                0,
                                0.1,
                                0);
                    }
                }
            }
        }
    }

    public void RegisterPayloadReceiver() {
        ClientPlayNetworking.registerGlobalReceiver(ClientboundInfernalShieldChargePayload.TYPE, ((payload, context) -> {
            context.client().execute(() -> {
                if (payload.enabled()) {
                    INFERNO_ENTITIES.add(payload.id());
                } else {
                    INFERNO_ENTITIES.remove(payload.id());
                }
            });
        }));
    }
}