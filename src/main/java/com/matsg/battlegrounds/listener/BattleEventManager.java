package com.matsg.battlegrounds.listener;

import com.matsg.battlegrounds.api.EventManager;
import com.matsg.battlegrounds.api.game.EventHandler;
import org.bukkit.event.Event;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class BattleEventManager implements EventManager {

    private List<EventHandler> eventHandlers;

    public BattleEventManager() {
        this.eventHandlers = new ArrayList<>();
    }

    public void callEvent(Event event) {
        for (EventHandler eventHandler : eventHandlers) {
            executeEvent(event, eventHandler);
        }
    }

    public void deregisterEventHandler(EventHandler eventHandler) {
        eventHandlers.remove(eventHandler);
    }

    private void executeEvent(Event event, EventHandler handler) {
        try {
            for (Method method : handler.getClass().getDeclaredMethods()) {
                for (Parameter parameter : method.getParameters()) {
                    if (parameter.getType() == event.getClass()) {
                        method.invoke(handler, event);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registerEventHandler(EventHandler eventHandler) {
        eventHandlers.add(eventHandler);
    }
}