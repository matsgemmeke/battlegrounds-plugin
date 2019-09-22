package com.matsg.battlegrounds;

import com.matsg.battlegrounds.api.Selection;
import org.bukkit.entity.Player;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BattleSelectionManagerTest {

    private Player player;
    private Selection selection;

    @Before
    public void setUp() {
        this.player = mock(Player.class);

        this.selection = new BattleSelection(null, null);
    }

    @Test
    public void findSelectionByPlayer() {
        BattleSelectionManager selectionManager = new BattleSelectionManager();
        selectionManager.setSelection(player, selection);

        assertEquals(selection, selectionManager.getSelection(player));
    }

    @Test
    public void findNonexistingSelectionByPlayer() {
        BattleSelectionManager selectionManager = new BattleSelectionManager();

        assertNull(selectionManager.getSelection(player));
    }

    @Test
    public void setCurrentSelectionOfPlayer() {
        BattleSelectionManager selectionManager = new BattleSelectionManager();
        selectionManager.setSelection(player, selection);

        assertEquals(selection, selectionManager.getSelection(player));
    }

    @Test
    public void replaceCurrentSelectionOfPlayer() {
        Selection newSelection = new BattleSelection(null, null);

        BattleSelectionManager selectionManager = new BattleSelectionManager();
        selectionManager.setSelection(player, selection);
        selectionManager.setSelection(player, newSelection);

        assertEquals(newSelection, selectionManager.getSelection(player));
    }
}
