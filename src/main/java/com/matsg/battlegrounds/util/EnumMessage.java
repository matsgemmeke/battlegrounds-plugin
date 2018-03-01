package com.matsg.battlegrounds.util;

import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.util.Message;
import com.matsg.battlegrounds.api.util.Placeholder;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public enum EnumMessage implements Message {

    COMMAND_ERROR("command-error", false),
    COUNTDOWN_NOTE("game-countdown-note", true),
    DESCRIPTION_CREATEGAME("command-description-creategame", false),
    DESCRIPTION_RELOAD("command-description-reload", false),
    GAME_CREATE("setup-game-create", true),
    GAME_EXISTS("setup-game-exists", false),
    HELP_MENU("command-help-menu", false),
    INVALID_ARGUMENT_TYPE("command-invalid-arg-type", false),
    INVALID_ARGUMENTS("command-invalid-args", false),
    INVALID_SENDER("command-invalid-sender", false),
    NO_PERMISSION("command-no-permission", false),
    PLAYER_JOIN("game-player-join", true),
    PLAYER_LEAVE("game-player-leave", true),
    PLAYER_MESSAGE("game-player-message", true),
    PREFIX("prefix", false),
    RELOAD_FAILED("setup-reload-failed", false),
    RELOAD_SUCCESS("setup-reload-success", true),
    SPECIFY_ID("setup-specify-id", false);

    private Battlegrounds plugin;
    private boolean prefix;
    private String message, path;

    EnumMessage(String path, boolean prefix) {
        this.plugin = BattlegroundsPlugin.getPlugin();
        this.message = plugin.getTranslator().getTranslation(path);
        this.path = path;
        this.prefix = prefix;
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
        if (prefix) {
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