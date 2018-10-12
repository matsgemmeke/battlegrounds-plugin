package com.matsg.battlegrounds.event.handler;

import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.event.handler.EventHandler;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.util.EnumMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

public class PlayerCommandPreprocessEventHandler implements EventHandler<PlayerCommandPreprocessEvent> {

    private Battlegrounds plugin;

    public PlayerCommandPreprocessEventHandler() {
        this.plugin = BattlegroundsPlugin.getPlugin();
    }

    public void handle(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        Game game = plugin.getGameManager().getGame(player);
        List<String> list = plugin.getBattlegroundsConfig().allowedCommands;
        String message = event.getMessage();

        if (game == null || list.contains("*") || list.contains(message.split(" ")[0].substring(1, message.split(" ")[0].length()))) {
            return;
        }

        EnumMessage.COMMAND_NOT_ALLOWED.send(player);

        event.setCancelled(true);
    }
}
