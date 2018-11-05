package com.matsg.battlegrounds.event.handler;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.event.handler.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinEventHandler implements EventHandler<PlayerJoinEvent> {

    private Battlegrounds plugin;

    public PlayerJoinEventHandler(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    public void handle(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!plugin.getPlayerStorage().contains(player.getUniqueId())) {
            plugin.getPlayerStorage().registerPlayer(player.getUniqueId(), player.getName());
        } else {
            plugin.getPlayerStorage().updatePlayer(player);
        }
    }
}