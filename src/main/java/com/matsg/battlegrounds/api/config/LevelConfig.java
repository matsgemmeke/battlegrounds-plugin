package com.matsg.battlegrounds.api.config;

public interface LevelConfig extends Yaml {

    int getLevel(int exp);
}