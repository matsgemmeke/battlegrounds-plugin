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

    public EventChannel(EventHandler<T>... eventHandlers) {
        this.eventHandlers = new ArrayDeque<>(Arrays.asList(eventHandlers));
    }

    /**
     * Adds all event handlers of another event channel, combining the two.
     *
     * @param eventChannel the event channel to add
     */
    public void addEventChannel(EventChannel<T> eventChannel) {
        eventHandlers.addAll(eventChannel.eventHandlers);
    }

    /**
     * Adds an event handler to the channel.
     *
     * @param eventHandler the event handler to add
     */
    public void addEventHandler(EventHandler<T> eventHandler) {
        eventHandlers.add(eventHandler);
    }

    /**
     * Gets the amount of handlers in the channel.
     *
     * @return the event handler count
     */
    public int getEventHandlerCount() {
        return eventHandlers.size();
    }

    /**
     * Passes an event to all event handlers.
     *
     * @param event the event to handle
     */
    public void handleEvent(T event) {
        for (EventHandler<T> eventHandler : eventHandlers) {
            eventHandler.handle(event);
        }
    }

    /**
     * Removes an event handler from the channel.
     *
     * @param eventHandler the event handler to remove
     */
    public void removeEventHandler(EventHandler<T> eventHandler) {
        eventHandlers.remove(eventHandler);
    }
}
