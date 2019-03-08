package com.matsg.battlegrounds.api.game;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public interface GameSign {

    void click(Player player);

    Sign getSign();

    boolean update();
}
