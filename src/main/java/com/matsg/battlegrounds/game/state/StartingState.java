package com.matsg.battlegrounds.game.state;

import com.matsg.battlegrounds.api.game.Action;
import com.matsg.battlegrounds.api.game.GameState;

public class StartingState implements GameState {

    public boolean isAllowed(Action action) {
        return action == Action.USE_ITEM;
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

    public String toString() {
        return "Starting";
    }
}
