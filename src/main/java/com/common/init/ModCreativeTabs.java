package com.common.init;

import com.PotSMP;
import com.common.item.ModItem;

import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;

public class ModCreativeTabs {
    public static final ResourceKey<CreativeModeTab> POT_CREATIVE_TAB_KEY = ResourceKey.create(
            BuiltInRegistries.CREATIVE_MODE_TAB.key(), PotSMP.id("creative_tab")
    );
    public static final CreativeModeTab POT_CREATIVE_TAB = FabricCreativeModeTab.builder()
            .icon(() -> new ItemStack(ModItem.AFFINITY_PLUS))
            .title(Component.translatable("creativeTab.POT_SMP"))
            .displayItems((parameters, output) -> {
                output.accept(ModItem.AFFINITY_MINUS.value());
                output.accept(ModItem.AFFINITY_PLUS.value());
                output.accept(ModItem.AFFINITY_PLUS_UNRESTRICTED.value());
            })
                    .build();
    public static void initialize() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, POT_CREATIVE_TAB_KEY, POT_CREATIVE_TAB);
    }
}