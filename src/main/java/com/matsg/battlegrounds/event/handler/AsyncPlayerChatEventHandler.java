package com.matsg.battlegrounds.event.handler;

import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.event.handler.EventHandler;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.Team;
import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.util.EnumMessage;
import org.bukkit.ChatColor;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncPlayerChatEventHandler implements EventHandler<AsyncPlayerChatEvent> {

    private Battlegrounds plugin;
    private Game game;

    public AsyncPlayerChatEventHandler(Game game) {
        this.game = game;
        this.plugin = BattlegroundsPlugin.getPlugin();
    }

    public boolean handle(AsyncPlayerChatEvent event) {
        GamePlayer gamePlayer = game.getPlayerManager().getGamePlayer(event.getPlayer());
        Team team = gamePlayer.getTeam();

        if (plugin.getBattlegroundsConfig().broadcastChat) {
            plugin.getLogger().info("[Game " + game.getId() + "] " + gamePlayer.getName() + ": " + event.getMessage());
        }

        game.getPlayerManager().broadcastMessage(EnumMessage.PLAYER_MESSAGE.getMessage(
                new Placeholder("bg_message", event.getMessage()),
                new Placeholder("player_name", team.getChatColor() + gamePlayer.getName() + ChatColor.WHITE)));

        return true;
    }
}