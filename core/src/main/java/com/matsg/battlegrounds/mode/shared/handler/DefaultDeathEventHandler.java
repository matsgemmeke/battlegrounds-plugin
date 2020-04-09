package com.matsg.battlegrounds.mode.shared.handler;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.entity.PlayerState;
import com.matsg.battlegrounds.api.event.EventHandler;
import com.matsg.battlegrounds.api.event.GamePlayerDeathEvent;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.GameMode;
import com.matsg.battlegrounds.api.item.Firearm;
import com.matsg.battlegrounds.api.item.Weapon;
import org.bukkit.Location;
import org.bukkit.entity.Item;

public class DefaultDeathEventHandler implements EventHandler<GamePlayerDeathEvent> {

    private Game game;
    private GameMode gameMode;

    public DefaultDeathEventHandler(Game game, GameMode gameMode) {
        this.game = game;
        this.gameMode = gameMode;
    }

    public void handle(GamePlayerDeathEvent event) {
        if (event.getGame() == game || !gameMode.isActive()) {
            return;
        }

        GamePlayer gamePlayer = event.getGamePlayer();
        gamePlayer.setDeaths(gamePlayer.getDeaths() + 1);
        gamePlayer.setLives(gamePlayer.getLives() - 1);

        if (gamePlayer.getLives() <= 0) {
            gamePlayer.setState(PlayerState.SPECTATING);
            gamePlayer.getState().apply(game, gamePlayer);
        }

        Weapon weapon = gamePlayer.getLoadout().getWeapon(gamePlayer.getPlayer().getItemInHand());

        if (weapon instanceof Firearm) {
            Location location = gamePlayer.getLocation();
            Item item = location.getWorld().dropItem(location, weapon.getItemStack());
            ((Firearm) weapon).onDrop(gamePlayer, item);
        }
    }
}
