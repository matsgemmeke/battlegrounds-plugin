package com.matsg.battlegrounds.game.gamemode;

import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.player.GamePlayer;

public class FreeForAll extends AbstractGameMode {

    public FreeForAll(Game game) {
        super(game, "Free-for-all", "FFA");
    }

    public void spawnPlayers(GamePlayer... players) {

    }
}