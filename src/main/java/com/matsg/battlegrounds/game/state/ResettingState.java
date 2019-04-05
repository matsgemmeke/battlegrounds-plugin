package com.matsg.battlegrounds.game.state;

import com.matsg.battlegrounds.api.game.Action;
import com.matsg.battlegrounds.api.game.GameState;

public class ResettingState implements GameState {

    public boolean isAllowed(Action action) {
        return action == Action.MOVEMENT;
    }

    public boolean isInProgress() {
        return true;
    }

    public GameState next() {
        throw new IllegalStateException();
    }

    public GameState previous() {
        return new InGameState();
    }

    public String toString() {
        return "Resetting";
    }
}
