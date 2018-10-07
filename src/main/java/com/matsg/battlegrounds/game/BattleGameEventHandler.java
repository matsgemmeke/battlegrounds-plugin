package com.matsg.battlegrounds.game;

import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.GameEventHandler;
import com.matsg.battlegrounds.api.event.handler.EventHandler;
import com.matsg.battlegrounds.event.handler.*;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

import java.util.HashMap;
import java.util.Map;

public class BattleGameEventHandler implements GameEventHandler {

    private Map<Class<? extends Event>, EventHandler> handlers;

    public BattleGameEventHandler(Game game) {
        this.handlers = new HashMap<>();

        addEventHandler(AsyncPlayerChatEvent.class, new AsyncPlayerChatEventHandler(game));
        addEventHandler(EntityDamageByEntityEvent.class, new EntityDamageByEntityEventHandler(game));
        addEventHandler(PlayerDeathEvent.class, new PlayerDeathEventHandler(game));
        addEventHandler(PlayerDropItemEvent.class, new PlayerDropItemEventHandler(game));
        addEventHandler(PlayerInteractEvent.class, new PlayerInteractEventHandler(game));
        addEventHandler(PlayerItemHeldEvent.class, new PlayerItemHeldEventHandler(game));
        addEventHandler(PlayerMoveEvent.class, new PlayerMoveEventHandler(game));
        addEventHandler(PlayerPickupItemEvent.class, new PlayerPickupItemEventHandler(game));
        addEventHandler(PlayerCommandPreprocessEvent.class, new PlayerCommandPreprocessEventHandler());
        addEventHandler(PlayerRespawnEvent.class, new PlayerRespawnEventHandler(game));
    }

    public void addEventHandler(Class<? extends Event> eventClass, EventHandler eventHandler) {
        if (handlers.containsKey(eventClass)) {
            throw new EventHandlingException("Event class " + eventClass.getSimpleName() + " already registered");
        }
        handlers.put(eventClass, eventHandler);
    }

    public boolean handleEvent(Event event) {
        if (!handlers.containsKey(event.getClass())) {
            throw new EventHandlingException("No handler for event class " + event.getClass().getSimpleName());
        }
        return handlers.get(event.getClass()).handle(event);
    }

    public void removeEventHandler(Class<? extends Event> eventClass) {
        handlers.remove(eventClass);
    }
}
