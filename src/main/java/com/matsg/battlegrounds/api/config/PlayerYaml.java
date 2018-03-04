package com.matsg.battlegrounds.api.config;

import com.matsg.battlegrounds.api.item.LoadoutClass;

import java.util.Collection;

public interface PlayerYaml extends Yaml {

    Collection<LoadoutClass> getLoadoutClasses();

    StoredPlayer getStoredPlayer();

    void saveLoadoutClass(int classNumber, LoadoutClass loadoutClass);
}