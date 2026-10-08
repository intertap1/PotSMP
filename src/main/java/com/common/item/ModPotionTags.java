package com.common.item;

import com.PotSMP;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.alchemy.Potion;

public class ModPotionTags {
    public static final TagKey<Potion> CustomPotions = TagKey.create(Registries.POTION, PotSMP.id("custom_potion"));
}
