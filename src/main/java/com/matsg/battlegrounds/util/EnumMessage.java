package com.matsg.battlegrounds.util;

import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.util.Message;
import com.matsg.battlegrounds.api.util.Placeholder;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public enum EnumMessage implements Message {

    ARENA_CONFIRM_REMOVE("setup-arena-confirm-remove", false),
    ARENA_CREATE("setup-arena-create", true),
    ARENA_EXISTS("setup-arena-exists", false),
    ARENA_NOT_EXISTS("setup-arena-not-exists", false),
    ARENA_REMOVE("setup-arena-remove", false),
    COMMAND_ERROR("command-error", false),
    COUNTDOWN_NOTE("game-countdown-note", true),
    DESCRIPTION_ADDSPAWN("command-description-addspawn", false),
    DESCRIPTION_CREATEARENA("command-description-createarena", false),
    DESCRIPTION_CREATEGAME("command-description-creategame", false),
    DESCRIPTION_RELOAD("command-description-reload", false),
    DESCRIPTION_REMOVEARENA("command-description-removearena", false),
    DESCRIPTION_REMOVEGAME("command-description-removegame", false),
    DESCRIPTION_SETGAMESIGN("command-description-setgamesign", false),
    DESCRIPTION_SETLOBBY("command-description-setlobby", false),
    GAME_CONFIRM_REMOVE("setup-game-confirm-remove", false),
    GAME_CREATE("setup-game-create", true),
    GAME_EXISTS("setup-game-exists", false),
    GAME_NOT_EXISTS("setup-game-not-exists", false),
    GAME_REMOVE("setup-game-remove", true),
    GAMESIGN_SET("setup-gamesign-set", true),
    HELP_MENU("command-help-menu", false),
    INVALID_ARGUMENT_TYPE("command-invalid-arg-type", false),
    INVALID_ARGUMENTS("command-invalid-args", false),
    INVALID_BLOCK("setup-invalid-block", false),
    INVALID_SENDER("command-invalid-sender", false),
    LOBBY_SET("setup-lobby-set", true),
    NO_PERMISSION("command-no-permission", false),
    NO_SELECTION("setup-no-selection", false),
    PLAYER_JOIN("game-player-join", true),
    PLAYER_LEAVE("game-player-leave", true),
    PLAYER_MESSAGE("game-player-message", true),
    PREFIX("prefix", false),
    RELOAD_FAILED("setup-reload-failed", false),
    RELOAD_SUCCESS("setup-reload-success", true),
    SPAWN_ADD("setup-spawn-add", true),
    SPECIFY_ID("setup-specify-id", false),
    SPECIFY_NAME("setup-specify-name", false),
    STAT_ACCURACY("item-stat-accuracy", false),
    STAT_DAMAGE("item-stat-damage", false),
    STAT_FIRERATE("item-stat-firerate", false),
    STAT_RANGE("item-stat-range", false),
    TYPE_KNIFE("item-type-knife", false);

    private Battlegrounds plugin;
    private boolean prefix;
    private String message, path;

    EnumMessage(String path, boolean prefix) {
        this.plugin = BattlegroundsPlugin.getPlugin();
        this.message = plugin.getTranslator().getTranslation(path);
        this.path = path;
        this.prefix = prefix;
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