package com.matsg.battlegrounds.event.handler;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.event.EventHandler;
import com.matsg.battlegrounds.api.game.Game;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerSignInteractHandler implements EventHandler<PlayerInteractEvent> {

    private Battlegrounds plugin;

    public PlayerSignInteractHandler(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    public void handle(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();

        if (block == null || !(block.getState() instanceof Sign)) {
            return;
        }

        Sign sign = (Sign) event.getClickedBlock().getState();

        for (Game game : plugin.getGameManager().getGames()) {
            if (game.getGameSign() != null && game.getGameSign().getSign().equals(sign)) {
                game.getGameSign().click(event.getPlayer());
                break;
            }
        }
    }
}
