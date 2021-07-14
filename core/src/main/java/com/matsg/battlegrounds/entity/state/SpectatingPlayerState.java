package com.matsg.battlegrounds.entity.state;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.entity.PlayerState;
import com.matsg.battlegrounds.api.game.Game;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.jetbrains.annotations.NotNull;

public class SpectatingPlayerState implements PlayerState {

    private static final boolean CAN_INTERACT = false;
    private static final boolean CAN_MOVE = true;
    private static final boolean IS_ALIVE = false;
    private static final ChatColor CHAT_COLOR = ChatColor.GRAY;
    private static final GameMode SPECTATE_GAMEMODE = GameMode.CREATIVE;

    private Game game;
    private GameMode originalGameMode;
    private GamePlayer gamePlayer;

    public SpectatingPlayerState(Game game, GamePlayer gamePlayer, GameMode originalGameMode) {
        this.game = game;
        this.gamePlayer = gamePlayer;
        this.originalGameMode = originalGameMode;
    }

    public boolean canInteract() {
        return CAN_INTERACT;
    }

    public boolean canMove() {
        return CAN_MOVE;
    }

    @NotNull
    public ChatColor getChatColor() {
        return CHAT_COLOR;
    }

    public boolean isAlive() {
        return IS_ALIVE;
    }

    public void apply() {
        game.getPlayerManager().setVisible(gamePlayer, false);
        gamePlayer.getPlayer().setGameMode(SPECTATE_GAMEMODE);
    }

    public void remove() {
        game.getPlayerManager().setVisible(gamePlayer, true);
        gamePlayer.getPlayer().setGameMode(originalGameMode);
    }
}
