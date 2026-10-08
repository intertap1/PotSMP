package com;

import com.common.config.PotServerConfig;
import com.common.effect.RegisterEffects;
import com.common.events.BlockCounter;
import com.common.events.DamageEvents;
import com.common.events.ItemCallback;
import com.common.init.ModCreativeTabs;
import com.common.item.ModItem;
import com.common.item.ModPotion;
import com.common.network.ClientboundInfernalShieldChargePayload;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.resources.Identifier;

import net.minecraft.world.item.CreativeModeTabs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PotSMP implements ModInitializer {
	public static final PotServerConfig CONFIG = PotServerConfig.createAndLoad();
	public static final String MOD_ID = "pot_smp";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		RegisterEffects.register();
		ModPotion.register();
		ModItem.register();
		DamageEvents.register();
		ItemCallback.register();
		BlockCounter.register();
		ModCreativeTabs.initialize();
		PayloadTypeRegistry.clientboundPlay().register(ClientboundInfernalShieldChargePayload.TYPE, ClientboundInfernalShieldChargePayload.CODEC);
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
