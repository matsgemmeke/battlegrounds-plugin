package com.matsg.battlegrounds.nms;

public enum EnumVersion {

    V1_8_R2("1.8.R2", 8),
    V1_8_R3("1.8.R3", 8),
    V1_9_R1("1.9.R1", 9),
    V1_9_R2("1.9.R2", 9),
    V1_10_R1("1.10.R1", 10),
    V1_11_R1("1.11.R1", 11),
    V1_12_R1("1.12.R1", 12),
    V1_13_R1("1.13.R1", 13),
    V1_13_R2("1.13.R2", 13);

    private int value;
    private String version;

    EnumVersion(String version, int value) {
        this.version = version;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public String toString() {
        return version;
    }
}
