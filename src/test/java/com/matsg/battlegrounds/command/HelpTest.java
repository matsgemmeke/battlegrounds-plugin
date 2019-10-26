package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.Version;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class HelpTest {

    private Command subCommandPermission;
    private Command subCommandNoPermission;
    private Translator translator;
    private Version version;

    @Before
    public void setUp() {
        this.subCommandPermission = mock(Command.class);
        this.subCommandNoPermission = mock(Command.class);
        this.translator = mock(Translator.class);
        this.version = mock(Version.class);
    }

    @Test
    public void executeHelpCommandWithPlayerSenderNoPermission() {
        Player player = mock(Player.class);
        String permission = "permission";

        when(player.hasPermission(permission)).thenReturn(false);
        when(subCommandPermission.getPermissionNode()).thenReturn(permission);

        Help command = new Help(translator, new Command[] { subCommandNoPermission, subCommandPermission }, version);
        command.execute(player, new String[0]);

        verify(player, times(3)).sendMessage(anyString());
        verify(version, times(1)).sendJSONMessage(eq(player), anyString(), anyString(), anyString());
    }

    @Test
    public void executeHelpCommandWithPlayerSenderWithPermission() {
        Player player = mock(Player.class);
        String permission = "permission";

        when(player.hasPermission(permission)).thenReturn(true);
        when(subCommandPermission.getPermissionNode()).thenReturn(permission);

        Help command = new Help(translator, new Command[] { subCommandNoPermission, subCommandPermission }, version);
        command.execute(player, new String[0]);

        verify(player, times(3)).sendMessage(anyString());
        verify(version, times(2)).sendJSONMessage(eq(player), anyString(), anyString(), anyString());
    }

    @Test
    public void executeHelpCommandWithConsoleSender() {
        ConsoleCommandSender sender = mock(ConsoleCommandSender.class);
        String permission = "permission";

        when(sender.hasPermission(anyString())).thenReturn(true); // Console has all permissions
        when(subCommandPermission.getPermissionNode()).thenReturn(permission);

        Help command = new Help(translator, new Command[] { subCommandNoPermission, subCommandPermission }, version);
        command.execute(sender, new String[0]);

        verify(sender, times(5)).sendMessage(anyString());
    }
}
