package com.matsg.battlegrounds.api.game;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public interface GameSign {

    Sign getSign();

    void click(Player player);

    boolean update();
}
