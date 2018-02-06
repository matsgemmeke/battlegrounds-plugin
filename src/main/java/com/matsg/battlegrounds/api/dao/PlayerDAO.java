package com.matsg.battlegrounds.api.dao;

import java.util.UUID;

public interface PlayerDAO extends Comparable<PlayerDAO> {

    int get(String paramString);

    int getBestKills();

    int getBestRound();

    int getDowns();

    int getHeadshots();

    int getKills();

    String getName();

    int getRevives();

    UUID getUUID();
}