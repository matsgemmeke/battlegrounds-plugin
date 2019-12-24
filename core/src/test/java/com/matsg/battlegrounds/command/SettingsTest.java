package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.gui.ViewFactory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class SettingsTest {

    private Translator translator;
    private ViewFactory viewFactory;

    @Before
    public void setUp() {
        this.translator = mock(Translator.class);
        this.viewFactory = mock(ViewFactory.class);
    }

    @Test
    public void openSettingsGui() {
        Player player = mock(Player.class);

        Settings command = new Settings(translator, viewFactory);
        command.execute(player, new String[0]);

        verify(player, times(1)).openInventory(any(Inventory.class));
    }
}
