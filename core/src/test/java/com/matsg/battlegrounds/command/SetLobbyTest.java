package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.BattleGameManager;
import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.GameManager;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.storage.CacheYaml;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class SetLobbyTest {

    private CacheYaml dataFile;
    private Game game;
    private GameManager gameManager;
    private int gameId;
    private Player player;
    private Translator translator;

    @Before
    public void setUp() {
        this.dataFile = mock(CacheYaml.class);
        this.game = mock(Game.class);
        this.player = mock(Player.class);
        this.translator = mock(Translator.class);

        this.gameId = 1;
        this.gameManager = new BattleGameManager();

        when(game.getDataFile()).thenReturn(dataFile);
        when(game.getId()).thenReturn(gameId);
    }

    @Test
    public void setLobbyOfGame() {
        String responseMessage = "Response";
        String[] args = new String[] { "command", String.valueOf(gameId) };
        TranslationKey key = TranslationKey.LOBBY_SET;

        gameManager.getGames().add(game);

        when(translator.translate(eq(key.getPath()), anyVararg())).thenReturn(responseMessage);

        SetLobby command = new SetLobby(translator, gameManager);
        command.execute(player, args);

        verify(dataFile, times(1)).setLocation(anyString(), any(Location.class), anyBoolean());
        verify(dataFile, times(1)).save();
        verify(player, times(1)).sendMessage(responseMessage);
    }
}
