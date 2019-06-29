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
    POWERUP_ACTIVATE("title-power-up-activate"),
    POWERUP_DEACTIVATE("title-power-up-deactivate"),
    TDM_START("title-tdm-start"),
    ZOMBIES_GAME_OVER("title-zombies-game-over"),
    ZOMBIES_NEW_WAVE("title-zombies-new-wave"),
    ZOMBIES_START("title-zombies-start");

    private int fadeIn;
    private int fadeOut;
    private int time;
    private String path;
    private String subText;
    private String titleText;
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
            titleText = split[0];
            subText = split[1];
            fadeIn = Integer.parseInt(split[2]);
            time = Integer.parseInt(split[3]);
            fadeOut = Integer.parseInt(split[4]);
        } catch (Exception e) {
            throw new TitleFormatException("An error occurred while formatting the title");
        }

        this.path = path;
    }

    public Title getTitle() {
        return createTitle();
    }

    public void send(Player player, Placeholder... placeholders) {
        Title title = createTitle();
        title.setTitleText(translator.createSimpleMessage(titleText, placeholders));
        title.setSubText(translator.createSimpleMessage(subText, placeholders));

        title.send(player);
    }

    public String toString() {
        return createTitle().toString() + "@" + path;
    }

    private Title createTitle() {
        return new Title(titleText, subText, fadeIn, time, fadeOut);
    }
}
