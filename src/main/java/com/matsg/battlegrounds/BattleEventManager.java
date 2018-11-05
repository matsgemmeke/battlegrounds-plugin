package com.matsg.battlegrounds;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.EventManager;
import com.matsg.battlegrounds.api.event.handler.EventHandler;
import com.matsg.battlegrounds.event.handler.*;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;

import java.util.HashMap;
import java.util.Map;

public class BattleEventManager implements EventManager {

    private Map<Class<? extends Event>, EventHandler> handlers;

    public BattleEventManager(Battlegrounds plugin) {
        this.handlers = new HashMap<>();

        addEventHandler(AsyncPlayerChatEvent.class, new AsyncPlayerChatEventHandler(plugin));
        addEventHandler(BlockBreakEvent.class, new BlockBreakEventHandler(plugin));
        addEventHandler(BlockPlaceEvent.class, new BlockPlaceEventHandler(plugin));
        addEventHandler(BlockPhysicsEvent.class, new BlockPhysicsEventHandler(plugin));
        addEventHandler(EntityDamageByEntityEvent.class, new EntityDamageByEntityEventHandler(plugin));
        addEventHandler(InventoryClickEvent.class, new InventoryClickEventHandler());
        addEventHandler(InventoryCloseEvent.class, new InventoryCloseEventHandler(plugin));
        addEventHandler(FoodLevelChangeEvent.class, new FoodLevelChangeEventHandler(plugin));
        addEventHandler(PlayerCommandPreprocessEvent.class, new PlayerCommandPreprocessEventHandler(plugin));
        addEventHandler(PlayerDeathEvent.class, new PlayerDeathEventHandler(plugin));
        addEventHandler(PlayerDropItemEvent.class, new PlayerDropItemEventHandler(plugin));
        addEventHandler(PlayerInteractEvent.class, new PlayerInteractEventHandler(plugin));
        addEventHandler(PlayerItemHeldEvent.class, new PlayerItemHeldEventHandler(plugin));
        addEventHandler(PlayerJoinEvent.class, new PlayerJoinEventHandler(plugin));
        addEventHandler(PlayerKickEvent.class, new PlayerKickEventHandler(plugin));
        addEventHandler(PlayerMoveEvent.class, new PlayerMoveEventHandler(plugin));
        addEventHandler(PlayerPickupItemEvent.class, new PlayerPickupItemEventHandler(plugin));
        addEventHandler(PlayerQuitEvent.class, new PlayerQuitEventHandler(plugin));
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
            return;
        }
        handlers.get(event.getClass()).handle(event);
    }

    public void removeEventHandler(Class<? extends Event> eventClass) {
        handlers.remove(eventClass);
    }
}