package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.util.Message;
import org.bukkit.command.CommandSender;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({BattlegroundsPlugin.class, Message.class})
public class ReloadTest {

    private Battlegrounds plugin;
    private CommandSender sender;

    @Before
    public void setUp() {
        this.plugin = mock(Battlegrounds.class);
        this.sender = mock(CommandSender.class);

        PowerMockito.mockStatic(BattlegroundsPlugin.class);

        when(BattlegroundsPlugin.getPlugin()).thenReturn(plugin);

        PowerMockito.mockStatic(Message.class);
    }

    @Test
    public void testReloadCommandWithoutFail() {
        String message = "Test";

        when(plugin.loadConfigs()).thenReturn(true);
        when(Message.create(TranslationKey.RELOAD_SUCCESS)).thenReturn(message);

        Reload command = new Reload(plugin);
        command.execute(sender, new String[0]);

        verify(sender, times(1)).sendMessage(message);
    }

    @Test
    public void testReloadCommandWithFail() {
        String message = "Fail";

        when(plugin.loadConfigs()).thenReturn(false);
        when(Message.create(TranslationKey.RELOAD_FAILED)).thenReturn(message);

        Reload command = new Reload(plugin);
        command.execute(sender, new String[0]);

        verify(sender, times(1)).sendMessage(message);
    }
}
