package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.storage.PlayerStorage;
import com.matsg.battlegrounds.api.storage.StoredPlayer;
import org.bukkit.entity.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RenameTest {

    private Player player;
    private PlayerStorage playerStorage;
    private Translator translator;

    @Before
    public void setUp() {
        this.player = mock(Player.class);
        this.playerStorage = mock(PlayerStorage.class);
        this.translator = mock(Translator.class);
    }

    @Test
    public void renameLoadoutWithoutSpecifiedNumber() {
        String responseMessage = "Response";
        TranslationKey key = TranslationKey.SPECIFY_LOADOUT_ID;

        String[] args = new String[] { "command" };

        when(translator.translate(key.getPath())).thenReturn(responseMessage);

        Rename command = new Rename(translator, playerStorage);
        command.execute(player, args);

        verify(player, times(1)).sendMessage(responseMessage);
    }

    @Test
    public void renameLoadoutWithInvalidNumber() {
        String responseMessage = "Response";
        TranslationKey key = TranslationKey.INVALID_ARGUMENT_TYPE;

        String[] args = new String[] { "command", "fail" };

        when(translator.translate(eq(key.getPath()), anyVararg())).thenReturn(responseMessage);

        Rename command = new Rename(translator, playerStorage);
        command.execute(player, args);

        verify(player, times(1)).sendMessage(responseMessage);
    }

    @Test
    public void renameLoadoutWithUnavailableNumber() {
        String responseMessage = "Response";
        TranslationKey key = TranslationKey.INVALID_LOADOUT;

        int loadoutNr = 100;
        StoredPlayer storedPlayer = mock(StoredPlayer.class);
        String[] args = new String[] { "command", String.valueOf(loadoutNr) };
        UUID uuid = UUID.randomUUID();

        when(player.getUniqueId()).thenReturn(uuid);
        when(playerStorage.getStoredPlayer(uuid)).thenReturn(storedPlayer);
        when(storedPlayer.getLoadoutSetup(loadoutNr)).thenReturn(null);
        when(translator.translate(key.getPath())).thenReturn(responseMessage);

        Rename command = new Rename(translator, playerStorage);
        command.execute(player, args);

        verify(player, times(1)).sendMessage(responseMessage);
    }

    @Test
    public void renameLoadoutWithoutSpecifiedName() {
        String responseMessage = "Response";
        TranslationKey key = TranslationKey.SPECIFY_LOADOUT_NAME;

        int loadoutNr = 1;
        Map<String, String> loadoutSetup = new HashMap<>();
        StoredPlayer storedPlayer = mock(StoredPlayer.class);
        String[] args = new String[] { "command", String.valueOf(loadoutNr) };
        UUID uuid = UUID.randomUUID();

        when(player.getUniqueId()).thenReturn(uuid);
        when(playerStorage.getStoredPlayer(uuid)).thenReturn(storedPlayer);
        when(storedPlayer.getLoadoutSetup(loadoutNr)).thenReturn(loadoutSetup);
        when(translator.translate(key.getPath())).thenReturn(responseMessage);

        Rename command = new Rename(translator, playerStorage);
        command.execute(player, args);

        verify(player, times(1)).sendMessage(responseMessage);
    }

    @Test
    public void renameLoadoutWithValidNumberAndName() {
        String responseMessage = "Response";
        TranslationKey key = TranslationKey.RENAME_LOADOUT;

        int loadoutNr = 1;
        Map<String, String> loadoutSetup = new HashMap<>();
        StoredPlayer storedPlayer = mock(StoredPlayer.class);
        String[] args = new String[] { "command", String.valueOf(loadoutNr), "new name" };
        UUID uuid = UUID.randomUUID();

        loadoutSetup.put("loadout_name", "old name");

        when(player.getUniqueId()).thenReturn(uuid);
        when(playerStorage.getStoredPlayer(uuid)).thenReturn(storedPlayer);
        when(storedPlayer.getLoadoutSetup(loadoutNr)).thenReturn(loadoutSetup);
        when(translator.translate(eq(key.getPath()), anyVararg())).thenReturn(responseMessage);

        Rename command = new Rename(translator, playerStorage);
        command.execute(player, args);

        verify(player, times(1)).sendMessage(responseMessage);
        verify(storedPlayer, times(1)).saveLoadout(loadoutNr, loadoutSetup);

        assertEquals("new name", loadoutSetup.get("loadout_name"));
    }
}
