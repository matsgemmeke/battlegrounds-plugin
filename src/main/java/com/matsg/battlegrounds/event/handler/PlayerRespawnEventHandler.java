package com.matsg.battlegrounds.event.handler;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.event.handler.EventHandler;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.Spawn;
import com.matsg.battlegrounds.api.player.GamePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnEventHandler implements EventHandler<PlayerRespawnEvent> {

    private Battlegrounds plugin;

    public PlayerRespawnEventHandler(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    public void handle(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        Game game = plugin.getGameManager().getGame(player);

        if (game == null) {
            return;
        }

        GamePlayer gamePlayer = game.getPlayerManager().getGamePlayer(player);
        Spawn spawn = game.getGameMode().getRespawnPoint(gamePlayer);

        game.getPlayerManager().respawnPlayer(gamePlayer, spawn);
        event.setRespawnLocation(spawn.getLocation());
    }
}
