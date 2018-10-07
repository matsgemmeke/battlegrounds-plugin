package com.matsg.battlegrounds.event.handler;

import com.matsg.battlegrounds.api.event.handler.EventHandler;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.Spawn;
import com.matsg.battlegrounds.api.player.GamePlayer;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnEventHandler implements EventHandler<PlayerRespawnEvent> {

    private Game game;

    public PlayerRespawnEventHandler(Game game) {
        this.game = game;
    }

    public boolean handle(PlayerRespawnEvent event) {
        GamePlayer gamePlayer = game.getPlayerManager().getGamePlayer(event.getPlayer());
        Spawn spawn = game.getGameMode().getRespawnPoint(gamePlayer);

        game.getPlayerManager().respawnPlayer(gamePlayer, spawn);
        event.setRespawnLocation(spawn.getLocation());
        return false;
    }
}