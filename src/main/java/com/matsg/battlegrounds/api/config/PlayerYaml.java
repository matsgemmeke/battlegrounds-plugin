package com.matsg.battlegrounds.api.config;

import com.matsg.battlegrounds.api.item.Loadout;

import java.util.Collection;
import java.util.UUID;

public interface PlayerYaml extends Yaml {

    void createDefaultAttributes();

    Collection<Loadout> getLoadouts();

    StoredPlayer getStoredPlayer();

    void saveLoadout(int loadoutNumber, Loadout loadout);

    void updateName(String name);
}