package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.BattleGameManager;
import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.GameManager;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.storage.BattlegroundsConfig;
import com.matsg.battlegrounds.api.storage.CacheYaml;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class SetGameSignTest {

    private BattlegroundsConfig config;
    private Block block;
    private Game game;
    private GameManager gameManager;
    private int gameId;
    private Player player;
    private Translator translator;

    @Before
    public void setUp() {
        this.config = mock(BattlegroundsConfig.class);
        this.block = mock(Block.class);
        this.game = mock(Game.class);
        this.player = mock(Player.class);
        this.translator = mock(Translator.class);

        this.gameId = 1;
        this.gameManager = new BattleGameManager();

        gameManager.getGames().add(game);

        when(game.getId()).thenReturn(gameId);
        when(player.getTargetBlock(null, 5)).thenReturn(block);
    }

    @Test
    public void setGameSignWhenTargetIsNotASign() {
        String responseMessage = "Response";
        TranslationKey key = TranslationKey.INVALID_BLOCK;

        Chest chest = mock(Chest.class);
        String[] args = new String[] { "command", String.valueOf(gameId) };

        when(block.getState()).thenReturn(chest);
        when(translator.translate(key.getPath())).thenReturn(responseMessage);

        SetGameSign command = new SetGameSign(translator, gameManager, config);
        command.execute(player, args);

        verify(player, times(1)).sendMessage(responseMessage);
    }

    @Test
    public void setGameSignWhenTargetIsASign() {
        String responseMessage = "Response";
        TranslationKey key = TranslationKey.GAMESIGN_SET;

        CacheYaml dataFile = mock(CacheYaml.class);
        Sign sign = mock(Sign.class);
        String[] args = new String[] { "command", String.valueOf(gameId) };

        when(block.getState()).thenReturn(sign);
        when(game.getDataFile()).thenReturn(dataFile);
        when(translator.translate(eq(key.getPath()), anyVararg())).thenReturn(responseMessage);

        SetGameSign command = new SetGameSign(translator, gameManager, config);
        command.execute(player, args);

        verify(dataFile, times(1)).setLocation(anyString(), any(Location.class), anyBoolean());
        verify(dataFile, times(1)).save();
        verify(player, times(1)).sendMessage(responseMessage);
        verify(sign, times(1)).update();
    }
}
