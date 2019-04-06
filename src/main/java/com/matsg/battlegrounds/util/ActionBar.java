package com.matsg.battlegrounds.util;

import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.util.Placeholder;
import org.bukkit.entity.Player;

public enum ActionBar {

    CHANGE_LOADOUT("actionbar-change-loadout"),
    DOOR("actionbar-door"),
    LEAVE_ARENA("actionbar-leave-arena"),
    PERKMACHINE_SOLD_OUT("actionbar-perkmachine-sold-out"),
    POINTS_DEDUCT("actionbar-points-deduct"),
    POINTS_INCREASE("actionbar-points-increase"),
    SAME_LOADOUT("actionbar-same-loadout"),
    UNSUFFICIENT_POINTS("actionbar-unsufficient-points");

    private Battlegrounds plugin;
    private MessageHelper messageHelper;
    private String message, path;

    ActionBar(String path) {
        this.path = path;
        this.plugin = BattlegroundsPlugin.getPlugin();
        this.messageHelper = new MessageHelper();
        this.message = messageHelper.create(path);
    }

    public String getMessage() {
        return message;
    }

    private String replace(Placeholder... placeholders) {
        return messageHelper.create(message, placeholders);
    }

    public void send(Player player, Placeholder... placeholders) {
        plugin.getVersion().sendActionBar(player, replace(placeholders));
    }

    public String toString() {
        return message + "@" + path;
    }
}
