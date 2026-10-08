package com.common.effect;

import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class LightningEffect extends MobEffect {
    protected LightningEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xdd42f5);
        Identifier LightningID = Identifier.fromNamespaceAndPath("pot_smp", "effect.lightning_boost");
        this.addAttributeModifier(
                Attributes.MOVEMENT_SPEED,
                LightningID,
                0.20F,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                );
    }

}
