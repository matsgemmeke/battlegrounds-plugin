package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.Translator;
import com.matsg.battlegrounds.api.Battlegrounds;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;

import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
        PluginCommand.class,
        Translator.class
})
public class BattlegroundsCommandTest {

    private Battlegrounds plugin;
    private Command fakeCommand;
    private CommandSender sender;
    private String fakeCommandName;

    @Before
    public void setUp() {
        this.plugin = mock(Battlegrounds.class);
        this.sender = mock(CommandSender.class);
        this.fakeCommand = mock(Command.class);

        this.fakeCommandName = "fake";

        File fakeFile = new File("");

        PowerMockito.mockStatic(Translator.class);

        when(plugin.getDataFolder()).thenReturn(fakeFile);
        when(fakeCommand.getName()).thenReturn(fakeCommandName);
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

    @Test
    public void testCommandSubcommandRequiresPermission() {
        BattlegroundsCommand command = new BattlegroundsCommand(plugin);
        String message = "Test", messagePath = TranslationKey.NO_PERMISSION.getPath();
        String permission = "permission";

        when(fakeCommand.getPermissionNode()).thenReturn(permission);
        when(sender.hasPermission(permission)).thenReturn(false);
        when(Translator.translate(messagePath)).thenReturn(message);

        command.getSubCommands().add(fakeCommand);

        command.onCommand(sender, null, "battlegrounds", new String[] { fakeCommandName });

        verify(sender, times(1)).sendMessage(message);
    }

    @Test
    public void testCommandExecuteSubcommand() {
        BattlegroundsCommand command = new BattlegroundsCommand(plugin);
        String[] args = new String[] { fakeCommandName };

        command.getSubCommands().add(fakeCommand);

        command.onCommand(sender, null, "battlegrounds", args);

        verify(fakeCommand, times(1)).execute(sender, args);
    }

    @Test
    public void testCommandExecuteSubcommandWithException() {
        BattlegroundsCommand command = new BattlegroundsCommand(plugin);
        String message = "Test", messagePath = TranslationKey.COMMAND_ERROR.getPath();
        String[] args = new String[] { fakeCommandName };

        doThrow(new RuntimeException()).when(fakeCommand).execute(sender, args);

        when(Translator.translate(messagePath)).thenReturn(message);

        command.getSubCommands().add(fakeCommand);

        command.onCommand(sender, null, "battlegrounds", args);

        verify(sender, times(1)).sendMessage(message);
    }
}
