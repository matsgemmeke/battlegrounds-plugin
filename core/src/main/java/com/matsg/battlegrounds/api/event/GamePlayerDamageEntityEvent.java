package com.matsg.battlegrounds.api.event;

import com.matsg.battlegrounds.api.entity.BattleEntity;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.entity.Hitbox;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.item.Weapon;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GamePlayerDamageEntityEvent extends Event {

    private static final HandlerList handlerList = new HandlerList();
    private BattleEntity entity;
    private double damage;
    private Game game;
    private GamePlayer gamePlayer;
    private Hitbox hitbox;
    private Weapon weapon;

    public GamePlayerDamageEntityEvent(Game game, GamePlayer gamePlayer, BattleEntity entity, Weapon weapon, double damage, Hitbox hitbox) {
        this.game = game;
        this.gamePlayer = gamePlayer;
        this.entity = entity;
        this.weapon = weapon;
        this.damage = damage;
        this.hitbox = hitbox;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    /**
     * Gets the amount of damage inflicted.
     *
     * @return the damage
     */
    public double getDamage() {
        return damage;
    }

    /**
     * Sets the amount of the damage inflicted.
     *
     * @param damage the damage
     */
    public void setDamage(double damage) {
        this.damage = damage;
    }

    /**
     * Gets the entity that was damaged.
     *
     * @return the entity
     */
    public BattleEntity getEntity() {
        return entity;
    }

    /**
     * Gets the game in which the damage event took place.
     *
     * @return the game
     */
    public Game getGame() {
        return game;
    }

    /**
     * Gets the player who damaged the entity.
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
     * Gets the hitbox on which the damage was infliced on.
     *
     * @return the hitbox
     */
    public Hitbox getHitbox() {
        return hitbox;
    }

    /**
     * Gets the weapon which was used to damage the entity.
     *
     * @return the weapon
     */
    public Weapon getWeapon() {
        return weapon;
    }
}
