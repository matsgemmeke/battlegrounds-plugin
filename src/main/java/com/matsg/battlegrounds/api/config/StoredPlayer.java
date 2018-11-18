package com.matsg.battlegrounds.api.config;

import com.matsg.battlegrounds.api.item.Loadout;
import com.matsg.battlegrounds.api.player.OfflineGamePlayer;

import java.util.Collection;

public interface StoredPlayer extends OfflineGamePlayer, Comparable<StoredPlayer> {

    int getAttribute(String attribute);

    void createDefaultAttributes();

    Loadout getLoadout(int loadoutNumber);

    Collection<Loadout> getLoadouts();

    void saveLoadout(int loadoutNumber, Loadout loadout);

    void updateName(String name);
}