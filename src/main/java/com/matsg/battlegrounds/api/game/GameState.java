package com.matsg.battlegrounds.api.game;

public enum GameState {

    WAITING(0, true, false, "§2§l[ WAITING ]"),
    STARTING(1, false, true, "§6§l[ STARTING ]"),
    IN_GAME(2, false, true, "§4§l[ IN GAME ]"),
    RESETTING(3, false, false, "§9§l[ RESETTING ]"),
    DISABLED(4, false, false, "§8§l[ DISABLED ]");

    private boolean inProgress, joinable;
    private int id;
    private String signState;

    GameState(int id, boolean joinable, boolean inProgress, String signState) {
        this.id = id;
        this.inProgress = inProgress;
        this.joinable = joinable;
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

    public boolean isInProgress() {
        return inProgress;
    }

    public boolean isJoinable() {
        return joinable;
    }
}