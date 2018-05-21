package com.matsg.battlegrounds.api.event;

import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.util.EnumMessage;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class GamePlayerDeathEvent extends Event {

    private static final HandlerList handlerList = new HandlerList();
    private DeathCause deathCause;
    private Game game;
    private GamePlayer gamePlayer;

    public GamePlayerDeathEvent(Game game, GamePlayer gamePlayer, DeathCause deathCause) {
        this.game = game;
        this.gamePlayer = gamePlayer;
        this.deathCause = deathCause;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    public DeathCause getDeathCause() {
        return deathCause;
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

    public enum DeathCause {

        BURNING(1, EnumMessage.DEATH_BURNING.getMessage(), DamageCause.FIRE, DamageCause.FIRE_TICK, DamageCause.LAVA),
        DROWNING(2, EnumMessage.DEATH_DROWNING.getMessage(), DamageCause.DROWNING),
        FALLING(3, EnumMessage.DEATH_FALLING.getMessage(), DamageCause.FALL),
        PLAYER_KILL(4, EnumMessage.DEATH_PLAYER_KILL.getMessage(), DamageCause.ENTITY_ATTACK);

        private DamageCause[] damageCause;
        private int id;
        private String deathMessage;

        DeathCause(int id, String deathMessage, DamageCause... damageCause) {
            this.id = id;
            this.deathMessage = deathMessage;
            this.damageCause = damageCause;
        }

        public static DeathCause fromDamageCause(DamageCause damageCause) {
            for (DeathCause deathCause : values()) {
                for (DamageCause other : deathCause.damageCause) {
                    if (other == damageCause) {
                        return deathCause;
                    }
                }
            }
            return null;
        }

        public String getDeathMessage() {
            return deathMessage;
        }

        public int getId() {
            return id;
        }
    }
}