package com.matsg.battlegrounds.event.handler;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.event.EventHandler;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.Team;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.util.MessageHelper;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncPlayerChatEventHandler implements EventHandler<AsyncPlayerChatEvent> {

    private Battlegrounds plugin;
    private MessageHelper messageHelper;

    public AsyncPlayerChatEventHandler(Battlegrounds plugin) {
        this.plugin = plugin;
        this.messageHelper = new MessageHelper();
    }

    public void handle(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Game game = plugin.getGameManager().getGame(player);

        if (game == null) {
            for (GamePlayer gamePlayer : plugin.getGameManager().getAllPlayers()) {
                event.getRecipients().remove(gamePlayer.getPlayer());
            }
            return;
        }

        GamePlayer gamePlayer = game.getPlayerManager().getGamePlayer(player);
        Team team = gamePlayer.getTeam();

        if (plugin.getBattlegroundsConfig().broadcastChat) {
            plugin.getLogger().info("[Game " + game.getId() + "] " + gamePlayer.getName() + ": " + event.getMessage());
        }

        game.getPlayerManager().broadcastMessage(messageHelper.create(TranslationKey.PLAYER_MESSAGE,
                new Placeholder("bg_message", event.getMessage()),
                new Placeholder("player_name", team.getChatColor() + gamePlayer.getName() + ChatColor.WHITE)
        ));

        event.setCancelled(true);
    }
}
