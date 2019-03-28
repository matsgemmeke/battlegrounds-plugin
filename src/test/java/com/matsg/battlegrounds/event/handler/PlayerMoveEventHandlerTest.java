package com.matsg.battlegrounds.event.handler;

import com.matsg.battlegrounds.BattleGameManager;
import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.Translator;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.GameManager;
import com.matsg.battlegrounds.api.Version;
import com.matsg.battlegrounds.api.game.*;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.game.ArenaSpawn;
import com.matsg.battlegrounds.game.BattleArena;
import com.matsg.battlegrounds.game.BattleTeam;
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

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
        ActionBar.class,
        BattlegroundsPlugin.class,
        ChatColor.class,
        ReflectionUtils.class,
        Translator.class
})
public class PlayerMoveEventHandlerTest {

    private Arena arena;
    private Battlegrounds plugin;
    private Game game;
    private GameManager gameManager;
    private GamePlayer gamePlayer;
    private Player player;
    private PlayerManager playerManager;
    private PlayerMoveEvent event;
    private World world;

    @Before
    public void setUp() {
        this.plugin = mock(Battlegrounds.class);
        this.game = mock(Game.class);
        this.gamePlayer = mock(GamePlayer.class);
        this.player = mock(Player.class);
        this.playerManager = mock(PlayerManager.class);
        this.world = mock(World.class);

        this.arena = new BattleArena("Arena", new Location(world, 100, 100, 100), new Location(world, 0, 0, 0), world);
        this.event = new PlayerMoveEvent(player, new Location(world, 0, 0, 0), new Location(world, 1, 1, 1));
        this.gameManager = new BattleGameManager();

        gameManager.getGames().add(game);

        PowerMockito.mockStatic(BattlegroundsPlugin.class);
        PowerMockito.mockStatic(ChatColor.class);
        PowerMockito.mockStatic(ReflectionUtils.class);
        PowerMockito.mockStatic(Translator.class);

        when(BattlegroundsPlugin.getPlugin()).thenReturn(plugin);
        when(ReflectionUtils.getEnumVersion()).thenReturn(null);

        when(game.getArena()).thenReturn(arena);
        when(game.getPlayerManager()).thenReturn(playerManager);
        when(playerManager.getGamePlayer(player)).thenReturn(gamePlayer);
        when(plugin.getGameManager()).thenReturn(gameManager);
    }

    @Test
    public void testPlayerMoveWhenNotPlaying() {
        when(playerManager.getGamePlayer(player)).thenReturn(null);

        PlayerMoveEventHandler eventHandler = new PlayerMoveEventHandler(plugin);
        eventHandler.handle(event);

        verify(player, times(0)).teleport(any(Location.class));

        assertFalse(event.isCancelled());
    }

    @Test
    public void testPlayerMoveInArenaWhileInGame() {
        when(game.getState()).thenReturn(GameState.IN_GAME);

        PlayerMoveEventHandler eventHandler = new PlayerMoveEventHandler(plugin);
        eventHandler.handle(event);

        verify(player, times(0)).teleport(any(Location.class));
        verify(playerManager, times(1)).getGamePlayer(player);

        assertFalse(event.isCancelled());
    }

    @Test
    public void testPlayerMoveOutsideArenaWhileInGame() {
        event.setTo(new Location(world, 1000, 1000, 1000));

        Version version = mock(Version.class);

        when(game.getState()).thenReturn(GameState.IN_GAME);
        when(plugin.getVersion()).thenReturn(version);

        PowerMockito.mockStatic(ActionBar.class);

        PlayerMoveEventHandler eventHandler = new PlayerMoveEventHandler(plugin);
        eventHandler.handle(event);

        verify(player, times(1)).teleport(any(Location.class));
        verify(version, times(1)).sendActionBar(any(Player.class), anyString());

        assertFalse(event.isCancelled());
    }

    @Test
    public void testPlayerMoveInArenaWhileCountdown() {
        Spawn spawn = new ArenaSpawn(1, new Location(world, 50, 50, 50), 1);
        spawn.setGamePlayer(gamePlayer);

        arena.getSpawns().add(spawn);

        when(game.getState()).thenReturn(GameState.STARTING);

        PlayerMoveEventHandler eventHandler = new PlayerMoveEventHandler(plugin);
        eventHandler.handle(event);

        verify(player, times(1)).teleport(spawn.getLocation());
        verify(playerManager, times(2)).getGamePlayer(player);

        assertFalse(event.isCancelled());
    }

    @Test
    public void testPlayerMoveInArenaWhileCountdownNoSpawn() {
        Team team = new BattleTeam(1, "Team", null, null);

        when(game.getState()).thenReturn(GameState.STARTING);
        when(gamePlayer.getTeam()).thenReturn(team);

        PlayerMoveEventHandler eventHandler = new PlayerMoveEventHandler(plugin);
        eventHandler.handle(event);

        verify(player, times(0)).teleport(any(Location.class));

        assertFalse(event.isCancelled());
    }
}
