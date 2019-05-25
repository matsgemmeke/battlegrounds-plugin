package com.matsg.battlegrounds;

import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.PlayerManager;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BattleGameManagerTest {

    private Arena arena;
    private Game game;
    private PlayerManager playerManager;

    @Before
    public void setUp() {
        this.arena = mock(Arena.class);
        this.game = mock(Game.class);
        this.playerManager = mock(PlayerManager.class);

        List<Arena> arenas = new ArrayList<>();
        arenas.add(arena);

        when(arena.getName()).thenReturn("Arena");
        when(game.getArenaList()).thenReturn(arenas);
        when(game.getId()).thenReturn(1);
        when(game.getPlayerManager()).thenReturn(playerManager);
    }

    @Test
    public void findArenaByLocation() {
        Location location1 = new Location(mock(World.class), 1, 1, 1), location2 = location1.clone().add(1, 0, 1);

        when(arena.contains(location1)).thenReturn(true);
        when(arena.contains(location2)).thenReturn(false);

        BattleGameManager gameManager = new BattleGameManager();
        gameManager.getGames().add(game);

        assertNotNull(gameManager.getArena(location1));
        assertNull(gameManager.getArena(location2));
        assertEquals(arena, gameManager.getArena(location1));
    }

    @Test
    public void findArenaByName() {
        BattleGameManager gameManager = new BattleGameManager();
        gameManager.getGames().add(game);

        assertEquals(arena, gameManager.getArena(game, "Arena"));
        assertNull(gameManager.getArena(game, "Fail"));
    }

    @Test
    public void findGameByArena() {
        Arena otherArena = mock(Arena.class);

        BattleGameManager gameManager = new BattleGameManager();
        gameManager.getGames().add(game);

        assertEquals(game, gameManager.getGame(arena));
        assertNull(gameManager.getGame(otherArena));
    }

    @Test
    public void findGameById() {
        BattleGameManager gameManager = new BattleGameManager();
        gameManager.getGames().add(game);

        assertTrue(gameManager.exists(1));
        assertFalse(gameManager.exists(2));
        assertEquals(game, gameManager.getGame(1));
        assertNull(gameManager.getGame(2));
    }

    @Test
    public void findGameByPlayer() {
        GamePlayer gamePlayer = mock(GamePlayer.class);
        Player playerOne = mock(Player.class), playerTwo = mock(Player.class);

        when(gamePlayer.getPlayer()).thenReturn(playerOne);
        when(playerManager.getGamePlayer(playerOne)).thenReturn(gamePlayer);

        BattleGameManager gameManager = new BattleGameManager();
        gameManager.getGames().add(game);

        assertEquals(game, gameManager.getGame(playerOne));
        assertNull(gameManager.getGame(playerTwo));
        assertEquals(gamePlayer, gameManager.getGamePlayer(playerOne));
        assertNull(gameManager.getGamePlayer(playerTwo));
    }

    @Test
    public void findAllArenas() {
        BattleGameManager gameManager = new BattleGameManager();
        gameManager.getGames().add(game);

        assertEquals(1, gameManager.getArenaList().size());
        assertTrue(gameManager.getArenaList().contains(arena));
    }

    @Test
    public void findAllPlayers() {
        List<GamePlayer> list = new ArrayList<>();

        GamePlayer gamePlayer = mock(GamePlayer.class);

        list.add(gamePlayer);

        when(playerManager.getPlayers()).thenReturn(list);

        BattleGameManager gameManager = new BattleGameManager();
        gameManager.getGames().add(game);

        assertEquals(1, gameManager.getAllPlayers().size());
        assertTrue(gameManager.getAllPlayers().contains(gamePlayer));
    }

    @Test
    public void shutDownAllGames() {
        BattleGameManager gameManager = new BattleGameManager();
        gameManager.getGames().add(game);

        gameManager.shutdown();

        verify(game, times(1)).rollback();
    }
}
