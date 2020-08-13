package com.matsg.battlegrounds.storage;

import com.matsg.battlegrounds.api.storage.AbstractYaml;
import com.matsg.battlegrounds.api.storage.LevelConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class BattleLevelConfig extends AbstractYaml implements LevelConfig {

    public BattleLevelConfig(String filePath, InputStream resource) throws IOException {
        super("levels.yml", filePath, resource, true);
    }

    public int getExp(int level) {
        return getLevelFromNr(level).exp;
    }

    public float getExpBar(int exp) {
        int level = getLevel(exp);
        if (exp <= 0 || level >= 50) {
            return 0f;
        }
        int levelExp = getExp(level);
        return ((float) (exp - levelExp) / (getExp(level + 1) - levelExp));
    }

    public int getExpNeeded(int level, int exp) {
        return getLevelFromNr(level).exp - exp;
    }

    public int getLevel(int exp) {
        List<Level> list = getLevelCollection();
        for (Level level : list) {
            if (level.exp <= exp && (list.size() <= list.indexOf(level) + 1 || list.get(list.indexOf(level) + 1).exp > exp)) {
                return level.level;
            }
        }
        return 0;
    }

    private Level getLevelFromNr(int levelNr) {
        for (Level level : getLevelCollection()) {
            if (level.level == levelNr) {
                return level;
            }
        }
        return null;
    }

    private List<Level> getLevelCollection() {
        List<Level> list = new ArrayList<>();
        for (String level : getConfigurationSection("level").getKeys(false)) {
            String unlocks = getString("level." + level + ".unlocks");
            list.add(new Level(Integer.parseInt(level), getInt("level." + level + ".exp"), unlocks != null ? unlocks.split(", ") : new String[0]));
        }
        return list;
    }

    public int getLevelUnlocked(String weaponId) {
        for (Level level : getLevelCollection()) {
            for (String unlock : level.unlocks) {
                if (unlock.equals(weaponId)) {
                    return level.level;
                }
            }
        }
        return -1;
    }

    public boolean isUnlockable(String weaponId) {
        return getLevelUnlocked(weaponId) > -1;
    }

    private class Level {

        private int exp;
        private int level;
        private String[] unlocks;

        private Level(int level, int exp, String... unlocks) {
            this.level = level;
            this.exp = exp;
            this.unlocks = unlocks;
        }
    }
}
