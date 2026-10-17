package com.common.networking;

public class ClientPowerData {

    public record ClientPlayerPowerData(double accelerationCharge, int blockCounter, double undyingCharge, double RageHits, double InfernoHits) {
        public static final ClientPlayerPowerData EMPTY = new ClientPlayerPowerData(0.0, 0, 0.0, 0, 0);
    }

    private static ClientPlayerPowerData currentData =
            ClientPlayerPowerData.EMPTY;

    public static ClientPlayerPowerData getData() {
        return currentData;
    }

    public static void setData(ClientPlayerPowerData data) {
        currentData = data;
    }
}