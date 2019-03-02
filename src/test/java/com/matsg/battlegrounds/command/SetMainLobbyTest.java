package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.Translator;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.config.CacheYaml;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({BattlegroundsPlugin.class, Translator.class})
public class SetMainLobbyTest {

    @Test
    public void testSetMainLobbyCommand() {
        String message = "Test";
        String messagePath = TranslationKey.MAINLOBBY_SET.getPath();

        Battlegrounds plugin = mock(Battlegrounds.class);
        CacheYaml cache = mock(CacheYaml.class);
        Player player = mock(Player.class);

        PowerMockito.mockStatic(BattlegroundsPlugin.class);
        PowerMockito.mockStatic(Translator.class);

        when(BattlegroundsPlugin.getPlugin()).thenReturn(plugin);
        when(plugin.getBattlegroundsCache()).thenReturn(cache);
        when(Translator.translate(messagePath)).thenReturn(message);

        SetMainLobby command = new SetMainLobby(plugin);
        command.execute(player, new String[0]);

        verify(cache, times(1)).setLocation(anyString(), any(Location.class), anyBoolean());
        verify(cache, times(1)).save();
        verify(player, times(1)).sendMessage(message);
    }
}
