package com.matsg.battlegrounds.api.game;

public enum GameState {

    WAITING(0, true, "§2§l[ WAITING ]"),
    INGAME(1, false, "§4§l[ INGAME ]"),
    RESETTING(2, false, "§9§l[ RESETTING ]");

    private boolean joinable;
    private int id;
    private String signState;

    GameState(int id, boolean joinable, String signState) {
        this.id = id;
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

    public boolean isJoinable() {
        return joinable;
    }
}