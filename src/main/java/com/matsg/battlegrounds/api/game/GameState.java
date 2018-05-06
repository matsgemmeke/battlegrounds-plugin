package com.matsg.battlegrounds.api.game;

public enum GameState {

    WAITING(0, true, false, false, "§2§l[ WAITING ]"),
    STARTING(1, false, true, false, "§6§l[ STARTING ]"),
    IN_GAME(2, false, true, true, "§4§l[ IN GAME ]"),
    RESETTING(3, false, false, false, "§9§l[ RESETTING ]"),
    DISABLED(4, false, false, false, "§8§l[ DISABLED ]");

    private boolean allowItems, inProgress, joinable;
    private int id;
    private String signState;

    GameState(int id, boolean joinable, boolean inProgress, boolean allowItems, String signState) {
        this.id = id;
        this.inProgress = inProgress;
        this.joinable = joinable;
        this.allowItems = allowItems;
        this.signState = signState;
    }

    public static GameState getById(int id) {
        for (GameState state : values()) {
            if (state.id == id) {
                return state;
            }
        }
        return null;
    }

    public String asSignState() {
        return signState;
    }

    public boolean isAllowItems() {
        return allowItems;
    }

    public boolean isInProgress() {
        return inProgress;
    }

    public boolean isJoinable() {
        return joinable;
    }
}