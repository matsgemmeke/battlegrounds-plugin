package com.matsg.battlegrounds.api.player;

import java.util.UUID;

public interface OfflineGamePlayer {

    int getDeaths();

    int getExp();

    int getHeadshots();

    int getKills();

    String getName();

    UUID getUUID();

    void setDeaths(int deaths);

    void setExp(int exp);

    void setHeadshots(int headshots);

    void setKills(int kills);
}