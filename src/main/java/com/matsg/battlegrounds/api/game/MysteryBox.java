package com.matsg.battlegrounds.api.game;

import org.bukkit.block.Block;

public interface MysteryBox extends ArenaComponent, Interactable, Lockable, Watchable {

    Block getLeftSide();

    Block getRightSide();
}
