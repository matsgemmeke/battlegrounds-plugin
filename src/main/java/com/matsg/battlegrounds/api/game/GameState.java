package com.matsg.battlegrounds.api.game;

public interface GameState {

    /**
     * Gets whether a certain allowed in the game state.
     *
     * @param action The action type performed.
     * @return Whether the action is allowed.
     */
    boolean isAllowed(Action action);

    /**
     * Gets whether the game state is in progress.
     *
     * @return Whether the game state is in progress.
     */
    boolean isInProgress();

    /**
     * Goes to the next game state.
     *
     * @return The next game state.
     */
    GameState next();

    /**
     * Goes back to the previous game state.
     *
     * @return The previous game state.
     */
    GameState previous();
}
