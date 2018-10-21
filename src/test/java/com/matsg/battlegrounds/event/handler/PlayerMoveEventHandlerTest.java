package com.matsg.battlegrounds.event.handler;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.GameManager;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.Version;
import com.matsg.battlegrounds.api.game.*;
import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.game.ArenaSpawn;
import com.matsg.battlegrounds.nms.ReflectionUtils;
import com.matsg.battlegrounds.util.ActionBar;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ ActionBar.class, BattlegroundsPlugin.class, ChatColor.class, PlayerMoveEvent.class, ReflectionUtils.class })
public class PlayerMoveEventHandlerTest {

    private Arena arena;
    private Battlegrounds plugin;
    private Game game;
    private GameManager gameManager;
    private GamePlayer gamePlayer;
    private Location location;
    private Player player;
    private PlayerManager playerManager;
    private PlayerMoveEvent event;

    @Before
    public void setUp() {
        this.arena = mock(Arena.class);
        this.plugin = mock(Battlegrounds.class);
        this.game = mock(Game.class);
        this.gameManager = mock(GameManager.class);
        this.gamePlayer = mock(GamePlayer.class);
        this.location = new Location(mock(World.class), 0, 0, 0);
        this.player = mock(Player.class);
        this.playerManager = mock(PlayerManager.class);
        this.event = PowerMockito.mock(PlayerMoveEvent.class);

        when(event.getFrom()).thenReturn(location);
        when(event.getTo()).thenReturn(location);
        when(event.getPlayer()).thenReturn(player);
        when(game.getArena()).thenReturn(arena);
        when(game.getPlayerManager()).thenReturn(playerManager);
        when(gameManager.getGame(player)).thenReturn(game);
        when(player.getLocation()).thenReturn(location);
        when(playerManager.getGamePlayer(player)).thenReturn(gamePlayer);
        when(plugin.getGameManager()).thenReturn(gameManager);
    }

    @Test
    public void testPlayerMoveWhileNotInGame() {
        when(gameManager.getGame(player)).thenReturn(null);

        PlayerMoveEventHandler eventHandler = new PlayerMoveEventHandler(plugin);
        eventHandler.handle(event);

        verify(game, times(0)).getArena();

        assertFalse(event.isCancelled());
    }

    @Test
    public void testPlayerMoveInArenaWhileInGame() {
        when(arena.contains(any(Location.class))).thenReturn(true);
        when(game.getState()).thenReturn(GameState.IN_GAME);

        PlayerMoveEventHandler eventHandler = new PlayerMoveEventHandler(plugin);
        eventHandler.handle(event);

        verify(arena, times(0)).getSpawn(gamePlayer);
    }

    @Test
    public void testPlayerMoveOutsideArenaWhileInGame() {
        Translator translator = mock(Translator.class);
        Version version = mock(Version.class);

        when(arena.contains(any(Location.class))).thenReturn(false);
        when(game.getState()).thenReturn(GameState.IN_GAME);
        when(plugin.getTranslator()).thenReturn(translator);
        when(plugin.getVersion()).thenReturn(version);

        PowerMockito.mockStatic(BattlegroundsPlugin.class);
        PowerMockito.mockStatic(ChatColor.class);
        PowerMockito.mockStatic(ReflectionUtils.class);

        when(BattlegroundsPlugin.getPlugin()).thenReturn(plugin);
        when(ReflectionUtils.getEnumVersion()).thenReturn(null);

        PowerMockito.mockStatic(ActionBar.class);

        PlayerMoveEventHandler eventHandler = new PlayerMoveEventHandler(plugin);
        eventHandler.handle(event);

        verify(player, times(1)).teleport(any(Location.class));
        verify(version, times(1)).sendActionBar(any(Player.class), anyString());

        assertFalse(event.isCancelled());
    }

    @Test
    public void testPlayerMoveInArenaWhileCountdown() {
        Spawn spawn = new ArenaSpawn(1, location, 1);

        when(arena.contains(any(Location.class))).thenReturn(true);
        when(arena.getSpawn(gamePlayer)).thenReturn(spawn);
        when(event.getTo()).thenReturn(location.clone().add(1, 0, 1));
        when(game.getState()).thenReturn(GameState.STARTING);

        PlayerMoveEventHandler eventHandler = new PlayerMoveEventHandler(plugin);
        eventHandler.handle(event);

        verify(arena, times(1)).getSpawn(gamePlayer);
        verify(player, times(1)).teleport(any(Location.class));
        verify(playerManager, times(1)).getGamePlayer(player);
    }

    @Test
    public void testPlayerMoveInArenaWhileCountdownNoSpawn() {
        when(arena.contains(any(Location.class))).thenReturn(true);
        when(event.getTo()).thenReturn(location.clone().add(1, 0, 1));
        when(game.getState()).thenReturn(GameState.STARTING);

        PlayerMoveEventHandler eventHandler = new PlayerMoveEventHandler(plugin);
        eventHandler.handle(event);

        verify(player, times(0)).teleport(any(Location.class));
    }
}
