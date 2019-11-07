package com.matsg.battlegrounds.gui;

import org.bukkit.entity.Player;

public interface Button {

    void onLeftClick(Player player);

    void onRightClick(Player player);
}
