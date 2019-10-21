package com.matsg.battlegrounds.mode;

import com.matsg.battlegrounds.TranslationKey;

public enum GameModeType {

    FREE_FOR_ALL(1, TranslationKey.FFA_NAME.getPath(), TranslationKey.FFA_SHORT.getPath()),
    TEAM_DEATHMATCH(2, TranslationKey.TDM_NAME.getPath(), TranslationKey.TDM_SHORT.getPath()),
    ZOMBIES(3, TranslationKey.ZOMBIES_NAME.getPath(), TranslationKey.ZOMBIES_SHORT.getPath());

    private int id;
    private String namePath;
    private String shortNamePath;

    GameModeType(int id, String namePath, String shortNamePath) {
        this.id = id;
        this.namePath = namePath;
        this.shortNamePath = shortNamePath;
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

    public String getNamePath() {
        return namePath;
    }

    public String getShortNamePath() {
        return shortNamePath;
    }
}
