package com.common.item;

import com.PotSMP;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public class ModItemIds {
    public static final ResourceKey<Item> AFFINITY_PLUS =
            create("affinity_plus");
    public static final ResourceKey<Item> AFFINITY_PLUS_UNRESTRICTED =
            create("affinity_plus_unrestricted");
    public static final ResourceKey<Item> AFFINITY_MINUS =
            create("affinity_minus");
    private static ResourceKey<Item> create(String name) {
        return ResourceKey.create(
                Registries.ITEM,
                PotSMP.id(name)
        );
    }
}
