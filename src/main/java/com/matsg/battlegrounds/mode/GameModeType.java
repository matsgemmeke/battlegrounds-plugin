package com.matsg.battlegrounds.mode;

public enum GameModeType {

    FREE_FOR_ALL(1),
    TEAM_DEATHMATCH(2),
    ZOMBIES(3);

    private int id;

    GameModeType(int id) {
        this.id = id;
    }

    public static GameModeType valueOf(int id) {
        for (GameModeType type : values()) {
            if (type.id == id) {
                return type;
            }
        }
        return null;
    }

    public int getId() {
        return id;
    }
}
