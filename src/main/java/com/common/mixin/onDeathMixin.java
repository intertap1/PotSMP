package com.common.mixin;

import com.common.data.PotionAffinityData;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.util.UUID;

@Mixin(ServerPlayer.class)
public class onDeathMixin {
    @Inject(
            method = "die",
            at = @At("TAIL")
    )
    private void die(DamageSource source, CallbackInfo ci) {
        LivingEntity victim = (LivingEntity) (Object) this;
        if(!(victim instanceof Player playerVictim)) {
            return;
        }
        if (!(source.getEntity() instanceof Player playerKiller)) {
            return;
        }
        MinecraftServer server = playerVictim.level().getServer();
        UUID victimUUID = victim.getUUID();
        UUID killerUUID = playerKiller.getUUID();
        String victimName = victim.getName().getString();
        String killerName = playerKiller.getName().getString();
        PotionAffinityData potionAffinityData = PotionAffinityData.getPotionAffinityData(server);
        if (potionAffinityData.getPotionAffinity(victimUUID) > -5) {
            potionAffinityData.decreasePotionAffinityLevel(victimUUID);
            playerVictim.sendSystemMessage(Component.literal("You have been killed by " + killerName + "!" + " " + "Potion Affinity Level: " + potionAffinityData.getPotionAffinity(victimUUID)).withColor(TextColor.RED));
        }
        if (potionAffinityData.getPotionAffinity(killerUUID) < 5) {
            potionAffinityData.incrementPotionAffinityLevel(killerUUID);
            playerKiller.sendSystemMessage(Component.literal("You have killed " + victimName + "!" + " " + "Potion Affinity Level: " + potionAffinityData.getPotionAffinity(killerUUID)).withColor(TextColor.RED));
        }

    }
}
