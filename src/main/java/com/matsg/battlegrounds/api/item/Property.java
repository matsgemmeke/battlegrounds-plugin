package com.matsg.battlegrounds.api.item;

import com.matsg.battlegrounds.api.entity.GamePlayer;

public interface Property {

    GamePlayer getGamePlayer();

    void setGamePlayer(GamePlayer gamePlayer);
}
