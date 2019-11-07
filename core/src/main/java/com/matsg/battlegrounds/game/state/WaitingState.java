package com.matsg.battlegrounds.game.state;

import com.matsg.battlegrounds.api.game.Action;
import com.matsg.battlegrounds.api.game.GameState;
import com.matsg.battlegrounds.util.XMaterial;
import org.bukkit.inventory.ItemStack;

public class WaitingState implements GameState {

    public boolean isAllowed(Action action) {
        return action == Action.MOVEMENT || action == Action.USE_ITEM;
    }

    public boolean isInProgress() {
        return false;
    }

    public GameState next() {
        return new StartingState();
    }

    public GameState previous() {
        throw new IllegalStateException();
    }

    public ItemStack toItemStack() {
        return new ItemStack(XMaterial.TERRACOTTA.parseMaterial(), 1, (short) 5);
    }

    public String toString() {
        return "Waiting";
    }
}
