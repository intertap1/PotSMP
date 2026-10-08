package com.common.config;

import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;
import io.wispforest.owo.config.annotation.RangeConstraint;
import org.jetbrains.annotations.Range;

@Modmenu(modId = "pot_smp")
@Config(name= "Pot-CC", wrapperName = "PotClientConfig")
public class ClientConfig {
    public boolean CooldownsEnabled = true;

    @RangeConstraint(min = -1, max = 2)
    public double CooldownXOffset = -1;

    @RangeConstraint(min= -1, max = 2)
    public float CooldownYOffset = -1;

}
