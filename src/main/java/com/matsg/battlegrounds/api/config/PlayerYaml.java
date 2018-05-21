package com.matsg.battlegrounds.api.config;

import com.matsg.battlegrounds.api.item.Loadout;

import java.util.Collection;

public interface PlayerYaml extends Yaml {

    Collection<Loadout> getLoadouts();

    StoredPlayer getStoredPlayer();

    void saveLoadout(int classNumber, Loadout loadout);
}