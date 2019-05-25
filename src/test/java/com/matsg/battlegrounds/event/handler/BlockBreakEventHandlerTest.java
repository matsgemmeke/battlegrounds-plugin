package com.matsg.battlegrounds.event.handler;

import com.matsg.battlegrounds.BattleGameManager;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.GameManager;
import com.matsg.battlegrounds.api.storage.BattlegroundsConfig;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.PlayerManager;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.game.BattleArena;
import com.matsg.battlegrounds.entity.BattleGamePlayer;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BlockBreakEventHandlerTest {

    private Arena arena;
    private Battlegrounds plugin;
    private BattlegroundsConfig config;
    private Block block;
    private BlockBreakEvent event;
    private Game game;
    private GameManager gameManager;
    private GamePlayer gamePlayer;
    private Player player;
    private PlayerManager playerManager;
    private World world;

    @Before
    public void setUp() {
        this.plugin = mock(Battlegrounds.class);
        this.config = mock(BattlegroundsConfig.class);
        this.block = mock(Block.class);
        this.game = mock(Game.class);
        this.player = mock(Player.class);
        this.playerManager = mock(PlayerManager.class);
        this.world = mock(World.class);

        this.arena = new BattleArena("Arena", world, new Location(world, 100, 100, 100), new Location(world, 0, 0, 0));
        this.event = new BlockBreakEvent(block, player);
        this.gameManager = new BattleGameManager();
        this.gamePlayer = new BattleGamePlayer(player, null);

        List<Arena> arenaList = new ArrayList<>();
        arenaList.add(arena);

        gameManager.getGames().add(game);

        when(block.getLocation()).thenReturn(new Location(world, 50, 50, 50));
        when(game.getArena()).thenReturn(arena);
        when(game.getArenaList()).thenReturn(arenaList);
        when(game.getPlayerManager()).thenReturn(playerManager);
        when(plugin.getBattlegroundsConfig()).thenReturn(config);
        when(plugin.getGameManager()).thenReturn(gameManager);
    }

    @Test
    public void testBlockBreakOutsideArena() {
        config.arenaProtection = true;

        when(block.getLocation()).thenReturn(new Location(world, 1000, 1000, 1000));

        BlockBreakEventHandler eventHandler = new BlockBreakEventHandler(plugin);
        eventHandler.handle(event);

        assertFalse(event.isCancelled());
    }

    @Test
    public void testBlockBreakWithoutArenaProtectionWhenNotPlaying() {
        config.arenaProtection = false;

        when(playerManager.getGamePlayer(player)).thenReturn(null);

        BlockBreakEventHandler eventHandler = new BlockBreakEventHandler(plugin);
        eventHandler.handle(event);

        assertFalse(event.isCancelled());
    }

    @Test
    public void testBlockBreakWithArenaProtectionWhenNotPlaying() {
        config.arenaProtection = true;

        when(playerManager.getGamePlayer(player)).thenReturn(null);

        BlockBreakEventHandler eventHandler = new BlockBreakEventHandler(plugin);
        eventHandler.handle(event);

        assertTrue(event.isCancelled());
    }

    @Test
    public void testBlockBreakWithoutArenaProtectionWhenPlaying() {
        config.arenaProtection = false;

        when(playerManager.getGamePlayer(player)).thenReturn(gamePlayer);

        BlockBreakEventHandler eventHandler = new BlockBreakEventHandler(plugin);
        eventHandler.handle(event);

        assertTrue(event.isCancelled());
    }

    @Test
    public void testBlockBreakWithArenaProtectionWhenPlaying() {
        config.arenaProtection = true;

        when(playerManager.getGamePlayer(player)).thenReturn(gamePlayer);

        BlockBreakEventHandler eventHandler = new BlockBreakEventHandler(plugin);
        eventHandler.handle(event);

        assertTrue(event.isCancelled());
    }
}
