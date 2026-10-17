package com.common.powers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class decayAbility {
    private static final Map<UUID, Boolean> DECAY_ENABLED = new HashMap<>();
    public static final Map<UUID, Integer> DECAY_TIMER = new HashMap<>();

    public static boolean isDecayEnabled(UUID uuid) {
        return DECAY_ENABLED.getOrDefault(uuid, false);
    }

    public static void setDecayEnabled(UUID uuid, boolean enabled) {
        DECAY_ENABLED.put(uuid, enabled);
    }
    public static void setDecayDisabled(UUID uuid) {
        DECAY_ENABLED.put(uuid, false);
    }
    public static int getDecayTimer(UUID uuid) {
        return DECAY_TIMER.getOrDefault(uuid, 0);
    }
    public static void incrimentDecayTimer(UUID uuid, int i) {
        DECAY_TIMER.put(uuid, getDecayTimer(uuid) + i);
    }
    public static void resetDecayTimer(UUID uuid) {
        DECAY_TIMER.put(uuid, 0);
    }
}
