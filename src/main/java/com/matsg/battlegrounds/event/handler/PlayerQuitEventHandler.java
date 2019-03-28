package com.matsg.battlegrounds.event.handler;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.event.EventHandler;
import com.matsg.battlegrounds.api.game.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitEventHandler implements EventHandler<PlayerQuitEvent> {

    private Battlegrounds plugin;

    public PlayerQuitEventHandler(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @Override
    public void handle(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Game game = plugin.getGameManager().getGame(player);

        if (game == null) {
            return;
        }

        game.getPlayerManager().removePlayer(game.getPlayerManager().getGamePlayer(player));
    }
}
