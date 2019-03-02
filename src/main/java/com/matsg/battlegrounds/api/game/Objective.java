package com.matsg.battlegrounds.api.game;

import com.matsg.battlegrounds.nms.Title;

public interface Objective {

    String getId();

    Title getTitle();

    boolean isReached(Game game);
}
