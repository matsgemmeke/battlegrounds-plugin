package com.matsg.battlegrounds.api.gamemode;

import com.matsg.battlegrounds.api.game.GameState;

public interface StateListener {

    void onStateChange(GameState state);
}
