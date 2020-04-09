package com.matsg.battlegrounds.game.state;

import com.matsg.battlegrounds.api.game.GameAction;
import com.matsg.battlegrounds.api.game.GameState;
import com.matsg.battlegrounds.util.XMaterial;
import org.bukkit.inventory.ItemStack;

public class StartingState implements GameState {

    public boolean isAllowed(GameAction action) {
        return action == GameAction.USE_ITEM;
    }

    public boolean isInProgress() {
        return false;
    }

    public GameState next() {
        return new InGameState();
    }

    public GameState previous() {
        return new WaitingState();
    }

    public ItemStack toItemStack() {
        return new ItemStack(XMaterial.TERRACOTTA.parseMaterial(), 1, (short) 4);
    }

    public String toString() {
        return "Starting";
    }
}
