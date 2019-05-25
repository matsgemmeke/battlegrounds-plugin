package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.storage.CacheYaml;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class SetMainLobbyTest {

    @Test
    public void testSetMainLobbyCommand() {
        String responseMessage = "Response";
        TranslationKey key = TranslationKey.MAINLOBBY_SET;

        Battlegrounds plugin = mock(Battlegrounds.class);
        CacheYaml cache = mock(CacheYaml.class);
        Player player = mock(Player.class);
        Translator translator = mock(Translator.class);

        when(plugin.getBattlegroundsCache()).thenReturn(cache);
        when(plugin.getTranslator()).thenReturn(translator);
        when(translator.translate(key)).thenReturn(responseMessage);

        SetMainLobby command = new SetMainLobby(plugin);
        command.execute(player, new String[0]);

        verify(cache, times(1)).setLocation(anyString(), any(Location.class), anyBoolean());
        verify(cache, times(1)).save();
        verify(player, times(1)).sendMessage(responseMessage);
    }
}
