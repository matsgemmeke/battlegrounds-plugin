package com.matsg.battlegrounds.api.game;

public enum PlayerStatus {

    ALIVE(0),
    DOWNED(1),
    SPECTATING(2);

    private int id;

    PlayerStatus(int id) {
        this.id = id;
    }

    public static PlayerStatus getById(int id) {
        for (PlayerStatus playerStatus : values()) {
            if (playerStatus.id == id) {
                return playerStatus;
            }
        }
        return null;
    }
}