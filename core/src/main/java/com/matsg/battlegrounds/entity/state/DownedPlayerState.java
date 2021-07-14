package com.matsg.battlegrounds.entity.state;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.entity.PlayerState;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DownedPlayerState implements PlayerState {

    private static final boolean CAN_INTERACT = false;
    private static final boolean CAN_MOVE = false;
    private static final boolean IS_ALIVE = true;
    private static final ChatColor CHAT_COLOR = ChatColor.WHITE;
    private static final float DOWNED_WALK_SPEED = 0.0f;
    // Five minute invincibility period
    private static final int NO_DAMAGE_TICKS_DURATION = 6000;

    private float originalWalkSpeed;
    private GamePlayer gamePlayer;

    public DownedPlayerState(GamePlayer gamePlayer, float originalWalkSpeed) {
        this.gamePlayer = gamePlayer;
        this.originalWalkSpeed = originalWalkSpeed;
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
        Player player = gamePlayer.getPlayer();
        player.setNoDamageTicks(NO_DAMAGE_TICKS_DURATION);
        player.setWalkSpeed(DOWNED_WALK_SPEED);
    }

    public void remove() {
        Player player = gamePlayer.getPlayer();
        player.setNoDamageTicks(0);
        player.setWalkSpeed(originalWalkSpeed);
    }
}
