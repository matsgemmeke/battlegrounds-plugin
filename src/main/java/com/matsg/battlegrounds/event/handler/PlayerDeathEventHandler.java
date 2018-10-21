package com.matsg.battlegrounds.event.handler;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.event.GamePlayerDeathEvent;
import com.matsg.battlegrounds.api.event.GamePlayerDeathEvent.DeathCause;
import com.matsg.battlegrounds.api.event.handler.EventHandler;
import com.matsg.battlegrounds.api.game.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathEventHandler implements EventHandler<PlayerDeathEvent> {

    private Battlegrounds plugin;

    public PlayerDeathEventHandler(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    public void handle(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Game game = plugin.getGameManager().getGame(player);

        if (game == null) {
            return;
        }

        event.setDeathMessage(null);
        event.setDroppedExp(0);
        event.setKeepInventory(true);
        event.setKeepLevel(true);

        DeathCause deathCause = player.getLastDamageCause() != null ? DeathCause.fromDamageCause(player.getLastDamageCause().getCause()) : null;

        if (deathCause == null) {
            return; // Only notify the game of death events the game should handle
        }

        plugin.getServer().getPluginManager().callEvent(new GamePlayerDeathEvent(game, game.getPlayerManager().getGamePlayer(player), deathCause));
        game.getGameMode().onDeath(game.getPlayerManager().getGamePlayer(player), DeathCause.SUICIDE);
    }
}
