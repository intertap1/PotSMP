package com.common.item;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;


public class ModItem {
    // Affinity Plus Item
    public static Holder<Item> AFFINITY_PLUS =
            Registry.registerForHolder(
                    BuiltInRegistries.ITEM,
                    ModItemIds.AFFINITY_PLUS,
                    new Item(new Item.Properties().stacksTo(1).setId(ModItemIds.AFFINITY_PLUS))
            );
    // Affinity Plus Unrestricted Item
    public static Holder<Item> AFFINITY_PLUS_UNRESTRICTED =
            Registry.registerForHolder(
                    BuiltInRegistries.ITEM,
                    ModItemIds.AFFINITY_PLUS_UNRESTRICTED,
                    new Item(new Item.Properties().stacksTo(1).setId(ModItemIds.AFFINITY_PLUS_UNRESTRICTED))
            );
    // Affinity Minus
    public static Holder<Item> AFFINITY_MINUS =
            Registry.registerForHolder(
                    BuiltInRegistries.ITEM,
                    ModItemIds.AFFINITY_MINUS,
                    new Item(new Item.Properties().stacksTo(1).setId(ModItemIds.AFFINITY_MINUS))
            );
    public static void register() {
    }
}
