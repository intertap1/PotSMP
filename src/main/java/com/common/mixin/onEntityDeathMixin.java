package com.common.mixin;

import com.common.effect.RegisterEffects;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

import static com.common.effect.UndyingEffect.UndyingCharge;
import static com.common.network.PlayerNetworking.sendPlayerPowerData;

@Mixin(LivingEntity.class)
public class onEntityDeathMixin {
    @Inject (
            method = "checkTotemDeathProtection",
            at = @At("HEAD"),
            cancellable = true)
    private void checkTotemDeathProtection(DamageSource killingDamage, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity victim = (LivingEntity) (Object) this;
        if (!(victim instanceof ServerPlayer player)) {
            return;
        }
        if (!(victim.hasEffect(RegisterEffects.UNDYING))) {
            return;
        }
        UUID victimUUID = victim.getUUID();
        double totemCharge = UndyingCharge.getOrDefault(victimUUID,0.0);
        if (totemCharge >= 1) {
            victim.setHealth(1.0F);
            totemCharge = 0.0;
            UndyingCharge.put(victimUUID,totemCharge);
            UndyingEffect(victim);
            cir.setReturnValue(true);
        }
    }
    @Unique
    private void UndyingEffect(LivingEntity victim) {
        victim.level().broadcastEntityEvent(victim, (byte)35);
        victim.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
        victim.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
        victim.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0));
        victim.removeEffect(RegisterEffects.UNDYING);
        sendPlayerPowerData((ServerPlayer) victim);
    }
}
