package com.matsg.battlegrounds.api.config;

import com.matsg.battlegrounds.api.item.Loadout;

import java.util.Collection;

public interface PlayerYaml extends Yaml {

    void createDefaultAttributes();

    Loadout getLoadout(int loadoutId);

    Collection<Loadout> getLoadouts();

    StoredPlayer getStoredPlayer();

    void saveLoadout(Loadout loadout);

    void updateName(String name);
}