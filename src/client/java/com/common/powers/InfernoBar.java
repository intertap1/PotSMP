package com.common.powers;


import com.common.effect.RegisterEffects;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

import java.util.UUID;

import static com.PotSMPClient.CONFIG;
import static com.common.events.DamageEvents.InfernoHits;
import static com.common.events.DamageEvents.RageHits;

public class InfernoBar implements ClientModInitializer {


    @Override
    public void onInitializeClient() {
        HudElementRegistry.attachElementAfter(
                VanillaHudElements.CHAT,
                InfernoOverlay.HUD_ID,
                InfernoOverlay::extract
        );
    }

    public final class InfernoOverlay {
        static final Identifier HUD_ID = Identifier.fromNamespaceAndPath("pot_smp", "inferno_overlay");
        private static final Identifier BAR_TEXTURE = Identifier.fromNamespaceAndPath("pot_smp", "textures/gui/bar_base.png");
        private static final Identifier INFERNO_BAR_TEXTURE_FILL = Identifier.fromNamespaceAndPath("pot_smp", "textures/gui/inferno_bar.png");

        private InfernoOverlay() {
        }

        static void extract(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
            Minecraft client = Minecraft.getInstance();

            if (client.player == null || client.gui.screen() != null) return;
            if (!client.player.hasEffect(RegisterEffects.INFERNO)) return;
            // Placing Bar
            int priority = checkPotionPriority(client);
            int barWidth = 182;
            int barHeight = 5;
            int screenWidth = graphics.guiWidth();
            int screenHeight = graphics.guiHeight();
            // Calculate Progress
            UUID uuid = client.player.getUUID();
            double Hits = InfernoHits.getOrDefault(uuid, 0.0);

            int filledWidth = (int) (barWidth * Hits);
            int x = (int) ((double) (screenWidth - barWidth) / 2 * CONFIG.CooldownXOffset());
            if (priority == 1) {
                barWidth = barWidth / 2;
                x = (int) ((double) ((screenWidth / 2) - barWidth) * CONFIG.CooldownXOffset());
                filledWidth = filledWidth / 2;
            }
            int y = (int) (screenHeight * CONFIG.CooldownYOffset());

            // Unfilled Inferno Bar
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
            // Filled Inferno Bar
            graphics.blit(
                    RenderPipelines.GUI_TEXTURED,
                    INFERNO_BAR_TEXTURE_FILL,
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
            else if (client.player.hasEffect(RegisterEffects.LIGHTNING)) {
                return 1;
            }
            else if (client.player.hasEffect(RegisterEffects.HEAVY)) {
                return 1;
            }
            else if (client.player.hasEffect(RegisterEffects.UNDYING)) {
                return 1;
            }
            else {
                return 2;
            }
        }
    }
