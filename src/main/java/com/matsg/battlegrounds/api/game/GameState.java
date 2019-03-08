package com.matsg.battlegrounds.api.game;

public enum GameState {

    WAITING(1, false, true, true, false),
    STARTING(2, true, true, false, false),
    IN_GAME(3, true, true, true, true),
    RESETTING(4, true, false, true, false),
    DISABLED(5, false, false, false, false);

    private boolean allowItems, allowMove, allowWeapons, inProgress;
    private int id;

    GameState(int id, boolean inProgress, boolean allowItems, boolean allowMove, boolean allowWeapons) {
        this.id = id;
        this.inProgress = inProgress;
        this.allowItems = allowItems;
        this.allowMove = allowMove;
        this.allowWeapons = allowWeapons;
    }

    public static GameState getById(int id) {
        for (GameState state : values()) {
            if (state.id == id) {
                return state;
            }
        }
        return null;
    }

    public boolean isAllowItems() {
        return allowItems;
    }

    public boolean isAllowMove() {
        return allowMove;
    }

    public boolean isAllowWeapons() {
        return allowWeapons;
    }

    public boolean isInProgress() {
        return inProgress;
    }
}
