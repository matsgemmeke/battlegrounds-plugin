package com.matsg.battlegrounds.api.config;

public interface LevelConfig extends Yaml {

    int getExpNeeded(int level, int exp);

    int getLevel(int exp);
}