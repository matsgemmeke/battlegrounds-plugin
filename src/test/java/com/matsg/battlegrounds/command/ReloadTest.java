package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.Translator;
import org.bukkit.command.CommandSender;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class ReloadTest {

    private Battlegrounds plugin;
    private CommandSender sender;
    private String responseMessage;
    private Translator translator;

    @Before
    public void setUp() {
        this.plugin = mock(Battlegrounds.class);
        this.sender = mock(CommandSender.class);
        this.translator = mock(Translator.class);

        this.responseMessage = "Response";

        when(plugin.getTranslator()).thenReturn(translator);
    }

    @Test
    public void reloadWithoutFail() {
        TranslationKey key = TranslationKey.RELOAD_SUCCESS;

        when(plugin.loadConfigs()).thenReturn(true);
        when(translator.translate(key.getPath())).thenReturn(responseMessage);

        Reload command = new Reload(plugin, translator);
        command.execute(sender, new String[0]);

        verify(sender, times(1)).sendMessage(responseMessage);
    }

    @Test
    public void reloadWithFail() {
        TranslationKey key = TranslationKey.RELOAD_FAILED;

        when(plugin.loadConfigs()).thenReturn(false);
        when(translator.translate(key.getPath())).thenReturn(responseMessage);

        Reload command = new Reload(plugin, translator);
        command.execute(sender, new String[0]);

        verify(sender, times(1)).sendMessage(responseMessage);
    }
}
