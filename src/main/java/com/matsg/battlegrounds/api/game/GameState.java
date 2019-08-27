package com.matsg.battlegrounds.api.game;

public interface GameState {

    /**
     * Gets whether a certain allowed in the game state.
     *
     * @param action the action type performed
     * @return whether the action is allowed
     */
    boolean isAllowed(Action action);

    /**
     * Gets whether the game state is in progress.
     *
     * @return whether the game state is in progress
     */
    boolean isInProgress();

    /**
     * Goes to the next game state.
     *
     * @return the next game state
     */
    GameState next();

    /**
     * Goes back to the previous game state.
     *
     * @return the previous game state
     */
    GameState previous();
}
