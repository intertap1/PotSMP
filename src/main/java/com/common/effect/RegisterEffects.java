package com.common.effect;

import com.PotSMP;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;

public class RegisterEffects {
    public static final Holder<MobEffect> INFERNO = Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, PotSMP.id("inferno"), new InfernoEffect());
    public static final Holder<MobEffect> LIGHTNING = Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, PotSMP.id("lightning"), new LightningEffect());
    public static final Holder<MobEffect> RAGE = Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, PotSMP.id("rage"), new RageEffect());
    public static final Holder<MobEffect> UNDYING = Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, PotSMP.id("undying"), new UndyingEffect());
    public static final Holder<MobEffect> HEAVY = Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, PotSMP.id("heavy"), new HeavyEffect());
    public static void register() {
    }
}
