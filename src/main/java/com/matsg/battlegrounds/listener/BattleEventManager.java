package com.matsg.battlegrounds.listener;

import com.matsg.battlegrounds.api.EventManager;
import com.matsg.battlegrounds.api.game.EventHandler;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.List;

public class BattleEventManager implements EventManager {

    private List<EventHandler> eventHandlers;

    public BattleEventManager() {
        this.eventHandlers = new ArrayList<>();
    }

    public void callEvent(Event event) {

    }

    public void registerEventHandler(EventHandler eventHandler) {
        eventHandlers.add(eventHandler);
    }
}