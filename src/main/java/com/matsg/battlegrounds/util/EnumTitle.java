package com.matsg.battlegrounds.util;

import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.nms.Title;
import org.bukkit.entity.Player;

public enum EnumTitle {

    COUNTDOWN("title-countdown"),
    FFA_START("title-ffa-start"),
    OBJECTIVE_ELIMINATION("title-objective-elimination"),
    OBJECTIVE_SCORE("title-objective-score"),
    OBJECTIVE_TIME("title-objective-time"),
    TDM_START("title-tdm-start"),
    ZOMBIES_START("title-zombies-start");

    private String path;
    private Title title;
    private Translator translator;

    EnumTitle(String path) {
        this.translator = BattlegroundsPlugin.getPlugin().getTranslator();

        if (path == null || path.length() <= 0) {
            throw new TitleFormatException("Title argument cannot be null");
        }
        String string = translator.translate(path);
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
