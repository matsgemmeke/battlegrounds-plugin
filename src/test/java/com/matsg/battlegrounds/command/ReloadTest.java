package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.Translator;
import com.matsg.battlegrounds.api.Battlegrounds;
import org.bukkit.command.CommandSender;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({BattlegroundsPlugin.class, Translator.class})
public class ReloadTest {

    private Battlegrounds plugin;
    private CommandSender sender;

    @Before
    public void setUp() {
        this.plugin = mock(Battlegrounds.class);
        this.sender = mock(CommandSender.class);

        PowerMockito.mockStatic(BattlegroundsPlugin.class);
        PowerMockito.mockStatic(Translator.class);

        when(BattlegroundsPlugin.getPlugin()).thenReturn(plugin);
    }

    @Test
    public void testReloadCommandWithoutFail() {
        String message = "Test", messagePath = TranslationKey.RELOAD_SUCCESS.getPath();

        when(plugin.loadConfigs()).thenReturn(true);
        when(Translator.translate(messagePath)).thenReturn(message);

        Reload command = new Reload(plugin);
        command.execute(sender, new String[0]);

        verify(sender, times(1)).sendMessage(message);
    }

    @Test
    public void testReloadCommandWithFail() {
        String message = "Fail";

        when(plugin.loadConfigs()).thenReturn(false);
        when(Translator.translate(any())).thenReturn(message);

        Reload command = new Reload(plugin);
        command.execute(sender, new String[0]);

        verify(sender, times(1)).sendMessage(message);
    }
}
