package com.matsg.battlegrounds.util;

import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.api.Translator;
import org.bukkit.entity.Player;

public enum ActionBar {

    CHANGE_LOADOUT("actionbar-change-loadout"),
    DOOR("actionbar-door"),
    ITEMCHEST("actionbar-itemchest"),
    LEAVE_ARENA("actionbar-leave-arena"),
    PERKMACHINE_SOLD_OUT("actionbar-perkmachine-sold-out"),
    POINTS_DEDUCT("actionbar-points-deduct"),
    POINTS_INCREASE("actionbar-points-increase"),
    SAME_LOADOUT("actionbar-same-loadout"),
    UNSUFFICIENT_POINTS("actionbar-unsufficient-points");

    private Battlegrounds plugin;
    private String message, path;
    private Translator translator;

    ActionBar(String path) {
        this.path = path;
        this.plugin = BattlegroundsPlugin.getPlugin();
        this.translator = plugin.getTranslator();
        this.message = translator.translate(path);
    }

    public String getMessage() {
        return message;
    }

    private String replace(Placeholder... placeholders) {
        return translator.translate(path, placeholders);
    }

    public void send(Player player, Placeholder... placeholders) {
        plugin.getVersion().sendActionBar(player, replace(placeholders));
    }

    public String toString() {
        return message + "@" + path;
    }
}
