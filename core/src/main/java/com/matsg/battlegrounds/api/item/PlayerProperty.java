package com.matsg.battlegrounds.api.item;

import com.matsg.battlegrounds.api.entity.GamePlayer;

public interface PlayerProperty {

    GamePlayer getGamePlayer();

    void setGamePlayer(GamePlayer gamePlayer);
}
