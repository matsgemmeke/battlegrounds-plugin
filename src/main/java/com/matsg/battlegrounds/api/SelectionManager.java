package com.matsg.battlegrounds.api;

import org.bukkit.entity.Player;

public interface SelectionManager {

    Selection getSelection(Player player);

    void setSelection(Player player, Selection selection);
}
