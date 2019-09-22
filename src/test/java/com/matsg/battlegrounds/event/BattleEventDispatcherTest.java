package com.matsg.battlegrounds.event;

import com.matsg.battlegrounds.api.event.EventChannel;
import com.matsg.battlegrounds.api.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BattleEventDispatcherTest {

    private PluginManager pluginManager;

    @Before
    public void setUp() {
        this.pluginManager = mock(PluginManager.class);
    }

    @Test
    public void registerNewEventChannelToDispatcher() {
        EventChannel<PlayerJoinEvent> eventChannel = new EventChannel<>();

        BattleEventDispatcher eventDispatcher = new BattleEventDispatcher(pluginManager);
        eventDispatcher.registerEventChannel(PlayerJoinEvent.class, eventChannel);

        assertEquals(1, eventDispatcher.getEventChannelCount());
    }

    @Test
    public void registerExistingEventChannelToDispatcher() {
        EventHandler<PlayerJoinEvent> eventHandler = mock(EventHandler.class);
        EventChannel<PlayerJoinEvent> eventChannelOne = new EventChannel<>(eventHandler);
        EventChannel<PlayerJoinEvent> eventChannelTwo = new EventChannel<>(eventHandler);

        BattleEventDispatcher eventDispatcher = new BattleEventDispatcher(pluginManager);
        eventDispatcher.registerEventChannel(PlayerJoinEvent.class, eventChannelOne);
        eventDispatcher.registerEventChannel(PlayerJoinEvent.class, eventChannelTwo);

        assertEquals(1, eventDispatcher.getEventChannelCount());
        assertEquals(2, eventChannelOne.getEventHandlerCount());
    }

    @Test
    public void unregisterExistingEventChannel() {
        EventHandler<PlayerJoinEvent> eventHandler = mock(EventHandler.class);
        EventChannel<PlayerJoinEvent> eventChannel = new EventChannel<>(eventHandler);

        BattleEventDispatcher eventDispatcher = new BattleEventDispatcher(pluginManager);
        eventDispatcher.registerEventChannel(PlayerJoinEvent.class, eventChannel);
        eventDispatcher.unregisterEventChannel(PlayerJoinEvent.class);

        assertEquals(0, eventDispatcher.getEventChannelCount());
    }

    @Test
    public void unregisterNonexistingEventChannel() {
        EventHandler<PlayerJoinEvent> eventHandler = mock(EventHandler.class);
        EventChannel<PlayerJoinEvent> eventChannel = new EventChannel<>(eventHandler);

        BattleEventDispatcher eventDispatcher = new BattleEventDispatcher(pluginManager);
        eventDispatcher.registerEventChannel(PlayerJoinEvent.class, eventChannel);
        eventDispatcher.unregisterEventChannel(PlayerQuitEvent.class);

        assertEquals(1, eventDispatcher.getEventChannelCount());
    }

    @Test
    public void dispatchInternalEventCallsChannels() {
        EventHandler<PlayerJoinEvent> eventHandler = mock(EventHandler.class);
        EventChannel<PlayerJoinEvent> eventChannel = new EventChannel<>(eventHandler);
        PlayerJoinEvent event = new PlayerJoinEvent(null, null);

        BattleEventDispatcher eventDispatcher = new BattleEventDispatcher(pluginManager);
        eventDispatcher.registerEventChannel(PlayerJoinEvent.class, eventChannel);

        eventDispatcher.dispatchInternalEvent(event);

        verify(eventHandler, times(1)).handle(event);
    }

    @Test
    public void dispatchInternalEventDoesNotCallUnregisteredChannels() {
        EventHandler<PlayerJoinEvent> eventHandler = mock(EventHandler.class);
        EventChannel<PlayerJoinEvent> eventChannel = new EventChannel<>(eventHandler);
        PlayerQuitEvent event = new PlayerQuitEvent(null, null);

        BattleEventDispatcher eventDispatcher = new BattleEventDispatcher(pluginManager);
        eventDispatcher.registerEventChannel(PlayerJoinEvent.class, eventChannel);

        eventDispatcher.dispatchInternalEvent(event);

        verify(eventHandler, times(0)).handle(any());
    }

    @Test
    public void dispatchExternalEventCallsChannelsAndPluginManager() {
        EventHandler<PlayerJoinEvent> eventHandler = mock(EventHandler.class);
        EventChannel<PlayerJoinEvent> eventChannel = new EventChannel<>(eventHandler);
        PlayerJoinEvent event = new PlayerJoinEvent(null, null);

        BattleEventDispatcher eventDispatcher = new BattleEventDispatcher(pluginManager);
        eventDispatcher.registerEventChannel(PlayerJoinEvent.class, eventChannel);

        eventDispatcher.dispatchExternalEvent(event);

        verify(eventHandler, times(1)).handle(event);
        verify(pluginManager, times(1)).callEvent(event);
    }
}
