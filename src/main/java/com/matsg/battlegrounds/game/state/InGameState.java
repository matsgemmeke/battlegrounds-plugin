package com.matsg.battlegrounds.game.state;

import com.matsg.battlegrounds.api.game.Action;
import com.matsg.battlegrounds.api.game.GameState;
import com.matsg.battlegrounds.util.XMaterial;
import org.bukkit.inventory.ItemStack;

public class InGameState implements GameState {

    public boolean isAllowed(Action action) {
        return action == Action.MOVEMENT || action == Action.USE_ITEM || action == Action.USE_WEAPON;
    }

    public boolean isInProgress() {
        return true;
    }

    public GameState next() {
        return new ResettingState();
    }

    public GameState previous() {
        return new StartingState();
    }

    public ItemStack toItemStack() {
        return new ItemStack(XMaterial.TERRACOTTA.parseMaterial(), 1, (short) 14);
    }

    public String toString() {
        return "Ingame";
    }
}
