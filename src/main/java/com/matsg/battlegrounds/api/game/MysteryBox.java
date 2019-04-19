package com.matsg.battlegrounds.api.game;

import org.bukkit.block.Block;

public interface MysteryBox extends ArenaComponent, Interactable, Lockable, Priceable, Watchable {

    Block getLeftSide();

    Block getRightSide();

    boolean isActive();

    void setActive(boolean active);
}
