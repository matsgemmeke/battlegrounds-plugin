package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.Translator;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.GameManager;
import com.matsg.battlegrounds.api.config.CacheYaml;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.Spawn;
import com.matsg.battlegrounds.game.ArenaSpawn;
import com.matsg.battlegrounds.game.BattleArena;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Translator.class})
public class AddSpawnTest {

    private Arena arena;
    private Battlegrounds plugin;
    private CacheYaml dataFile;
    private Game game;
    private GameManager gameManager;
    private int gameId;
    private Location location;
    private Player player;
    private String arenaName;
    private World world;

    @Before
    public void setUp() {
        this.plugin = mock(Battlegrounds.class);
        this.dataFile = mock(CacheYaml.class);
        this.game = mock(Game.class);
        this.gameManager = mock(GameManager.class);
        this.player = mock(Player.class);
        this.world = mock(World.class);

        this.location = new Location(world, 0, 0, 0);
        this.arenaName = "Arena";
        this.arena = new BattleArena(arenaName, location, location, world);
        this.gameId = 1;

        PowerMockito.mockStatic(Translator.class);

        when(game.getArena()).thenReturn(arena);
        when(game.getDataFile()).thenReturn(dataFile);
        when(game.getId()).thenReturn(gameId);
        when(gameManager.exists(gameId)).thenReturn(true);
        when(gameManager.getArena(game, arenaName)).thenReturn(arena);
        when(gameManager.getGame(gameId)).thenReturn(game);
        when(player.getLocation()).thenReturn(location);
        when(plugin.getGameManager()).thenReturn(gameManager);
    }

    @Test
    public void testCommandInvalidTeamIdSpecified() {
        String message = "Test", messagePath = TranslationKey.INVALID_ARGUMENT_TYPE.getPath();

        when(Translator.translate(messagePath)).thenReturn(message);

        AddSpawn command = new AddSpawn(plugin);
        command.execute(player, new String[] { "addspawn", String.valueOf(gameId), arenaName, "teamid" });

        verify(dataFile, never()).set(anyString(), anyBoolean());
        verify(player, times(1)).sendMessage(message);
    }

    @Test
    public void testCommandTeamIdSpecifiedAndTeamHasBase() {
        int spawnIndex = 1, teamId = 1;
        Spawn spawn = new ArenaSpawn(spawnIndex, location, teamId);
        String message = "Test", messagePath = TranslationKey.SPAWN_TEAMBASE_EXISTS.getPath();

        arena.getSpawns().add(spawn);
        spawn.setTeamBase(true);

        when(Translator.translate(messagePath)).thenReturn(message);

        AddSpawn command = new AddSpawn(plugin);
        command.execute(player, new String[] { "addspawn", String.valueOf(gameId), arenaName, String.valueOf(teamId), "-b" });

        verify(dataFile, never()).set(anyString(), anyBoolean());
        verify(player, times(1)).sendMessage(message);
    }

    @Test
    public void testCommandNoTeamIdSpecified() {
        int spawnIndex = 1, teamId = 0;
        Spawn spawn = new ArenaSpawn(spawnIndex, location, teamId);
        String message = "Test", messagePath = TranslationKey.SPAWN_ADD.getPath();

        arena.getSpawns().add(spawn);

        when(Translator.translate(messagePath)).thenReturn(message);

        AddSpawn command = new AddSpawn(plugin);
        command.execute(player, new String[] { "addspawn", String.valueOf(gameId), arenaName });

        verify(dataFile, times(1)).set("arena." + arenaName + ".spawn." + (spawnIndex + 1) + ".base", false);
        verify(dataFile, times(1)).setLocation("arena." + arenaName + ".spawn." + (spawnIndex + 1) + ".location", location, true);
        verify(dataFile, times(1)).set("arena." + arenaName + ".spawn." + (spawnIndex + 1) + ".team", teamId);
        verify(dataFile, times(1)).save();
        verify(player, times(1)).sendMessage(message);
    }

    @Test
    public void testCommandNoTeamIdSpecifiedWithAddedSpawns() {
        int spawnIndex = 1;
        String message = "Test", messagePath = TranslationKey.SPAWN_ADD.getPath();

        when(Translator.translate(messagePath)).thenReturn(message);

        AddSpawn command = new AddSpawn(plugin);
        command.execute(player, new String[] { "addspawn", String.valueOf(gameId), arenaName });

        verify(dataFile, times(1)).set("arena." + arenaName + ".spawn." + spawnIndex + ".base", false);
        verify(dataFile, times(1)).setLocation("arena." + arenaName + ".spawn." + spawnIndex + ".location", location, true);
        verify(dataFile, times(1)).set("arena." + arenaName + ".spawn." + spawnIndex + ".team", 0);
        verify(dataFile, times(1)).save();
        verify(player, times(1)).sendMessage(message);
    }

    @Test
    public void testCommandTeamIdSpecifiedAndBase() {
        int spawnIndex = 1, teamId = 1;
        String message = "Test", messagePath = TranslationKey.SPAWN_ADD.getPath();

        when(Translator.translate(messagePath)).thenReturn(message);

        AddSpawn command = new AddSpawn(plugin);
        command.execute(player, new String[] { "addspawn", String.valueOf(gameId), arenaName, String.valueOf(teamId), "-b" });

        verify(dataFile, times(1)).set("arena." + arenaName + ".spawn." + spawnIndex + ".base", true);
        verify(dataFile, times(1)).setLocation("arena." + arenaName + ".spawn." + spawnIndex + ".location", location, true);
        verify(dataFile, times(1)).set("arena." + arenaName + ".spawn." + spawnIndex + ".team", teamId);
        verify(dataFile, times(1)).save();
        verify(player, times(1)).sendMessage(message);
    }
}
