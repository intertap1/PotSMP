package com.common.powers;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;

import java.util.Random;
import java.util.UUID;

import static com.PotSMP.CONFIG;
import static com.common.events.DamageEvents.AccelerationCharge;
import static com.common.network.PlayerNetworking.sendPlayerPowerData;

public class dischargeAbility {

        public static void discharge(Player player) {

        if (!(player.level() instanceof ServerLevel level)) {
            return;
        }
        UUID uuid = player.getUUID();
        double dischargeAmount = AccelerationCharge.getOrDefault(uuid, 0.0);
        if (dischargeAmount >= 0.25) {
            for (LivingEntity entity : player.level().getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(5), entity -> entity != player)) {
                entity.hurt(entity.damageSources().lightningBolt(), CONFIG.AccelerationDischargeDamage() );
            }
            Random random = new Random();
            for (int i = 0; i < 5; i++) {
                double x = player.getX() + random.nextInt(5) - 2;
                double z = player.getZ() + random.nextInt(5) - 2;
                double y = player.getY();

                LightningBolt lightning = new LightningBolt(EntityTypes.LIGHTNING_BOLT, level);

                lightning.setPos(x,y,z);
                lightning.setVisualOnly(true);
                level.addFreshEntity(lightning);


            }
            dischargeAmount -= 0.25;
            AccelerationCharge.put(uuid,dischargeAmount);
            sendPlayerPowerData((ServerPlayer) player);
        }
    }
}
