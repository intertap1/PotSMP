package com.common.mixin;

import com.common.data.PotionAffinityData;
import com.common.effect.RegisterEffects;
import com.common.events.BlockCounter;
import com.common.network.ClientboundInfernalShieldChargePayload;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.UUID;

import static com.common.events.BlockCounter.blockCounter;
import static com.PotSMP.CONFIG;
import static com.common.events.DamageEvents.*;

@Mixin(LivingEntity.class)
abstract class LivingEntityMixin {

    @Inject(
            method = "onEffectsRemoved",
            at = @At(value = "HEAD")
    )
    private void onEffectRemoved(Collection<MobEffectInstance> effects, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        for (MobEffectInstance effect : effects) {
            if (effect.is(RegisterEffects.INFERNO)) {
                System.out.println("Inferno ended on " + entity.getName().getString());
            }
        }
    }

    @Inject(
            method = "onEffectAdded",
            at = @At(value = "HEAD")
    )
    private void onEffectAdded(MobEffectInstance effect, Entity source, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (effect.is(RegisterEffects.INFERNO)) {
        }
    }

    @ModifyVariable(
            method = "hurtServer",
            at = @At("HEAD"),
            argsOnly = true)
    private float modifyIncomingDamage(float amount, ServerLevel world, DamageSource source, float originalAmount) {
        if ((Object) this instanceof LivingEntity player) {
            if (source.getEntity() != null) {
                UUID uuid = player.getUUID();
                if (player.hasEffect(RegisterEffects.INFERNO)) {


                double Hits = InfernoHits.getOrDefault(uuid, 0.0);
                if (Hits >= 0.99) {
                    Hits = 0;
                    InfernoHits.put(uuid, Hits);
                    ClientboundInfernalShieldChargePayload payload = new ClientboundInfernalShieldChargePayload(false, player.getId());
                    for (ServerPlayer serverPlayer : PlayerLookup.level(world)) {
                        ServerPlayNetworking.send(serverPlayer, payload);
                    }
                    playSound(SoundEvents.SHIELD_BREAK, player, SoundSource.PLAYERS);
                    return amount * CONFIG.InfernoDamageReduction();

                }
            }
        }
    }
        return amount;
    }
    @Inject (
            method = "onEffectAdded",
            at = @At("HEAD")
    )
    private void onApplyStatusEffect(MobEffectInstance effect, Entity source, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (!entity.level().isClientSide()) {

            if(effect.is(RegisterEffects.INFERNO)) {
                UUID uuid = entity.getUUID();
                InfernoHits.put(uuid, 0.0);
            }

            if(effect.is(RegisterEffects.RAGE)) {
                UUID uuid = entity.getUUID();
                RageHits.put(uuid, 0.0);
            }
            if(effect.is(RegisterEffects.ACCELERATION)) {
                UUID uuid = entity.getUUID();
                blockCounter.put(uuid, 0);
            }
            if(effect.is(RegisterEffects.ACCELERATION)) {
                UUID uuid = entity.getUUID();
                AccelerationCharge.put(uuid, 0.0);
            }
        }
    }
    @Unique
    public void playSound(Holder<SoundEvent> sound, LivingEntity player, SoundSource soundSource) {
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