package com.common.effect;

import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class AccelerationEffect extends MobEffect {
    protected AccelerationEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xdd42f5);
        Identifier AccelerationID = Identifier.fromNamespaceAndPath("pot_smp", "effect.acceleration_boost");
        this.addAttributeModifier(
                Attributes.MOVEMENT_SPEED,
                AccelerationID,
                0.20F,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                );
    }

}
