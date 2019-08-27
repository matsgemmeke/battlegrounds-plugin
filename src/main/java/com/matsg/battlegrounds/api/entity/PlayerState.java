package com.matsg.battlegrounds.api.entity;

import com.matsg.battlegrounds.api.game.Game;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;

public enum PlayerState {

    ACTIVE(0, true, ChatColor.WHITE, GameMode.SURVIVAL, true, true),
    DOWNED(1, true, ChatColor.WHITE, GameMode.SURVIVAL, false, false),
    SPECTATING(2, false, ChatColor.GRAY, GameMode.CREATIVE, false, true);

    private boolean alive, interact, move;
    private ChatColor chatColor;
    private GameMode gameMode;
    private int id;

    PlayerState(int id, boolean alive, ChatColor chatColor, GameMode gameMode, boolean interact, boolean move) {
        this.id = id;
        this.alive = alive;
        this.chatColor = chatColor;
        this.gameMode = gameMode;
        this.interact = interact;
        this.move = move;
    }

    public static PlayerState valueOf(int id) {
        for (PlayerState playerState : values()) {
            if (playerState.id == id) {
                return playerState;
            }
        }
        return null;
    }

    /**
     * Gets whether a player can interact with other things in this state.
     *
     * @return whether a player in this state can interact with other things
     */
    public boolean canInteract() {
        return interact;
    }

    /**
     * Gets whether a player can move in this state.
     *
     * @return whether a player in this state can move.
     */
    public boolean canMove() {
        return move;
    }

    /**
     * Gets chat color of the state.
     *
     * @return the state's chat color
     */
    public ChatColor getChatColor() {
        return chatColor;
    }

    /**
     * Gets whether a player in this state is alive.
     *
     * @return whether a player in this state is alive
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Applies the state to a player.
     *
     * @param game the game
     * @param gamePlayer the player
     */
    public void apply(Game game, GamePlayer gamePlayer) {
        game.getPlayerManager().setVisible(gamePlayer, alive);
        gamePlayer.getPlayer().setGameMode(gameMode);
        gamePlayer.getPlayer().setWalkSpeed(move ? (float) 0.2 : (float) 0.0);
    }
}
