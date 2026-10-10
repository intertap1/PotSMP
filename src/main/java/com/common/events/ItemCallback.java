package com.common.events;

import com.common.data.ModPotionTagProvider;
import com.common.data.PotionAffinityData;
import com.common.item.ModItem;
import com.common.item.ModPotionIds;
import com.common.item.ModPotionTags;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import java.util.UUID;


public class ItemCallback {
    public static void register() {
        UseItemCallback.EVENT.register((player,level,hand) -> {
            MinecraftServer server = level.getServer();
            UUID uuid = player.getUUID();

            if (server == null) {
                return InteractionResult.PASS;
            }

            if (player.isSpectator()) {
                return InteractionResult.PASS;
            }
            return PotionAffinityItems(player,uuid,hand,server);
        });
    }
    public static boolean isCustomPotion(Player player, InteractionHand hand) {
        if (hand == InteractionHand.MAIN_HAND) {
            ItemStack stack = player.getMainHandItem();
            return verifyCustomPotion(player, stack);
        }
        else {
            ItemStack stack = player.getOffhandItem();
            return verifyCustomPotion(player, stack);
        }

    }
    public static boolean verifyCustomPotion(Player player, ItemStack stack) {
        PotionContents potionContents = stack.get(DataComponents.POTION_CONTENTS);
        if (potionContents == null) {
            return false;
        }
        return potionContents.potion()
                .map(potion -> potion.is(ModPotionTags.CustomPotions))
                .orElse(false);
    }
    public static int isAffinityItem(Player player, InteractionHand hand) {
        if (hand == InteractionHand.MAIN_HAND) {
            ItemStack stack = player.getMainHandItem();
            return verifyAffinityItem(player, stack);
        }
        else  {
            ItemStack stack = player.getOffhandItem();
            return verifyAffinityItem(player, stack);
        }
    }
    public static int verifyAffinityItem(Player player, ItemStack stack) {
        if (stack.is(ModItem.AFFINITY_PLUS)) {
            return 1;
        }
        if (stack.is(ModItem.AFFINITY_MINUS)) {
            return 2;
        }
        if (stack.is(ModItem.AFFINITY_PLUS_UNRESTRICTED)) {;
            return 3;
        }
        return 4;
    }
    public static InteractionResult PotionAffinityItems(Player player,UUID uuid,InteractionHand hand,MinecraftServer server) {
        if (isCustomPotion(player,hand)) {
            PotionAffinityData potionAffinityData = PotionAffinityData.getPotionAffinityData(server);
            potionAffinityData.incrementPotionAffinityLevel(uuid);
        }
        PotionAffinityData potionAffinityData = PotionAffinityData.getPotionAffinityData(server);
        switch(isAffinityItem(player,hand)) {
            case 1:
                if (potionAffinityData.getPotionAffinity(uuid) < 0) {
                    potionAffinityData.incrementPotionAffinityLevel(uuid);
                    player.setItemInHand(hand,ItemStack.EMPTY);
                    player.sendSystemMessage(Component.literal("You have used an Affinity Item. " + "Potion Affinity Level: " + potionAffinityData.getPotionAffinity(uuid)).withColor(TextColor.GREEN));
                } else {
                    return InteractionResult.FAIL;
                }
                break;
            case 2:
                potionAffinityData.decreasePotionAffinityLevel(uuid);
                player.setItemInHand(hand,ItemStack.EMPTY);
                player.sendSystemMessage(Component.literal("You have used an Affinity Item. " + "Potion Affinity Level: " + potionAffinityData.getPotionAffinity(uuid)).withColor(TextColor.GREEN));
                break;
            case 3:
                potionAffinityData.incrementPotionAffinityLevel(uuid);
                player.setItemInHand(hand,ItemStack.EMPTY);
                player.sendSystemMessage(Component.literal("You have used an Affinity Item. " + "Potion Affinity Level: " + potionAffinityData.getPotionAffinity(uuid)).withColor(TextColor.GREEN));
                break;
        }
        return InteractionResult.PASS;
    }
}
