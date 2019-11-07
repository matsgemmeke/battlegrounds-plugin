package com.matsg.battlegrounds.api.event;

import com.matsg.battlegrounds.api.entity.BattleEntity;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.entity.Hitbox;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GamePlayerKillEntityEvent extends Event {

    private static final HandlerList handlerList = new HandlerList();
    private BattleEntity entity;
    private Game game;
    private GamePlayer gamePlayer;
    private Hitbox hitbox;
    private int points;
    private Weapon weapon;

    public GamePlayerKillEntityEvent(Game game, GamePlayer gamePlayer, BattleEntity entity, Weapon weapon, Hitbox hitbox, int points) {
        this.game = game;
        this.gamePlayer = gamePlayer;
        this.entity = entity;
        this.weapon = weapon;
        this.hitbox = hitbox;
        this.points = points;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    /**
     * Gets the entity that was killed.
     *
     * @return the entity
     */
    public BattleEntity getEntity() {
        return entity;
    }

    /**
     * Gets the game in which the kill took place.
     *
     * @return the game
     */
    public Game getGame() {
        return game;
    }

    /**
     * Gets the player who killed the entity.
     *
     * @return the player
     */
    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public HandlerList getHandlers() {
        return handlerList;
    }

    /**
     * Gets the hitbox on which the kill was inflicted on.
     *
     * @return the hitbox
     */
    public Hitbox getHitbox() {
        return hitbox;
    }

    /**
     * Gets the amount of points gained by the player.
     *
     * @return the amount of points
     */
    public int getPoints() {
        return points;
    }

    /**
     * Gets the amount of points gained by the player.
     *
     * @param points the amount of points
     */
    public void setPoints(int points) {
        this.points = points;
    }

    /**
     * Gets the weapon which was used to kill the entity.
     *
     * @return the weapon
     */
    public Weapon getWeapon() {
        return weapon;
    }
}
