package com.common.effect;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.PotSMP.CONFIG;
import static com.common.effect.AccelerationEffect.playSound;

public class UndyingEffect extends MobEffect {
    public static final Map<UUID, Double> UndyingCharge = new HashMap<>();
    protected UndyingEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x8BB013);
    }
    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity entity, int amplifier) {
        if (entity instanceof Player player) {
            UUID uuid = player.getUUID();
            double passiveCharge = UndyingCharge.getOrDefault(uuid, 0.0);

            if (passiveCharge <= 1) {
                passiveCharge += CONFIG.PassiveUndyingharge();
                passiveCharge = Math.min(passiveCharge,1.01);
                UndyingCharge.put(uuid, passiveCharge);
            }
            if (passiveCharge == 0.25) {
                playSound(SoundEvents.PLAYER_LEVELUP, player, SoundSource.PLAYERS);
                player.sendOverlayMessage(Component.literal("Charge 25%").withColor(TextColor.GREEN));
            }
            if (passiveCharge == 0.50) {
                playSound(SoundEvents.PLAYER_LEVELUP, player, SoundSource.PLAYERS);
                player.sendOverlayMessage(Component.literal("Charge 50%").withColor(TextColor.GREEN));
            }
            if (passiveCharge == 0.75) {
                playSound(SoundEvents.PLAYER_LEVELUP, player, SoundSource.PLAYERS);
                player.sendOverlayMessage(Component.literal("Charge 75%").withColor(TextColor.GREEN));
            }
            if (passiveCharge == 1) {
                playSound(SoundEvents.PLAYER_LEVELUP, player, SoundSource.PLAYERS);
                player.sendOverlayMessage(Component.literal("Charge 100%").withColor(TextColor.GREEN));
            }
        }
        return true;
    }
    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 20 == 0;
    }
}
