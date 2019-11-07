package com.matsg.battlegrounds.api.event;

import com.matsg.battlegrounds.api.game.Game;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameStartEvent extends Event {

    private static final HandlerList handlerList = new HandlerList();
    private Game game;

    public GameStartEvent(Game game) {
        this.game = game;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    /**
     * Gets the game that started.
     *
     * @return the game that started
     */
    public Game getGame() {
        return game;
    }

    public HandlerList getHandlers() {
        return handlerList;
    }
}
