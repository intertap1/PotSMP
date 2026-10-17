package com.common.item;

import com.PotSMP;
import com.common.effect.RegisterEffects;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.registry.FabricPotionBrewingBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;

import static java.beans.EventHandler.create;

public class ModPotion {
    // Inferno Potion
    public static final Holder<Potion> INFERNO_POTION =
            Registry.registerForHolder(
                    BuiltInRegistries.POTION,
                    ModPotionIds.INFERNO_POTION,
                    new Potion("inferno_potion",
                            new MobEffectInstance(
                                    RegisterEffects.INFERNO,
                                    19200,
                                    0
                            )
                    )
            );
    // Lightning Potion
    public static final Holder<Potion> ACCELERATION_POTION =
            Registry.registerForHolder(
                    BuiltInRegistries.POTION,
                    ModPotionIds.ACCELERATION_POTION,
                    new Potion("acceleration_potion",
                            new MobEffectInstance(
                                    RegisterEffects.ACCELERATION,
                                    19200,
                                    0
                            )
                    )
            );

    // Rage Potion
    public static final Holder<Potion> RAGE_POTION =
            Registry.registerForHolder(
                    BuiltInRegistries.POTION,
                    ModPotionIds.RAGE_POTION,
                    new Potion("rage_potion",
                            new MobEffectInstance(
                                    RegisterEffects.RAGE,
                                    19200,
                                    0
                            )
                    )
            );

    // Undying Potion
    public static final Holder<Potion> UNDYING_POTION =
            Registry.registerForHolder(
                    BuiltInRegistries.POTION,
                    ModPotionIds.UNDYING_POTION,
                    new Potion("undying_potion",
                            new MobEffectInstance(
                                    RegisterEffects.UNDYING,
                                    19200,
                                    0
                            )
                    )
            );

    // Building Potion
    public static final Holder<Potion> HEAVY_POTION =
            Registry.registerForHolder(
                    BuiltInRegistries.POTION,
                    ModPotionIds.HEAVY_POTION,
                    new Potion("heavy_potion",
                            new MobEffectInstance(
                                    RegisterEffects.HEAVY,
                                    19200,
                                    0
                            )
                    )
            );

    // Mining Potion
    public static final Holder<Potion> MINING_POTION =
            Registry.registerForHolder(
                    BuiltInRegistries.POTION,
                    ModPotionIds.MINING_POTION,
                    new Potion("mining_potion",
                            new MobEffectInstance(
                                    MobEffects.STRENGTH,
                                    200,
                                    0
                            )
                    )
            );

    // Shrinking Potion
    public static final Holder<Potion> SHRINKING_POTION =
            Registry.registerForHolder(
                    BuiltInRegistries.POTION,
                    ModPotionIds.SHRINKING_POTION,
                    new Potion("shrinking_potion",
                            new MobEffectInstance(
                                    MobEffects.STRENGTH,
                                    200,
                                    0
                            )
                    )
            );

    // Titan Potion
    public static final Holder<Potion> TITAN_POTION =
            Registry.registerForHolder(
                    BuiltInRegistries.POTION,
                    ModPotionIds.TITAN_POTION,
                    new Potion("titan_potion",
                            new MobEffectInstance(
                                    MobEffects.STRENGTH,
                                    200,
                                    0
                            )
                    )
            );
    public static void register() {
        FabricPotionBrewingBuilder.BUILD.register(builder -> {
            builder.addMix(
                    Potions.WATER,
                    Items.POTATO,
                    INFERNO_POTION
            );
        });
    }
}
