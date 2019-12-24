package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.storage.LevelConfig;
import com.matsg.battlegrounds.api.storage.PlayerStorage;
import com.matsg.battlegrounds.api.storage.StoredPlayer;
import com.matsg.battlegrounds.gui.ViewFactory;
import org.bukkit.entity.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.mockito.Mockito.*;

public class LoadoutCommandTest {

    private int minLevel;
    private LevelConfig levelConfig;
    private Player player;
    private PlayerStorage playerStorage;
    private Translator translator;
    private ViewFactory viewFactory;

    @Before
    public void setUp() {
        this.levelConfig = mock(LevelConfig.class);
        this.player = mock(Player.class);
        this.playerStorage = mock(PlayerStorage.class);
        this.translator = mock(Translator.class);
        this.viewFactory = mock(ViewFactory.class);

        this.minLevel = 4;
    }

    @Test
    public void executeCommandPlayerNotRegistered() {
        String responseMessage = "Response";
        TranslationKey key = TranslationKey.CUSTOM_LOADOUT_LOCKED;

        when(translator.translate(eq(key.getPath()), anyVararg())).thenReturn(responseMessage);

        LoadoutCommand command = new LoadoutCommand(translator, levelConfig, playerStorage, viewFactory, minLevel);
        command.execute(player, new String[0]);

        verify(player, times(1)).sendMessage(responseMessage);
    }

    @Test
    public void executeCommandPlayerNotHighEnoughLevel() {
        String responseMessage = "Response";
        TranslationKey key = TranslationKey.CUSTOM_LOADOUT_LOCKED;

        StoredPlayer storedPlayer = mock(StoredPlayer.class);
        UUID uuid = UUID.randomUUID();

        when(levelConfig.getLevel(anyInt())).thenReturn(minLevel - 1);
        when(player.getUniqueId()).thenReturn(uuid);
        when(playerStorage.getStoredPlayer(uuid)).thenReturn(storedPlayer);
        when(translator.translate(eq(key.getPath()), anyVararg())).thenReturn(responseMessage);

        LoadoutCommand command = new LoadoutCommand(translator, levelConfig, playerStorage, viewFactory, minLevel);
        command.execute(player, new String[0]);

        verify(player, times(1)).sendMessage(responseMessage);
    }
}
