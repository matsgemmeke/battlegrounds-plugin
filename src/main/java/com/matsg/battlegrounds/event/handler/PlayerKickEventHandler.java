package com.matsg.battlegrounds.event.handler;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.event.handler.EventHandler;
import com.matsg.battlegrounds.api.game.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerKickEvent;

public class PlayerKickEventHandler implements EventHandler<PlayerKickEvent> {

    private Battlegrounds plugin;

    public PlayerKickEventHandler(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @Override
    public void handle(PlayerKickEvent event) {
        Player player = event.getPlayer();
        Game game = plugin.getGameManager().getGame(player);

        if (game == null) {
            return;
        }

        game.getPlayerManager().removePlayer(game.getPlayerManager().getGamePlayer(player));
    }
}
