package com.matsg.battlegrounds.event.handler;

import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.event.GamePlayerDeathEvent;
import com.matsg.battlegrounds.api.event.GamePlayerDeathEvent.DeathCause;
import com.matsg.battlegrounds.api.event.handler.EventHandler;
import com.matsg.battlegrounds.api.game.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathEventHandler implements EventHandler<PlayerDeathEvent> {

    private Battlegrounds plugin;
    private Game game;

    public PlayerDeathEventHandler(Game game) {
        this.game = game;
        this.plugin = BattlegroundsPlugin.getPlugin();
    }

    public boolean handle(PlayerDeathEvent event) {
        Player player = event.getEntity();

        event.setDeathMessage(null);
        event.setDroppedExp(0);
        event.setKeepInventory(true);
        event.setKeepLevel(true);

        DeathCause deathCause = player.getLastDamageCause() != null ? DeathCause.fromDamageCause(player.getLastDamageCause().getCause()) : null;

        if (deathCause == null) {
            return false; // Only notify the game of death events the game should handle
        }

        plugin.getServer().getPluginManager().callEvent(new GamePlayerDeathEvent(game, game.getPlayerManager().getGamePlayer(player), deathCause));
        return false;
    }
}