package com.matsg.battlegrounds.api.player;

import com.matsg.battlegrounds.api.game.Game;
import org.bukkit.GameMode;

public enum PlayerStatus {

    ACTIVE(0, true, 'f', GameMode.SURVIVAL, true, true),
    DOWNED(1, true, 'f', GameMode.SURVIVAL, false, false),
    SPECTATING(2, false, '7', GameMode.CREATIVE, false, true);

    private boolean alive, interact, move;
    private char hex;
    private GameMode gameMode;
    private int id;

    PlayerStatus(int id, boolean alive, char hex, GameMode gameMode, boolean interact, boolean move) {
        this.id = id;
        this.alive = alive;
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

    public boolean isAlive() {
        return alive;
    }

    public void apply(Game game, GamePlayer gamePlayer) {
        game.getPlayerManager().setVisible(gamePlayer, alive);
        gamePlayer.getPlayer().setGameMode(gameMode);
        gamePlayer.getPlayer().setWalkSpeed(move ? (float) 0.2 : (float) 0.0);
    }
}
