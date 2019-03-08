package com.matsg.battlegrounds.api.event;

import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.api.player.Hitbox;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GamePlayerKillPlayerEvent extends Event {

    private static final HandlerList handlerList = new HandlerList();
    private Game game;
    private GamePlayer gamePlayer, killer;
    private Hitbox hitbox;
    private Weapon weapon;

    public GamePlayerKillPlayerEvent(Game game, GamePlayer gamePlayer, GamePlayer killer, Weapon weapon, Hitbox hitbox) {
        this.game = game;
        this.gamePlayer = gamePlayer;
        this.hitbox = hitbox;
        this.killer = killer;
        this.weapon = weapon;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    public Game getGame() {
        return game;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public HandlerList getHandlers() {
        return handlerList;
    }

    public Hitbox getHitbox() {
        return hitbox;
    }

    public GamePlayer getKiller() {
        return killer;
    }

    public Weapon getWeapon() {
        return weapon;
    }
}
