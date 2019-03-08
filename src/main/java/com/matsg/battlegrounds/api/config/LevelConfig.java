package com.matsg.battlegrounds.api.config;

public interface LevelConfig extends Yaml {

    int getExp(int level);

    float getExpBar(int exp);

    int getExpNeeded(int level, int exp);

    int getLevel(int exp);

    int getLevelUnlocked(String object);
}
