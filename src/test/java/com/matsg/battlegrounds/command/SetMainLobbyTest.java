package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.config.CacheYaml;
import com.matsg.battlegrounds.util.Message;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({BattlegroundsPlugin.class, Message.class})
public class SetMainLobbyTest {

    @Test
    public void testSetMainLobbyCommand() {
        String message = "Test";

        Battlegrounds plugin = mock(Battlegrounds.class);
        CacheYaml cache = mock(CacheYaml.class);
        Player player = mock(Player.class);

        PowerMockito.mockStatic(BattlegroundsPlugin.class);

        when(BattlegroundsPlugin.getPlugin()).thenReturn(plugin);
        when(plugin.getBattlegroundsCache()).thenReturn(cache);

        PowerMockito.mockStatic(Message.class);
        when(Message.create(TranslationKey.MAINLOBBY_SET)).thenReturn(message);

        SetMainLobby command = new SetMainLobby(plugin);
        command.execute(player, new String[0]);

        verify(cache, times(1)).setLocation(anyString(), any(Location.class), anyBoolean());
        verify(cache, times(1)).save();
        verify(player, times(1)).sendMessage(message);
    }
}
