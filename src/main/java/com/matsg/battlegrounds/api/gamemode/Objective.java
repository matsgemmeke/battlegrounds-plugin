package com.matsg.battlegrounds.api.gamemode;

import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.nms.Title;

public interface Objective {

    String getId();

    Title getTitle();

    boolean isReached(Game game);
}