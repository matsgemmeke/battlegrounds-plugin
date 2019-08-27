package com.matsg.battlegrounds.api.event;

import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.GameState;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameStateChangeEvent extends Event {

    private static final HandlerList handlerList = new HandlerList();
    private Game game;
    private GameState newState, oldState;

    public GameStateChangeEvent(Game game, GameState oldState, GameState newState) {
        this.game = game;
        this.oldState = oldState;
        this.newState = newState;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    /**
     * Gets the game whose state has changed.
     *
     * @return the game whose state has changed
     */
    public Game getGame() {
        return game;
    }

    public HandlerList getHandlers() {
        return handlerList;
    }

    /**
     * Gets the new state of the game.
     *
     * @return the new state
     */
    public GameState getNewState() {
        return newState;
    }

    /**
     * Gets the old state of the game.
     *
     * @return the old state
     */
    public GameState getOldState() {
        return oldState;
    }
}
