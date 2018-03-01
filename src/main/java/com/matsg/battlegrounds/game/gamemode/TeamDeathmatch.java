package com.matsg.battlegrounds.game.gamemode;

import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.GamePlayer;

public class TeamDeathmatch extends AbstractGameMode {

    public TeamDeathmatch(Game game) {
        super(game, "Team Deathmatch", "Team DM");
    }

    public void spawnPlayers(GamePlayer... players) {

    }
}