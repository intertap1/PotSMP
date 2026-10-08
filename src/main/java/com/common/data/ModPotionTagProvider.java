package com.common.data;

import com.PotSMP;
import com.common.item.ModPotionIds;
import com.common.item.ModPotionTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.alchemy.Potion;

import java.util.concurrent.CompletableFuture;

public class ModPotionTagProvider extends FabricTagsProvider<Potion> {
    public ModPotionTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.POTION, registriesFuture);
    }
    @Override
    protected void addTags(HolderLookup.Provider provider) {
        builder(ModPotionTags.CustomPotions)
                .add(ModPotionIds.BUILDING_POTION)
                .add(ModPotionIds.INFERNO_POTION)
                .add(ModPotionIds.LIGHTNING_POTION)
                .add(ModPotionIds.SHRINKING_POTION)
                .add(ModPotionIds.UNDYING_POTION)
                .add(ModPotionIds.MINING_POTION)
                .add(ModPotionIds.TITAN_POTION);
    }

}
