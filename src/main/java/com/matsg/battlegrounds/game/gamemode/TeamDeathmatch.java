package com.matsg.battlegrounds.game.gamemode;

import com.matsg.battlegrounds.api.config.Yaml;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.player.GamePlayer;

public class TeamDeathmatch extends AbstractGameMode {

    public TeamDeathmatch(Game game) {
        super(game, "Team Deathmatch", "Team DM", yaml);
    }

    public void addPlayer(GamePlayer gamePlayer) {

    }

    public void removePlayer(GamePlayer gamePlayer) {

    }

    public void spawnPlayers(GamePlayer... players) {

    }
}