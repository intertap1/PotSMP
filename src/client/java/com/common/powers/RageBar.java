package com.common.powers;

import com.common.effect.RegisterEffects;
import com.common.networking.ClientPowerData;
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

public class RageBar implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        HudElementRegistry.attachElementAfter(
                VanillaHudElements.CHAT,
                RageBar.RageOverlay.HUD_ID,
                RageBar.RageOverlay::extract
        );
    }

    public final class RageOverlay {
        static final Identifier HUD_ID = Identifier.fromNamespaceAndPath("pot_smp", "rage_overlay");
        private static final Identifier BAR_TEXTURE = Identifier.fromNamespaceAndPath("pot_smp", "textures/gui/bar_base.png");
        private static final Identifier RAGE_BAR_TEXTURE_FILL = Identifier.fromNamespaceAndPath("pot_smp", "textures/gui/rage_bar.png");
        private RageOverlay() {
        }
        static void extract(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
            Minecraft client = Minecraft.getInstance();

            if (client.player == null || client.gui.screen () != null) return;
            if (!client.player.hasEffect(RegisterEffects.RAGE)) return;
            // Placing Bar
            int priority = checkPotionPriority(client);
            int barWidth = 182;
            int barHeight = 5;
            int screenWidth = graphics.guiWidth();
            int screenHeight = graphics.guiHeight();
            // Calculate Progress
            double Hits = ClientPowerData.getData().RageHits();

            int filledWidth = (int) (barWidth * Hits);
            int x = (int) ((double) (screenWidth - barWidth) / 2 * CONFIG.CooldownXOffset());
            if (priority == 2) {
                barWidth = barWidth / 2;
                x = (int) ((double) ((screenWidth / 2)) * CONFIG.CooldownXOffset());
                filledWidth = filledWidth / 2;
            }
            int y = (int) ((screenHeight - 30) * CONFIG.CooldownYOffset());


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
                    RAGE_BAR_TEXTURE_FILL,
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
        public static int checkPotionPriority(Minecraft client) {
            if (client.player.hasEffect(RegisterEffects.INFERNO)) {
                return 2;
            }
            else if (client.player.hasEffect(RegisterEffects.ACCELERATION)) {
                return 2;
            }
            else {
            return 1;
            }
        }
    }
}
