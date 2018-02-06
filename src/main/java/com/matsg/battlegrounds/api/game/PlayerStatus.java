package com.matsg.battlegrounds.api.game;

import org.bukkit.GameMode;

public enum PlayerStatus {

    ALIVE(0, true, 'f', GameMode.SURVIVAL, true, true),
    DOWNED(1, true, 'f', GameMode.SURVIVAL, false, false),
    SPECTATING(2, false, '7', GameMode.CREATIVE, false, true);

    private boolean active, interact, move;
    private char hex;
    private GameMode gameMode;
    private int id;

    PlayerStatus(int id, boolean active, char hex, GameMode gameMode, boolean interact, boolean move) {
        this.id = id;
        this.active = active;
        this.gameMode = gameMode;
        this.hex = hex;
        this.interact = interact;
        this.move = move;
    }

    public static PlayerStatus valueOf(int id) {
        for (PlayerStatus playerStatus : values()) {
            if (playerStatus.id == id) {
                return playerStatus;
            }
        }
        return null;
    }

    public boolean canInteract() {
        return interact;
    }

    public boolean canMove() {
        return move;
    }

    public char getHex() {
        return hex;
    }

    public boolean isActive() {
        return active;
    }

    public void apply(Game game, GamePlayer gamePlayer) {
        game.setVisible(gamePlayer, active);
        gamePlayer.getPlayer().setGameMode(gameMode);
        gamePlayer.getPlayer().setWalkSpeed(move ? (float) 0.2 : (float) 0.0);
    }
}