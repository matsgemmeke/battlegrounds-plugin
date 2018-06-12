package com.matsg.battlegrounds.util;

import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.util.Message;
import com.matsg.battlegrounds.api.util.Placeholder;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public enum EnumMessage implements Message {

    ALREADY_PLAYING("game-already-playing", false),
    ARENA_CONFIRM_REMOVE("setup-arena-confirm-remove", false),
    ARENA_CREATE("setup-arena-create", true),
    ARENA_EXISTS("setup-arena-exists", false),
    ARENA_NOT_EXISTS("setup-arena-not-exists", false),
    ARENA_REMOVE("setup-arena-remove", false),
    ARMOR_HELMET("item-armor-helmet", false),
    ARMOR_VEST("item-armor-vest", false),
    CHANGE_LOADOUT("item-change-loadout", false),
    COMMAND_ERROR("command-error", false),
    COMMAND_NOT_ALLOWED("command-not-allowed", false),
    COUNTDOWN_NOTE("game-countdown-note", true),
    CUSTOM_LOADOUT_LOCKED("util-custom-loadout-locked", false),
    DEATH_BURNING("game-death-burning", true),
    DEATH_DROWNING("game-death-suicide", true),
    DEATH_FALLING("game-death-falling", true),
    DEATH_PLAYER_KILL("game-death-player-kill", true),
    DEATH_SUICIDE("game-death-player-kill", true),
    DESCRIPTION_ADDSPAWN("command-description-addspawn", false),
    DESCRIPTION_CREATEARENA("command-description-createarena", false),
    DESCRIPTION_CREATEGAME("command-description-creategame", false),
    DESCRIPTION_JOIN("command-description-join", false),
    DESCRIPTION_LEAVE("command-description-leave", false),
    DESCRIPTION_RELOAD("command-description-reload", false),
    DESCRIPTION_RENAME("command-description-rename", false),
    DESCRIPTION_REMOVEARENA("command-description-removearena", false),
    DESCRIPTION_REMOVEGAME("command-description-removegame", false),
    DESCRIPTION_REMOVESPAWN("command-description-removespawn", false),
    DESCRIPTION_SETGAMESIGN("command-description-setgamesign", false),
    DESCRIPTION_SETLOBBY("command-description-setlobby", false),
    EDIT_LOADOUT("view-edit-loadout", false),
    EDIT_WEAPON("view-edit-weapon", false),
    ENDREASON_ELIMINATION("game-endreason-elimination", false),
    ENDREASON_SCORE("game-endreason-score", false),
    ENDREASON_TIME("game-endreason-time", false),
    GAME_CONFIRM_REMOVE("setup-game-confirm-remove", false),
    FFA_NAME("gamemode-ffa-name", false),
    FFA_SHORT("gamemode-ffa-short", false),
    GAME_CREATE("setup-game-create", true),
    GAME_EXISTS("setup-game-exists", false),
    GAME_NOT_EXISTS("setup-game-not-exists", false),
    GAME_REMOVE("setup-game-remove", true),
    GAMESIGN_SET("setup-gamesign-set", true),
    GO_BACK("view-go-back", false),
    HELP_MENU("command-help-menu", false),
    IN_PROGRESS("game-in-progress", false),
    INVALID_ARGUMENT_TYPE("command-invalid-arg-type", false),
    INVALID_ARGUMENTS("command-invalid-args", false),
    INVALID_BLOCK("setup-invalid-block", false),
    INVALID_LOADOUT("command-invalid-loadout", false),
    INVALID_SENDER("command-invalid-sender", false),
    ITEM_LOCKED("view-item-locked", false),
    LOBBY_SET("setup-lobby-set", true),
    NO_PERMISSION("command-no-permission", false),
    NO_SELECTION("setup-no-selection", false),
    NONE_SELECTED("view-none-selected", false),
    NOT_PLAYING("game-not-playing", false),
    PLAYER_JOIN("game-player-join", true),
    PLAYER_LEAVE("game-player-leave", true),
    PLAYER_MESSAGE("game-player-message", true),
    PREFIX("prefix", false),
    RELOAD_FAILED("setup-reload-failed", false),
    RELOAD_SUCCESS("setup-reload-success", true),
    RENAME_LOADOUT("command-rename-loadout", true),
    RESULT_DEFEAT("game-result-defeat", false),
    RESULT_DRAW("game-result-draw", false),
    RESULT_VICTORY("game-result-victory", false),
    SPAWN_ADD("setup-spawn-add", true),
    SPAWN_NOT_EXISTS("setup-spawn-not-exists", false),
    SPAWN_REMOVE("setup-spawn-remove", true),
    SPAWN_TEAMBASE_EXISTS("setup-spawn-teambase-exists", false),
    SPECIFY_ID("command-specify-id", false),
    SPECIFY_INDEX("command-specify-index", false),
    SPECIFY_LOADOUT("command-specify-loadout", false),
    SPECIFY_NAME("command-specify-name", false),
    SPECIFY_TEAM("command-specify-team", false),
    SPOTS_FULL("game-spots-full", false),
    STAT_ACCURACY("item-stat-accuracy", false),
    STAT_DAMAGE("item-stat-damage", false),
    STAT_FIRERATE("item-stat-firerate", false),
    STAT_RANGE("item-stat-range", false),
    TDM_NAME("gamemode-tdm-name", false),
    TDM_SHORT("gamemode-tdm-short", false),
    TEAM_ASSIGNMENT("game-team-assignment", true),
    TITLE_EDIT_LOADOUT("view-title-edit-loadout", false),
    TITLE_LOADOUT_MANAGER("view-title-loadout-manager", false),
    TITLE_SELECT_LOADOUT("view-title-select-loadout", false),
    TITLE_WEAPONS("view-title-weapons", false),
    TYPE_KNIFE("item-type-knife", false),
    WEAPON_EQUIPMENT("view-weapon-equipment", false),
    WEAPON_KNIFE("view-weapon-knife", false),
    WEAPON_PRIMARY("view-weapon-primary", false),
    WEAPON_SECONDARY("view-weapon-secondary", false);

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
        StringBuilder builder = new StringBuilder();
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