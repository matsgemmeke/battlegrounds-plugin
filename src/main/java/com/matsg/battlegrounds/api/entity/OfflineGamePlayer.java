package com.matsg.battlegrounds.api.entity;

import java.util.UUID;

public interface OfflineGamePlayer {

    int getDeaths();

    void setDeaths(int deaths);

    int getExp();

    void setExp(int exp);

    int getHeadshots();

    void setHeadshots(int headshots);

    int getKills();

    void setKills(int kills);

    String getName();

    UUID getUUID();

    boolean isOnline();

    int addExp(int exp);
}
