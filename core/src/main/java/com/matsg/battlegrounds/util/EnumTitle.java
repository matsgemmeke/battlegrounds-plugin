package com.matsg.battlegrounds.util;

import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.Placeholder;
import org.bukkit.entity.Player;

public enum EnumTitle {

    COUNTDOWN("title-countdown"),
    FFA_START("title-ffa-start"),
    OBJECTIVE_ELIMINATION("title-objective-elimination"),
    OBJECTIVE_SCORE("title-objective-score"),
    OBJECTIVE_TIME("title-objective-time"),
    PLAYER_DOWNED("title-player-downed"),
    PLAYER_BEING_REVIVED("title-player-being-revived"),
    PLAYER_REVIVING_PLAYER("title-player-reviving-player"),
    PLAYER_REVIVE_SELF("title-player-revive-self"),
    POWERUP_ACTIVATE("title-power-up-activate"),
    POWERUP_DEACTIVATE("title-power-up-deactivate"),
    TDM_START("title-tdm-start"),
    ZOMBIES_GAME_OVER("title-zombies-game-over"),
    ZOMBIES_NEW_WAVE("title-zombies-new-wave"),
    ZOMBIES_START("title-zombies-start");

    private Battlegrounds plugin;
    private String path;

    EnumTitle(String path) {
        this.plugin = BattlegroundsPlugin.getPlugin();
        this.path = path;
    }

    public Title getTitle() {
        return createTitle();
    }

    public void send(Player player, Placeholder... placeholders) {
        Title title = createTitle();
        title.setTitleText(plugin.getTranslator().createSimpleMessage(title.getTitleText(), placeholders));
        title.setSubText(plugin.getTranslator().createSimpleMessage(title.getSubText(), placeholders));

        title.send(player);
    }

    public String toString() {
        return createTitle().toString() + "@" + path;
    }

    private Title createTitle() {
        if (path == null || path.length() <= 0) {
            throw new TitleFormatException("Title argument cannot be null");
        }

        String string = plugin.getTranslator().translate(path);
        String[] split = string.split(",");

        if (split.length <= 4) {
            throw new TitleFormatException("Invalid title format \"" + string + "\"");
        }

        int fadeIn;
        int fadeOut;
        int time;
        String subText;
        String titleText;

        try {
            titleText = split[0];
            subText = split[1];
            fadeIn = Integer.parseInt(split[2]);
            time = Integer.parseInt(split[3]);
            fadeOut = Integer.parseInt(split[4]);
        } catch (Exception e) {
            throw new TitleFormatException("An error occurred while formatting the title");
        }

        return new Title(titleText, subText, fadeIn, time, fadeOut);
    }
}
