package com.common.events;

import com.common.effect.RageEffect;
import com.common.effect.RegisterEffects;
import com.common.network.ClientboundInfernalShieldChargePayload;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.PotSMP.CONFIG;
import static com.common.effect.UndyingEffect.UndyingCharge;

public class DamageEvents {
    public static final Map<UUID, Double> InfernoHits = new HashMap<>();
    public static final Map<UUID, Double> RageHits = new HashMap<>();
    public static final Map<UUID, Double> AccelerationCharge = new HashMap<>();
    public static void register() {
        ServerLivingEntityEvents.AFTER_DAMAGE.register((entity, source, baseDamageTaken, damageTaken, blocked) -> {
                    AddInfernoCharge(source);
                    AddRageCharge(source);
                    RemoveRageCharge(entity,source);
                    AddUndyingCharge(entity);
                });
    }
    public static void AddInfernoCharge(DamageSource source) {
        if (!(source.getEntity() instanceof ServerPlayer attacker)) {
            return;
        }
        if (!attacker.hasEffect(RegisterEffects.INFERNO)) {
            return;
        }

        UUID uuid = attacker.getUUID();
        double Hits = InfernoHits.getOrDefault(uuid, 0.0);
        if (Hits < 0.99) {
            Hits += CONFIG.InfernoShieldCharge();
        };
        if (Hits > 0.9) {
            if (attacker.level().isClientSide()) {
                return;
            }
            ClientboundInfernalShieldChargePayload payload = new ClientboundInfernalShieldChargePayload(true, attacker.getId());
                for (ServerPlayer player : PlayerLookup.level((ServerLevel) attacker.level())) {
                    ServerPlayNetworking.send(player, payload);
                }
        }
        InfernoHits.put(uuid, Hits);
        attacker.sendSystemMessage(Component.literal("Inferno Charge: " + Hits));
    }
    public static void AddRageCharge(DamageSource source) {
        if (!(source.getEntity() instanceof ServerPlayer attacker)) {
            return;
        }
        if (!attacker.hasEffect(RegisterEffects.RAGE)) {
            return;
        }

        UUID uuid = attacker.getUUID();
        double Hits = RageHits.getOrDefault(uuid, 0.0);
        if (Hits < 0.99) {
            Hits += CONFIG.RageBarCharge();
        };
        if (Hits > 0.9) {
            if (attacker.level().isClientSide()) {
                return;
            }
        }
        RageHits.put(uuid, Hits);
        RageEffect.updateRageAttributes(attacker, Hits);
        attacker.sendSystemMessage(Component.literal("Rage Hits: " + Hits));
    }
    public static void RemoveRageCharge(LivingEntity entity, DamageSource source) {
        if (entity instanceof Player player && source.getEntity() instanceof ServerPlayer attacker && player.hasEffect(RegisterEffects.RAGE)) {
            UUID uuid = player.getUUID();
            double Hits = RageHits.getOrDefault(uuid, 0.0);
            if (Hits > CONFIG.RageBarReduction()) {
                Hits -= CONFIG.RageBarReduction();
            }
            RageHits.put(uuid, Hits);
        }
    }
    public static void AddUndyingCharge(LivingEntity entity) {
        if (!(entity instanceof Player player)) {
            return;
        }
        if (!player.hasEffect(RegisterEffects.UNDYING)) {
            return;
        }

        UUID uuid = player.getUUID();
        double totemCharge = UndyingCharge.getOrDefault(uuid, 0.0);
        if (totemCharge <= 0.99) {
            totemCharge += CONFIG.UndyingDamageCharge();
            totemCharge = Math.min(totemCharge,1.01);
        };
        UndyingCharge.put(uuid, totemCharge);
    }
}


