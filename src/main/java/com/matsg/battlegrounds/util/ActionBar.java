package com.matsg.battlegrounds.util;

import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.util.Message;
import com.matsg.battlegrounds.api.util.Placeholder;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public enum ActionBar implements Message {

    CHANGE_LOADOUT("actionbar-change-loadout"),
    LEAVE_ARENA("actionbar-leave-arena"),
    SAME_LOADOUT("actionbar-same-loadout");

    private Battlegrounds plugin;
    private String message, path;

    ActionBar(String path) {
        this.plugin = BattlegroundsPlugin.getPlugin();
        this.message = plugin.getTranslator().getTranslation(path);
        this.path = path;
    }

    public String getMessage() {
        return message;
    }

    public String getMessage(Placeholder... placeholders) {
        return replace(placeholders);
    }

    private String replace(Placeholder... placeholders) {
        return ChatColor.translateAlternateColorCodes('&', Placeholder.replace(message, placeholders));
    }

    public void send(Player player, Placeholder... placeholders) {
        plugin.getVersion().sendActionBar(player, replace(placeholders));
    }

    public String toString() {
        return message + "@" + path;
    }
}