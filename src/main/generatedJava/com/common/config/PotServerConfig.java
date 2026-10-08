package com.common.config;

import blue.endless.jankson.Jankson;
import io.wispforest.owo.config.ConfigWrapper;
import io.wispforest.owo.config.ConfigWrapper.BuilderConsumer;
import io.wispforest.owo.config.Option;
import io.wispforest.owo.util.Observable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class PotServerConfig extends ConfigWrapper<com.common.config.Configs> {

    public final Keys keys = new Keys();

    private final Option<java.lang.Double> InfernoShieldCharge = this.optionForKey(this.keys.InfernoShieldCharge);
    private final Option<java.lang.Float> InfernoDamageReduction = this.optionForKey(this.keys.InfernoDamageReduction);

    private PotServerConfig() {
        super(com.common.config.Configs.class);
    }

    private PotServerConfig(BuilderConsumer consumer) {
        super(com.common.config.Configs.class, consumer);
    }

    public static PotServerConfig createAndLoad() {
        var wrapper = new PotServerConfig();
        wrapper.load();
        return wrapper;
    }

    public static PotServerConfig createAndLoad(BuilderConsumer consumer) {
        var wrapper = new PotServerConfig(consumer);
        wrapper.load();
        return wrapper;
    }

    public double InfernoShieldCharge() {
        return InfernoShieldCharge.value();
    }

    public void InfernoShieldCharge(double value) {
        InfernoShieldCharge.set(value);
    }

    public float InfernoDamageReduction() {
        return InfernoDamageReduction.value();
    }

    public void InfernoDamageReduction(float value) {
        InfernoDamageReduction.set(value);
    }


    public static class Keys {
        public final Option.Key InfernoShieldCharge = new Option.Key("InfernoShieldCharge");
        public final Option.Key InfernoDamageReduction = new Option.Key("InfernoDamageReduction");
    }
}

