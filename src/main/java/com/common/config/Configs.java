package com.common.config;

import io.wispforest.owo.config.annotation.Config;

@Config(name= "Pot-SC", wrapperName = "PotServerConfig")
public class Configs {
    public double InfernoShieldCharge = 0.1;
    public float InfernoDamageReduction = 0.4F;
    public double RageBarCharge = 0.1;
    public double PassiveRageBarCharge = 0.01;
    public double RageBarReduction = 0.05;
    public double RageSpeedBonus = 0.0;
    public double RageStrengthBonus = 0.0;
    public double AccelerationSpeedBonus = 0.0;
    public double PassiveAccelerationCharge = 0.01;
    public float AccelerationDischargeDamage = 5F;
    public float AccelerationCollisionDamage = 5F;
}
