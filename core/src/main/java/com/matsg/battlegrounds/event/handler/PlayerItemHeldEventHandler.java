package com.matsg.battlegrounds.event.handler;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.event.EventHandler;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemHeldEvent;

public class PlayerItemHeldEventHandler implements EventHandler<PlayerItemHeldEvent> {

    private Battlegrounds plugin;

    public PlayerItemHeldEventHandler(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    public void handle(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        Game game = plugin.getGameManager().getGame(player);

        if (game == null) {
            return;
        }

        GamePlayer gamePlayer = game.getPlayerManager().getGamePlayer(player);

        if (gamePlayer.getLoadout() == null) {
            return;
        }

        Weapon weapon = gamePlayer.getLoadout().getWeapon(player.getItemInHand());

        if (weapon == null) {
            return;
        }

        weapon.onSwitch(gamePlayer, event);
    }
}
