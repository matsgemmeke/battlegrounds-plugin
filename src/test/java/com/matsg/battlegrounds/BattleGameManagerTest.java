package com.matsg.battlegrounds;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.PlayerManager;
import com.matsg.battlegrounds.api.player.GamePlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(PowerMockRunner.class)
@PrepareForTest(BattlegroundsPlugin.class)
public class BattleGameManagerTest {

    @Test
    public void testGameManagerFindArenaByLocation() {
        // Mock an arena object
        Arena arena = mock(Arena.class);
        Location locationOne = mock(Location.class), locationTwo = mock(Location.class);

        when(arena.contains(locationOne)).thenReturn(true);
        when(arena.contains(locationTwo)).thenReturn(false);

        List<Arena> list = new ArrayList<>();
        list.add(arena);

        // Mock a game object
        Game game = mock(Game.class);
        when(game.getArenaList()).thenReturn(list);

        // Instantiate the game manager
        BattleGameManager gameManager = new BattleGameManager();
        gameManager.getGames().add(game);

        assertNotNull(gameManager.getArena(locationOne));
        assertNull(gameManager.getArena(locationTwo));
        assertEquals(arena, gameManager.getArena(locationOne));
    }

    @Test
    public void testGameManagerFindArenaByName() {
        // Mock an arena object
        Arena arena = mock(Arena.class);
        when(arena.getName()).thenReturn("Test");

        List<Arena> list = new ArrayList<>();
        list.add(arena);

        // Mock a game object
        Game game = mock(Game.class);
        when(game.getArenaList()).thenReturn(list);

        // Instantiate the game manager
        BattleGameManager gameManager = new BattleGameManager();
        gameManager.getGames().add(game);

        assertEquals(arena, gameManager.getArena(game, "Test"));
        assertEquals(null, gameManager.getArena(game, "Fail"));
    }

    @Test
    public void testGameManagerFindGamesByArena() {
        // Mock an arena object
        Arena arenaOne = mock(Arena.class), arenaTwo = mock(Arena.class);

        List<Arena> list = new ArrayList<>();
        list.add(arenaOne);

        // Mock a game object
        Game game = mock(Game.class);
        when(game.getArenaList()).thenReturn(list);

        // Instantiate the game manager
        BattleGameManager gameManager = new BattleGameManager();
        gameManager.getGames().add(game);

        assertEquals(game, gameManager.getGame(arenaOne));
        assertEquals(null, gameManager.getGame(arenaTwo));
    }

    @Test
    public void testGameManagerFindGamesById() {
        // Game data
        int id = 1;

        // Mock a game object
        Game game = mock(Game.class);
        when(game.getId()).thenReturn(id);

        // Instantiate the game manager
        BattleGameManager gameManager = new BattleGameManager();
        gameManager.getGames().add(game);

        assertTrue(gameManager.exists(1));
        assertFalse(gameManager.exists(2));
        assertEquals(game, gameManager.getGame(1));
        assertEquals(null, gameManager.getGame(2));
    }

    @Test
    public void testGameManagerFindGamesByPlayer() {
        // Game data
        int id = 1;

        // Create mock objects
        GamePlayer gamePlayer = mock(GamePlayer.class);
        Player playerOne = mock(Player.class), playerTwo = mock(Player.class);
        PlayerManager playerManager = mock(PlayerManager.class);

        when(gamePlayer.getPlayer()).thenReturn(playerOne);
        when(playerManager.getGamePlayer(playerOne)).thenReturn(gamePlayer);

        // Mock a game object
        Game game = mock(Game.class);
        when(game.getId()).thenReturn(id);
        when(game.getPlayerManager()).thenReturn(playerManager);

        // Instantiate the game manager
        BattleGameManager gameManager = new BattleGameManager();
        gameManager.getGames().add(game);

        assertEquals(game, gameManager.getGame(playerOne));
        assertEquals(null, gameManager.getGame(playerTwo));
        assertEquals(gamePlayer, gameManager.getGamePlayer(playerOne));
        assertEquals(null, gameManager.getGamePlayer(playerTwo));
    }

    @Test
    public void testGameManagerGetAllArenas() {
        List<Arena> list = new ArrayList<>();

        // Create mock objects
        Arena arena = mock(Arena.class);
        Game game = mock(Game.class);

        when(game.getArenaList()).thenReturn(list);

        list.add(arena);

        // Instantiate the game manager
        BattleGameManager gameManager = new BattleGameManager();
        gameManager.getGames().add(game);

        assertEquals(1, gameManager.getArenaList().size());
        assertTrue(gameManager.getArenaList().contains(arena));
    }

    @Test
    public void testGameManagerGetAllPlayers() {
        List<GamePlayer> list = new ArrayList<>();

        // Create mock objects
        Game game = mock(Game.class);
        GamePlayer gamePlayer = mock(GamePlayer.class);
        PlayerManager playerManager = mock(PlayerManager.class);

        when(game.getPlayerManager()).thenReturn(playerManager);
        when(playerManager.getPlayers()).thenReturn(list);

        list.add(gamePlayer);

        // Instantiate the game manager
        BattleGameManager gameManager = new BattleGameManager();
        gameManager.getGames().add(game);

        assertEquals(1, gameManager.getAllPlayers().size());
        assertTrue(gameManager.getAllPlayers().contains(gamePlayer));
    }

    @Test
    public void testGameManagerShutdown() {
        // Mock a game object
        Game game = mock(Game.class);

        // Instantiate the game manager
        BattleGameManager gameManager = new BattleGameManager();
        gameManager.getGames().add(game);

        gameManager.shutdown();

        verify(game, only()).rollback();
    }
}
