package com.matsg.battlegrounds.util;

import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.nms.Title;
import org.bukkit.entity.Player;

public enum EnumTitle {

    COUNTDOWN("title-countdown"),
    FFA_START("title-ffa-start"),
    OBJECTIVE_ELIMINATION("title-objective-elimination"),
    OBJECTIVE_SCORE("title-objective-score"),
    OBJECTIVE_TIME("title-objective-time"),
    TDM_START("title-tdm-start");

    private Battlegrounds plugin;
    private String path;
    private Title title;

    EnumTitle(String path) {
        this.plugin = BattlegroundsPlugin.getPlugin();
        if (path == null || path.length() <= 0) {
            throw new TitleFormatException("Title argument cannot be null");
        }
        String string = plugin.getTranslator().getTranslation(path);
        String[] split = string.split(",");
        if (split.length <= 4) {
            throw new TitleFormatException("Invalid title format \"" + string + "\"");
        }
        try {
            String title = split[0];
            String subTitle = split[1];
            int fadeIn = Integer.parseInt(split[2]);
            int time = Integer.parseInt(split[3]);
            int fadeOut = Integer.parseInt(split[4]);

            this.title = new Title(title, subTitle, fadeIn, time, fadeOut);
        } catch (Exception e) {
            throw new TitleFormatException("An error occurred while formatting the title");
        }
        this.path = path;
    }

    public String getMessage() {
        return title.getMessage();
    }

    public String getMessage(Placeholder... placeholders) {
        return title.getMessage(placeholders);
    }

    public Title getTitle() {
        return title;
    }

    public void send(Player player, Placeholder... placeholders) {
        title.send(player, placeholders);
    }

    public String toString() {
        return title.toString() + "@" + path;
    }
}