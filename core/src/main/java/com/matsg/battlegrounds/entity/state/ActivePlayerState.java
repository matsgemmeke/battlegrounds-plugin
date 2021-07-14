package com.matsg.battlegrounds.entity.state;

import com.matsg.battlegrounds.api.entity.PlayerState;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

public class ActivePlayerState implements PlayerState {

    private static final boolean CAN_INTERACT = true;
    private static final boolean CAN_MOVE = true;
    private static final boolean IS_ALIVE = true;
    private static final ChatColor CHAT_COLOR = ChatColor.WHITE;

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

    public void apply() { }

    public void remove() { }
}
