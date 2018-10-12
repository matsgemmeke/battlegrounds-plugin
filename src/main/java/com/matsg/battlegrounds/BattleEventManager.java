package com.matsg.battlegrounds;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.EventManager;
import com.matsg.battlegrounds.api.event.handler.EventHandler;
import com.matsg.battlegrounds.event.handler.*;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

import java.util.HashMap;
import java.util.Map;

public class BattleEventManager implements EventManager {

    private Map<Class<? extends Event>, EventHandler> handlers;

    public BattleEventManager(Battlegrounds plugin) {
        this.handlers = new HashMap<>();

        addEventHandler(AsyncPlayerChatEvent.class, new AsyncPlayerChatEventHandler(plugin));
        addEventHandler(EntityDamageByEntityEvent.class, new EntityDamageByEntityEventHandler(plugin));
        addEventHandler(FoodLevelChangeEvent.class, new FoodLevelChangeEventHandler(plugin));
        addEventHandler(PlayerCommandPreprocessEvent.class, new PlayerCommandPreprocessEventHandler());
        addEventHandler(PlayerDeathEvent.class, new PlayerDeathEventHandler(plugin));
        addEventHandler(PlayerDropItemEvent.class, new PlayerDropItemEventHandler(plugin));
        addEventHandler(PlayerInteractEvent.class, new PlayerInteractEventHandler(plugin));
        addEventHandler(PlayerItemHeldEvent.class, new PlayerItemHeldEventHandler(plugin));
        addEventHandler(PlayerMoveEvent.class, new PlayerMoveEventHandler(plugin));
        addEventHandler(PlayerPickupItemEvent.class, new PlayerPickupItemEventHandler(plugin));
        addEventHandler(PlayerRespawnEvent.class, new PlayerRespawnEventHandler(plugin));
    }

    public void addEventHandler(Class<? extends Event> eventClass, EventHandler eventHandler) {
        if (handlers.containsKey(eventClass)) {
            throw new EventHandlingException("Event class " + eventClass.getSimpleName() + " already registered");
        }
        handlers.put(eventClass, eventHandler);
    }

    public void handleEvent(Event event) {
        if (!handlers.containsKey(event.getClass())) {
            throw new EventHandlingException("No handler for event class " + event.getClass().getSimpleName());
        }
        handlers.get(event.getClass()).handle(event);
    }

    public void removeEventHandler(Class<? extends Event> eventClass) {
        handlers.remove(eventClass);
    }
}