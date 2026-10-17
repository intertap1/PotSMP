package com.common.mixin;

import com.common.effect.RegisterEffects;
import com.common.network.ClientboundInfernalShieldChargePayload;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.include.com.google.common.base.Predicate;

import java.util.Collection;
import java.util.UUID;

import static com.common.effect.UndyingEffect.UndyingCharge;
import static com.common.events.BlockCounter.blockCounter;
import static com.PotSMP.CONFIG;
import static com.common.events.DamageEvents.*;
import static com.common.network.PlayerNetworking.sendPlayerPowerData;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
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
                if (player.hasEffect(RegisterEffects.INFERNO)) {
                    amount = InfernoReduction(amount, world, player);
                }
                Entity attacker = source.getEntity();
                if (attacker instanceof ServerPlayer attackingPlayer) {
                    if (attackingPlayer.hasEffect(RegisterEffects.HEAVY)) {
                        amount = HeavyAddition(amount, source, player, attackingPlayer);
                    }
                }
            }
        }
        return amount;
    }

    @Inject(
            method = "onEffectAdded",
            at = @At("HEAD")
    )
    private void onApplyStatusEffect(MobEffectInstance effect, Entity source, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if(!(entity instanceof ServerPlayer player)) {
            return;
        }
        if (!entity.level().isClientSide()) {

            if (effect.is(RegisterEffects.INFERNO)) {
                UUID uuid = entity.getUUID();
                InfernoHits.put(uuid, 0.0);
            }

            if (effect.is(RegisterEffects.RAGE)) {
                UUID uuid = entity.getUUID();
                RageHits.put(uuid, 0.0);
            }
            if (effect.is(RegisterEffects.ACCELERATION)) {
                UUID uuid = entity.getUUID();
                blockCounter.put(uuid, 0);
            }
            if (effect.is(RegisterEffects.ACCELERATION)) {
                UUID uuid = entity.getUUID();
                AccelerationCharge.put(uuid, 0.0);
            }
            if (effect.is(RegisterEffects.UNDYING)) {
                UUID uuid = entity.getUUID();
                UndyingCharge.put(uuid,0.0);
            }
            sendPlayerPowerData(player);
        }
    }

    @Unique
    public void playSound(Holder<SoundEvent> sound, LivingEntity player, SoundSource soundSource) {
        Level world = player.level();
        world.playSound(
                null,
                player.getX(), player.getY(), player.getZ(),
                sound,
                soundSource,
                1f,
                1f
        );
    }

    @Unique
    private float InfernoReduction(float amount, ServerLevel world, LivingEntity player) {
        UUID uuid = player.getUUID();
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
        return amount;
    }
    @Unique
    private float HeavyAddition(float amount, DamageSource damageSource, LivingEntity player, ServerPlayer attacker) {
        double fallDistance = attacker.fallDistance;

        double damage;

        if (fallDistance <= 3.0) {
            damage = 4.0 * fallDistance;
        } else if (fallDistance <= 8.0) {
            damage = 12.0 + 2.0 * (fallDistance - 3.0);
        } else {
            damage = 22.0 + fallDistance - 8.0;
        }
        float newDamage =  attacker.level() instanceof ServerLevel level
                ? (float)(damage + EnchantmentHelper.modifyFallBasedDamage(level, attacker.getWeaponItem(), player, damageSource, 0.0F) * fallDistance)
                : (float)damage;
        if (fallDistance > 1.5) {
            ServerLevel level = (ServerLevel)attacker.level();
            onHurt(attacker,player,level);
            attacker.resetFallDistance();
            knockback(level,attacker,player);
            heavySlowness(attacker);
        }
        newDamage = newDamage + amount;
        return newDamage;
    }
    @Unique
    private void onHurt(LivingEntity attacker, LivingEntity victim, ServerLevel level) {

        if (attacker.onGround()) {
            if (attacker instanceof ServerPlayer player) {
                player.setSpawnExtraParticlesOnFall(true);
            }

            SoundEvent sound = attacker.fallDistance > 5.0 ? SoundEvents.MACE_SMASH_GROUND_HEAVY : SoundEvents.MACE_SMASH_GROUND;
            level.playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), sound, attacker.getSoundSource(), 1.0F, 1.0F);
        } else {
            level.playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), SoundEvents.MACE_SMASH_AIR, attacker.getSoundSource(), 1.0F, 1.0F);
        }

    }
    @Unique
    private static void knockback(final Level level, final Entity attacker, final Entity entity) {
        level.levelEvent(2013, entity.getOnPos(), 750);
        level.getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(3.5), knockbackPredicate(attacker, entity)).forEach(nearby -> {
            Vec3 direction = nearby.position().subtract(entity.position());
            double knockbackPower = getKnockbackPower(attacker, nearby, direction);
            Vec3 knockbackVector = direction.normalize().scale(knockbackPower);
            if (knockbackPower > 0.0) {
                nearby.push(knockbackVector.x, 0.7F, knockbackVector.z);

            }
        });
    }
    @Unique
    private static Predicate<LivingEntity> knockbackPredicate(final Entity attacker, final Entity entity) {
        return nearby -> {
            boolean notSpectator = !nearby.isSpectator();
            boolean notPlayer = nearby != attacker && nearby != entity;
            boolean notAlliedToPlayer = !attacker.isAlliedTo(nearby);
            boolean notTamedByPlayer = !(
                    nearby instanceof TamableAnimal animal && entity instanceof LivingEntity livingAttacker && animal.isTame() && animal.isOwnedBy(livingAttacker)
            );
            boolean notArmorStand = !(nearby instanceof ArmorStand armorStand && armorStand.isMarker());
            boolean withinRange = entity.distanceToSqr(nearby) <= Math.pow(3.5, 2.0);
            boolean notFlyingInCreative = !(nearby instanceof Player player && player.isCreative() && player.getAbilities().flying);
            return notSpectator && notPlayer && notAlliedToPlayer && notTamedByPlayer && notArmorStand && withinRange && notFlyingInCreative;
        };
    }
    @Unique
    private static double getKnockbackPower(final Entity attacker, final LivingEntity nearby, final Vec3 direction) {
        return (3.5 - direction.length()) * 0.7F * (attacker.fallDistance > 5.0 ? 2 : 1) * (1.0 - nearby.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
    }
    @Unique
    private void heavySlowness(LivingEntity attacker) {
        for (Entity entity : attacker.level().getEntities(
                attacker, attacker.getBoundingBox(), entity -> entity != attacker
        )) {
            if (attacker.getBoundingBox().intersects(entity.getBoundingBox())) {
                ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 100, 0));
            }
        }
    }
}
