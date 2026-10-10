package com.common.effect;

import com.common.powers.decayAbility;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.UUID;

import static com.PotSMP.CONFIG;
import static com.common.events.BlockCounter.blockCounter;
import static com.common.events.DamageEvents.AccelerationCharge;

public class AccelerationEffect extends MobEffect {
    protected AccelerationEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xdd42f5);
    }
    private static final Identifier ACCELERATION_SPEED_ID = Identifier.fromNamespaceAndPath("pot_smp", "acceleration_speed");
    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity entity, int amplifier) {
        if (entity instanceof Player player) {
            UUID uuid = player.getUUID();
            double passiveCharge = AccelerationCharge.getOrDefault(uuid, 0.0);

            if (passiveCharge < 1) {
                passiveCharge += CONFIG.PassiveAccelerationCharge();
                passiveCharge = Math.min(passiveCharge,1.0);
                AccelerationCharge.put(uuid, passiveCharge);
            }
            if (passiveCharge == 0.25) {
                playSound(SoundEvents.PLAYER_LEVELUP, player, SoundSource.PLAYERS);
            }

            int blocks = blockCounter.getOrDefault(uuid, 0);
            if (blocks == 250) {
                decayAbility.incrimentDecayTimer(uuid, 1);
            }
            if (decayAbility.getDecayTimer(uuid) == 5 ) {
                decayAbility.resetDecayTimer(uuid);
                blockCounter.put(uuid,125);
            }
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 20 == 0;
    }

    public static void updateAccelerationAttributes(Player player, int blocks) {
        AttributeInstance speed = player.getAttribute(Attributes.MOVEMENT_SPEED);
        if (speed == null) {
            return;
        }
        speed.removeModifier(ACCELERATION_SPEED_ID);
        double speedBonus = CONFIG.AccelerationSpeedBonus();
        if (blocks >= 250) {
            speedBonus += 1.0;
        } else if (blocks >= 125) {
            speedBonus += 0.8;
        } else if (blocks >= 100) {
            speedBonus += 0.6;
        } else if (blocks >= 50) {
            speedBonus += 0.4;
        } else if (blocks >= 25) {
            speedBonus += 0.2;
        }
        switch (blocks) {
            case 25:
                player.sendSystemMessage(Component.literal("You have reached 25 steps.").withColor(TextColor.DARK_PURPLE));
                break;
            case 50:
                player.sendSystemMessage(Component.literal("You have reached 50 steps.").withColor(TextColor.DARK_PURPLE));
                break;
            case 100:
                player.sendSystemMessage(Component.literal("You have reached 100 steps.").withColor(TextColor.DARK_PURPLE));
                break;
            case 125:
                player.sendSystemMessage(Component.literal("You have reached 125 steps.").withColor(TextColor.DARK_PURPLE));
                break;
        }

        AttributeModifier speedModifier = new AttributeModifier(
                ACCELERATION_SPEED_ID,
                speedBonus,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

        speed.addTransientModifier(speedModifier);
    }
    @Override
    public void removeAttributeModifiers(
            AttributeMap attributes
    ) {
        super.removeAttributeModifiers(attributes);

        AttributeInstance speed = attributes.getInstance(Attributes.MOVEMENT_SPEED);

        if (speed != null) {
            speed.removeModifier(ACCELERATION_SPEED_ID);
        }
    }
    public static void sendHotbarMessage(ServerPlayer player, int blockCounter) {
        player.sendOverlayMessage(Component.literal("Bʟᴏᴄᴋs: " + blockCounter + "/" + "250").withColor(TextColor.LIGHT_PURPLE));
    }
    public void playSound(SoundEvent sound, LivingEntity player, SoundSource soundSource) {
        Level world = player.level();
        world.playSound (
                null,
                player.getX(), player.getY(), player.getZ(),
                sound,
                soundSource,
                1f,
                1f
        );
    }
}
