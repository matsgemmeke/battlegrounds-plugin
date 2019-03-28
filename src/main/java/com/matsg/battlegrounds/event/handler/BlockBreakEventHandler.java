package com.matsg.battlegrounds.event.handler;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakEventHandler implements EventHandler<BlockBreakEvent> {

    private Battlegrounds plugin;

    public BlockBreakEventHandler(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    public void handle(BlockBreakEvent event) {
        if (!plugin.getBattlegroundsConfig().arenaProtection && plugin.getGameManager().getGame(event.getPlayer()) == null || plugin.getGameManager().getArena(event.getBlock().getLocation()) == null) {
            return;
        }

        event.setCancelled(true);
    }
}
