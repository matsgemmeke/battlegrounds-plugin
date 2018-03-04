package com.matsg.battlegrounds.api.config;

import com.matsg.battlegrounds.api.item.Weapon;

public interface LevelConfig extends Yaml {

    int getExpNeeded(int level, int exp);

    int getLevel(int exp);

    int getLevelUnlocked(Weapon weapon);
}