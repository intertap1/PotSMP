package com.common.powers;

import com.common.effect.RegisterEffects;
import com.common.networking.ClientPowerData;
import com.mojang.authlib.minecraft.client.MinecraftClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerEntity;

import java.util.UUID;

import static com.PotSMPClient.CONFIG;
import static com.common.events.DamageEvents.AccelerationCharge;

public class AccelerationBar implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HudElementRegistry.attachElementAfter(
                VanillaHudElements.CHAT,
                AccelerationOverlay.HUD_ID,
                AccelerationOverlay::extract
        );
    }

    public final class AccelerationOverlay {
        static final Identifier HUD_ID = Identifier.fromNamespaceAndPath("pot_smp", "acceleration_overlay");
        private static final Identifier BAR_TEXTURE = Identifier.fromNamespaceAndPath("pot_smp", "textures/gui/bar_base.png");
        private static final Identifier ACCELERATION_BAR_TEXTURE_FILL = Identifier.fromNamespaceAndPath("pot_smp", "textures/gui/acceleration_bar.png");

        private AccelerationOverlay() {
        }

        static void extract(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
            Minecraft client = Minecraft.getInstance();

            if (client.player == null || client.gui.screen() != null) return;
            if (!client.player.hasEffect(RegisterEffects.ACCELERATION)) return;
            // Placing Bar
            int priority = checkPotionPriority(client);
            int barWidth = 182;
            int barHeight = 5;
            int screenWidth = graphics.guiWidth();
            int screenHeight = graphics.guiHeight();
            // Calculate Progress
            double passiveCharge = ClientPowerData.getData().accelerationCharge();

            int filledWidth = (int) (barWidth * passiveCharge);
            int x = (int) ((double) (screenWidth - barWidth) / 2 * CONFIG.CooldownXOffset());
            if (priority == 1) {
                barWidth = barWidth / 2;
                x = (int) ((double) ((screenWidth / 2) - barWidth) * CONFIG.CooldownXOffset());
                filledWidth = filledWidth / 2;
            }
            if (priority == 3) {
                barWidth = barWidth / 2;
                x = (int) ((double) ((screenWidth / 2)) * CONFIG.CooldownXOffset());
                filledWidth = filledWidth / 2;
            }
            int y = (int) ((screenHeight - 30) * CONFIG.CooldownYOffset());

            // Unfilled Acceleration Bar
            graphics.blit(
                    RenderPipelines.GUI_TEXTURED,
                    BAR_TEXTURE,
                    x,
                    y,
                    0,
                    0,
                    barWidth,
                    barHeight,
                    barWidth,
                    barHeight
            );
            // Filled Acceleration Bar
            graphics.blit(
                    RenderPipelines.GUI_TEXTURED,
                    ACCELERATION_BAR_TEXTURE_FILL,
                    x,
                    y,
                    0,
                    0,
                    filledWidth,
                    barHeight,
                    barWidth,
                    barHeight
            );
        }
    }
    public static int checkPotionPriority(Minecraft client) {
        if (client.player.hasEffect(RegisterEffects.RAGE)) {
            return 1;
        }
        else if (client.player.hasEffect(RegisterEffects.INFERNO)) {
            return 3;
        }
        else {
            return 2;
        }
    }
}
