package com.common.effect;


import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

import static com.PotSMP.CONFIG;
import static com.common.events.DamageEvents.RageHits;

public class RageEffect extends MobEffect {
    protected RageEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xF54927);
    }

    private static final Identifier RAGE_SPEED_ID = Identifier.fromNamespaceAndPath("pot_smp", "rage_speed");
    private static final Identifier RAGE_STRENGTH_ID = Identifier.fromNamespaceAndPath("pot_smp", "rage_damage");

    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity entity, int amplifier) {

        if (entity instanceof Player player) {
            UUID uuid = player.getUUID();

            double Hits = RageHits.getOrDefault(uuid, 0.0);

            if (Hits < 1) {
                Hits += CONFIG.PassiveRageBarCharge();
                Hits = Math.min(Hits, 1.0);
                RageHits.put(uuid, Hits);
            }
            if (Hits > 1) {
                RageHits.put(uuid, 1.0);
            }

        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 80 == 0;
    }

    public static void updateRageAttributes(Player player, double Hits) {
        AttributeInstance speed = player.getAttribute(Attributes.MOVEMENT_SPEED);
        AttributeInstance damage = player.getAttribute(Attributes.ATTACK_DAMAGE);
        if (speed == null || damage == null) {
            return;
        }

        speed.removeModifier(RAGE_SPEED_ID);
        damage.removeModifier(RAGE_STRENGTH_ID);
        double speedBonus = CONFIG.RageSpeedBonus();
        double strengthBonus = CONFIG.RageStrengthBonus();
        if (Hits > 0 && Hits <= 0.25) {
            speedBonus = CONFIG.RageSpeedBonus() + 0.2;
        }
        if (Hits > 0.25 && Hits <= 0.75) {
            speedBonus = CONFIG.RageSpeedBonus() + 0.4;
            strengthBonus = CONFIG.RageStrengthBonus() + 3;
        }
        if (Hits > 0.75) {
            speedBonus = CONFIG.RageSpeedBonus() + 0.4;
            strengthBonus = CONFIG.RageStrengthBonus() + 7.5;
        }
        if (Hits == 0) {
            return;
        }

        AttributeModifier speedModifier = new AttributeModifier(
                RAGE_SPEED_ID,
                speedBonus,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        AttributeModifier damageModifier = new AttributeModifier(
                RAGE_STRENGTH_ID,
                strengthBonus,
                AttributeModifier.Operation.ADD_VALUE);

        speed.addTransientModifier(speedModifier);
        damage.addTransientModifier(damageModifier);
    }

    @Override
    public void removeAttributeModifiers(
            AttributeMap attributes
    ) {
        super.removeAttributeModifiers(attributes);

        AttributeInstance speed = attributes.getInstance(Attributes.MOVEMENT_SPEED);
        AttributeInstance damage = attributes.getInstance(Attributes.ATTACK_DAMAGE);

        if (speed != null) {
            speed.removeModifier(RAGE_SPEED_ID);
        }
        if (damage != null) {
            damage.removeModifier(RAGE_STRENGTH_ID);
        }
    }

}
