package com.matsg.battlegrounds.mode;

import com.matsg.battlegrounds.TranslationKey;

public enum GameModeType {

    FREE_FOR_ALL(1, TranslationKey.FFA_NAME.getPath(), TranslationKey.FFA_SHORT.getPath(), TranslationKey.FFA_DESCRIPTION.getPath()),
    TEAM_DEATHMATCH(2, TranslationKey.TDM_NAME.getPath(), TranslationKey.TDM_SHORT.getPath(), TranslationKey.TDM_DESCRIPTION.getPath()),
    ZOMBIES(3, TranslationKey.ZOMBIES_NAME.getPath(), TranslationKey.ZOMBIES_SHORT.getPath(), TranslationKey.ZOMBIES_DESCRIPTION.getPath());

    private int id;
    private String descriptionPath;
    private String namePath;
    private String shortNamePath;

    GameModeType(int id, String namePath, String shortNamePath, String descriptionPath) {
        this.id = id;
        this.namePath = namePath;
        this.shortNamePath = shortNamePath;
        this.descriptionPath = descriptionPath;
    }

    public static GameModeType valueOf(int id) {
        for (GameModeType type : values()) {
            if (type.id == id) {
                return type;
            }
        }
        return null;
    }

    public String getDescriptionPath() {
        return descriptionPath;
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
