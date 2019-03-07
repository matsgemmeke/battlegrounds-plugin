package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.Translator;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
        PluginCommand.class,
        Translator.class
})
public class BattlegroundsCommandTest {

    private BattlegroundsPlugin plugin;
    private CommandSender sender;
    private PluginCommand pluginCommand;
    private String fakeCommandName;
    private SubCommand fakeCommand;

    @Before
    public void setUp() {
        this.plugin = mock(BattlegroundsPlugin.class);
        this.sender = mock(CommandSender.class);
        this.fakeCommand = mock(SubCommand.class);

        this.pluginCommand = PowerMockito.mock(PluginCommand.class);
        this.fakeCommandName = "fake";

        PowerMockito.mockStatic(Translator.class);

        when(plugin.getCommand(anyString())).thenReturn(pluginCommand);
        when(fakeCommand.getName()).thenReturn(fakeCommandName);
    }

    @Test
    public void testCommandRegistration() {
        BattlegroundsCommand command = new BattlegroundsCommand(plugin);

        verify(pluginCommand, times(4)).setExecutor(command);
    }

    @Test
    public void testCommandNoArgumentsWithHelp() {
        BattlegroundsCommand command = new BattlegroundsCommand(plugin);

        command.onCommand(sender, null, "battlegrounds", new String[0]);

        verify(sender, atLeast(1)).sendMessage(anyString());
    }

    @Test
    public void testCommandSubcommandDoesNotExist() {
        BattlegroundsCommand command = new BattlegroundsCommand(plugin);
        String message = "Test", messagePath = TranslationKey.INVALID_ARGUMENTS.getPath();

        when(Translator.translate(messagePath)).thenReturn(message);

        command.onCommand(sender, null, "battlegrounds", new String[] { "invalid" });

        verify(sender, times(1)).sendMessage(message);
    }

    @Test
    public void testCommandSubcommandIsPlayerOnly() {
        BattlegroundsCommand command = new BattlegroundsCommand(plugin);
        CommandSender sender = mock(ConsoleCommandSender.class);
        String message = "Test", messagePath = TranslationKey.INVALID_SENDER.getPath();

        when(fakeCommand.isPlayerOnly()).thenReturn(true);
        when(Translator.translate(messagePath)).thenReturn(message);

        command.getSubCommands().add(fakeCommand);

        command.onCommand(sender, null, "battlegrounds", new String[] { fakeCommandName });

        verify(sender, times(1)).sendMessage(message);
    }
}
