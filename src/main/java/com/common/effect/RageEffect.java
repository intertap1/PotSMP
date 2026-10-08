package com.common.effect;


import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

import static com.PotSMP.CONFIG;
import static com.common.events.DamageEvents.RageHits;

public class RageEffect extends MobEffect {
    protected RageEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xF54927);
    }
    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity entity, int amplifier) {
        if (entity instanceof Player player) {
            UUID uuid = player.getUUID();
            double Hits = RageHits.getOrDefault(uuid, 0.0);
            if (Hits < 1) {
                Hits += CONFIG.PassiveRageBarCharge();
                Hits = Math.min(Hits,1.0);
            };
            if (Hits > 0.9) {
                return false;
            }
            RageHits.put(uuid, Hits);
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 80 == 0;
    }
    public void rageStatusEffects(ServerLevel level, LivingEntity entity) {
        if (entity instanceof Player player) {
            UUID uuid = player.getUUID();
            double Hits = RageHits.getOrDefault(uuid, 0.0);
            if (Hits > 0.25 && Hits < 0.5) {
                Identifier RageID = Identifier.fromNamespaceAndPath("pot_smp", "effect.rage_speed_boost");
                this.addAttributeModifier(
                        Attributes.MOVEMENT_SPEED,
                        RageID,
                        0.20F,
                        AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                );
                this.addAttributeModifier(
                        Attributes.ATTACK_DAMAGE,
                        RageID,
                        0.20F,
                        AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                        );
            }
            if (Hits > 0.5 && Hits < 0.75) {
                Identifier RageID = Identifier.fromNamespaceAndPath("pot_smp", "effect.rage_speed_boost");
                this.addAttributeModifier(
                        Attributes.MOVEMENT_SPEED,
                        RageID,
                        0.40F,
                        AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                );
                this.addAttributeModifier(
                        Attributes.ATTACK_DAMAGE,
                        RageID,
                        0.40F,
                        AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                );
            }
        }

    }
}
