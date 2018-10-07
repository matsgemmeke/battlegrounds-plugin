package com.matsg.battlegrounds.event.handler;

import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.event.handler.EventHandler;
import com.matsg.battlegrounds.util.EnumMessage;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

public class PlayerCommandPreprocessEventHandler implements EventHandler<PlayerCommandPreprocessEvent> {

    private Battlegrounds plugin;

    public PlayerCommandPreprocessEventHandler() {
        this.plugin = BattlegroundsPlugin.getPlugin();
    }

    public boolean handle(PlayerCommandPreprocessEvent event) {
        List<String> list = plugin.getBattlegroundsConfig().allowedCommands;

        if (list.contains("*") || list.contains(event.getMessage().split(" ")[0].substring(1, event.getMessage().split(" ")[0].length()))) {
            return false;
        }

        EnumMessage.COMMAND_NOT_ALLOWED.send(event.getPlayer());
        return true;
    }
}