package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.Translator;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class SettingsTest {

    private Battlegrounds plugin;
    private TaskRunner taskRunner;
    private Translator translator;

    @Before
    public void setUp() {
        this.plugin = mock(Battlegrounds.class);
        this.taskRunner = mock(TaskRunner.class);
        this.translator = mock(Translator.class);
    }

    @Test
    public void openSettingsGui() {
        Player player = mock(Player.class);
        Server server = mock(Server.class);

        when(plugin.getServer()).thenReturn(server);

        Settings command = new Settings(plugin, taskRunner, translator);
        command.execute(player, new String[0]);

        verify(player, times(1)).openInventory(any(Inventory.class));
    }
}
