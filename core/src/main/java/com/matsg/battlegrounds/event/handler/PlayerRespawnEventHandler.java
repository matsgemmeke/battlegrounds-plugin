package com.matsg.battlegrounds.event.handler;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.event.EventHandler;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.GameMode;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import org.bukkit.Location;
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

        GameMode gameMode = game.getGameMode();
        GamePlayer gamePlayer = game.getPlayerManager().getGamePlayer(player);
        Location respawnLocation = gameMode.getRespawnLocation(gamePlayer);

        gameMode.respawnPlayer(gamePlayer);

        event.setRespawnLocation(respawnLocation);
    }
}
