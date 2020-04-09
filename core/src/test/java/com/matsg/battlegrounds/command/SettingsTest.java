package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.gui.ViewFactory;
import com.matsg.battlegrounds.gui.view.View;
import org.bukkit.entity.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.function.Consumer;

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
        View view = mock(View.class);

        when(viewFactory.make(any(Class.class), any(Consumer.class))).thenReturn(view);

        Settings command = new Settings(translator, viewFactory);
        command.execute(player, new String[0]);

        verify(view, times(1)).openInventory(player);
    }
}
