package com.matsg.battlegrounds.event.handler;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceEventHandler implements EventHandler<BlockPlaceEvent> {

    private Battlegrounds plugin;

    public BlockPlaceEventHandler(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    public void handle(BlockPlaceEvent event) {
        if (!plugin.getBattlegroundsConfig().arenaProtection && plugin.getGameManager().getGame(event.getPlayer()) == null || plugin.getGameManager().getArena(event.getBlock().getLocation()) == null) {
            return;
        }

        event.setCancelled(true);
    }
}
