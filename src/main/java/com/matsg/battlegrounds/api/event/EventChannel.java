package com.matsg.battlegrounds.api.event;

import org.bukkit.event.Event;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class EventChannel<T extends Event> {

    private Deque<EventHandler<T>> eventHandlers;

    public EventChannel() {
        this.eventHandlers = new ArrayDeque<>();
    }

    public EventChannel(EventHandler<T>... eventConsumers) {
        this.eventHandlers = new ArrayDeque<>(Arrays.asList(eventConsumers));
    }

    public boolean addEventHandler(EventHandler<T> eventHandler) {
        return eventHandlers.add(eventHandler);
    }

    public void handleEvent(T event) {
        for (EventHandler<T> eventHandler : eventHandlers) {
            eventHandler.handle(event);
        }
    }

    public boolean removeEventHandler(EventHandler<T> eventHandler) {
        return eventHandlers.remove(eventHandler);
    }
}
