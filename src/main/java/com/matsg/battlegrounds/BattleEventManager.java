package com.matsg.battlegrounds;

import com.matsg.battlegrounds.api.EventManager;
import com.matsg.battlegrounds.api.event.handler.EventHandler;
import org.bukkit.event.Event;

import java.util.HashMap;
import java.util.Map;

public class BattleEventManager implements EventManager {

    private Map<Class<? extends Event>, EventHandler> handlers;

    public BattleEventManager() {
        this.handlers = new HashMap<>();
    }

    public void addEventHandler(Class<? extends Event> eventClass, EventHandler eventHandler) {
        if (handlers.containsKey(eventClass)) {
            throw new EventHandlingException("Event class " + eventClass.getSimpleName() + " already registered");
        }
        handlers.put(eventClass, eventHandler);
    }

    public void handleEvent(Event event) {
        if (!handlers.containsKey(event.getClass())) {
            return;
        }
        handlers.get(event.getClass()).handle(event);
    }

    public void removeEventHandler(Class<? extends Event> eventClass) {
        handlers.remove(eventClass);
    }
}
