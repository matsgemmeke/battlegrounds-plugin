package com.matsg.battlegrounds.game;

import com.matsg.battlegrounds.api.game.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class GameEventHandler implements EventHandler {

    public void onPlayerMove(PlayerMoveEvent event) {
        System.out.print(event.getPlayer().getName());
    }
}