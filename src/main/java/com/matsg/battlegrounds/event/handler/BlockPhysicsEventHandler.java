package com.matsg.battlegrounds.event.handler;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.event.handler.EventHandler;
import org.bukkit.event.block.BlockPhysicsEvent;

public class BlockPhysicsEventHandler implements EventHandler<BlockPhysicsEvent> {

    private Battlegrounds plugin;

    public BlockPhysicsEventHandler(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    public void handle(BlockPhysicsEvent event) {
        if (plugin.getGameManager().getArena(event.getBlock().getLocation()) == null) {
            return;
        }

        event.setCancelled(true);
    }
}
