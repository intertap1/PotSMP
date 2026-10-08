package com.common.data;

import com.PotSMP;
import com.mojang.serialization.Codec;
import net.minecraft.core.UUIDUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PotionAffinityData extends SavedData {
    private final Map<UUID, Integer> PotionAffinityLevel = new HashMap<>();

    public PotionAffinityData() {

    }

    public int getPotionAffinity(UUID playerUuid) {
        return PotionAffinityLevel.getOrDefault(playerUuid, 0);
    }

    public void incrementPotionAffinityLevel(UUID playerUuid) {
        if (getPotionAffinity(playerUuid) < 5) {
            PotionAffinityLevel.merge(playerUuid, 1, Integer::sum);
            setDirty();
        }
    }

    public void decreasePotionAffinityLevel(UUID playerUUID) {
        if (getPotionAffinity(playerUUID) > -5) {
            PotionAffinityLevel.merge(playerUUID, -1, Integer::sum);
            setDirty();
        }
    }
    private static final Codec<PotionAffinityData> CODEC = Codec.unboundedMap(
            Codec.STRING,
            Codec.INT
    ).xmap(
            map -> {
                PotionAffinityData data = new PotionAffinityData();
                for (Map.Entry<String, Integer> entry : map.entrySet()) {
                    UUID uuid = UUID.fromString(entry.getKey());
                    int affinityLevel = entry.getValue();
                    data.PotionAffinityLevel.put(uuid, affinityLevel);
                }
                return data;
            },
            data -> {
                Map<String, Integer> map = new HashMap<>();
                for (Map.Entry<UUID, Integer> entry : data.PotionAffinityLevel.entrySet()) {
                    map.put(
                            entry.getKey().toString(),
                            entry.getValue()
                    );
                }
                return map;
            }
            );
    private static final SavedDataType<PotionAffinityData> TYPE = new SavedDataType<>(
            PotSMP.id("potion_affinity_level"),
            PotionAffinityData::new,
            CODEC,
            null
    );
    public static PotionAffinityData getPotionAffinityData (MinecraftServer server) {
        ServerLevel level = server.getLevel(ServerLevel.OVERWORLD);

        if (level == null) {
            throw new IllegalStateException("Overworld does not exist");
        }
        return level.getDataStorage().computeIfAbsent(TYPE);
    }
}
