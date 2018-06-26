package com.matsg.battlegrounds.api.game;

public enum GameState {

    WAITING(1, true, false, true, true, false, "§2§l[ WAITING ]"),
    STARTING(2, false, true, true, false, false, "§6§l[ STARTING ]"),
    IN_GAME(3, false, true, true, true, true, "§4§l[ IN GAME ]"),
    RESETTING(4, false, false, true, true, false, "§9§l[ RESETTING ]"),
    DISABLED(5, false, false, false, false, false, "§8§l[ DISABLED ]");

    private boolean allowItems, allowMove, allowWeapons, inProgress, joinable;
    private int id;
    private String signState;

    GameState(int id, boolean joinable, boolean inProgress, boolean allowItems, boolean allowMove, boolean allowWeapons, String signState) {
        this.id = id;
        this.inProgress = inProgress;
        this.joinable = joinable;
        this.allowItems = allowItems;
        this.allowMove = allowMove;
        this.allowWeapons = allowWeapons;
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

    public boolean isAllowMove() {
        return allowMove;
    }

    public boolean isAllowWeapons() {
        return allowWeapons;
    }

    public boolean isInProgress() {
        return inProgress;
    }

    public boolean isJoinable() {
        return joinable;
    }
}