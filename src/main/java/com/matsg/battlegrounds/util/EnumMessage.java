package com.matsg.battlegrounds.util;

import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.api.util.Message;
import com.matsg.battlegrounds.api.util.Placeholder;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public enum EnumMessage implements Message {

    COUNTDOWN_NOTE("game-countdown-note", true),
    PLAYER_JOIN("game-player-join", true),
    PLAYER_LEAVE("game-player-leave", true),
    PLAYER_MESSAGE("game-player-message", true),
    PREFIX("prefix", false),
    RELOAD_FAILED("setup-reload-failed", false),
    RELOAD_SUCCESS("setup-reload-success", true);

    private boolean hasPrefix;
    private String message, path;

    EnumMessage(String path, boolean hasPrefix) {
        this.message = BattlegroundsPlugin.getPlugin().getTranslator().getTranslation(path);
        this.hasPrefix = hasPrefix;
        this.path = path;
    }

    public String getMessage() {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public String getMessage(Placeholder... placeholders) {
        return replace(placeholders);
    }

    // Get the String message without sending it to the player
    private String replace(Placeholder... placeholders) {
        StringBuilder builder = new StringBuilder(message);
        if (hasPrefix) {
            builder.append(PREFIX.getMessage());
        }
        return builder.append(ChatColor.translateAlternateColorCodes('&', Placeholder.replace(message, placeholders))).toString();
    }

    public void send(CommandSender sender, Placeholder... placeholders) {
        sender.sendMessage(replace(placeholders));
    }

    public void send(Player player, Placeholder... placeholders) {
        send((CommandSender) player, placeholders);
    }

    public String toString() {
        return message + "@" + path;
    }
}