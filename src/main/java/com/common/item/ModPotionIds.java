package com.common.item;

import com.PotSMP;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.item.alchemy.Potion;

public class ModPotionIds {

    public static final ResourceKey<Potion> INFERNO_POTION =
            create("inferno_potion");
    public static final ResourceKey<Potion> RAGE_POTION =
            create("rage_potion");
    public static final ResourceKey<Potion> ACCELERATION_POTION =
            create("acceleration_potion");
    public static final ResourceKey<Potion> UNDYING_POTION =
            create("undying_potion");
    public static final ResourceKey<Potion> HEAVY_POTION =
            create("heavy_potion");
    public static final ResourceKey<Potion> MINING_POTION =
            create("mining_potion");
    public static final ResourceKey<Potion> SHRINKING_POTION =
            create("shrinking_potion");
    public static final ResourceKey<Potion> TITAN_POTION =
            create("titan_potion");
    private static ResourceKey<Potion> create(String name) {
        return ResourceKey.create(
                Registries.POTION,
                PotSMP.id(name)
        );
    }
}
