package com.matsg.battlegrounds.gui.scoreboard;

import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.GameScoreboard;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.Placeholder;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;

public abstract class AbstractScoreboard implements GameScoreboard {

    protected static Battlegrounds plugin = BattlegroundsPlugin.getPlugin();
    protected Game game;
    protected Map<String, String> layout;
    protected Set<Team> scoreboardTeams;
    protected Set<World> worlds;
    protected String scoreboardId;

    public AbstractScoreboard() {
        this.layout = new HashMap<>();
        this.scoreboardTeams = new HashSet<>();
        this.worlds = new HashSet<>();
    }

    public Map<String, String> getLayout() {
        return layout;
    }

    public void setLayout(Map<String, String> layout) {
        this.layout = layout;
    }

    public String getScoreboardId() {
        return scoreboardId;
    }

    public Set<World> getWorlds() {
        return this.worlds;
    }

    public void addLayout(ScoreboardBuilder builder, Map<String, String> layout, Placeholder... placeholders) {
        for (String line : layout.keySet()) {
            if ((line.contains("line")) && layout.get(line).length() > 0) {
                builder.setLine(DisplaySlot.SIDEBAR, Integer.parseInt(line.substring(4, line.length())),
                        ChatColor.translateAlternateColorCodes('&', replace(layout.get(line), placeholders)));
            }
        }
    }

    public abstract void addTeams(ScoreboardBuilder paramScoreboardBuilder);

    public abstract Scoreboard buildScoreboard(Map<String, String> layout, Scoreboard scoreboard, GamePlayer gamePlayer);

    public Scoreboard createScoreboard() {
        return buildScoreboard(layout, null, null);
    }

    public void destroy() {
        for (Team team : scoreboardTeams) {
            team.unregister();
        }
    }

    public void display(Game game) {
        for (GamePlayer gamePlayer : game.getPlayerManager().getPlayers()) {
            display(gamePlayer);
        }
    }

    public void display(GamePlayer gamePlayer) {
        gamePlayer.getPlayer().setScoreboard(buildScoreboard(layout, gamePlayer.getPlayer().getScoreboard(), gamePlayer));
    }

    protected String getDate() {
        StringBuilder builder = new StringBuilder();
        if (Calendar.getInstance().get(5) < 10) {
            builder.append("0");
        }
        builder.append(Calendar.getInstance().get(5) + "/");
        if (Calendar.getInstance().get(2) + 1 < 10) {
            builder.append("0");
        }
        builder.append(Calendar.getInstance().get(2) + 1 + "/" + Calendar.getInstance().get(1));
        return builder.toString();
    }

    protected Scoreboard getNewScoreboard(Map<String, String> layout, Placeholder... placeholders) {
        ScoreboardBuilder builder = new ScoreboardBuilder();
        builder.addObjective(scoreboardId, "dummy", DisplaySlot.SIDEBAR);
        builder.setTitle(DisplaySlot.SIDEBAR, ChatColor.translateAlternateColorCodes('&', layout.get("title")));
        addLayout(builder, layout, placeholders);
        addTeams(builder);
        return builder.build();
    }

    protected Map<String, String> getScoreboardLayout(ConfigurationSection section) {
        Map<String, String> map = new HashMap<>();
        for (String string : section.getKeys(false)) {
            map.put(string, section.getString(string));
        }
        return map;
    }

    private String replace(String arg, Placeholder... placeholders) {
        for (Placeholder placeholder : placeholders) {
            if (arg.contains("%" + placeholder.getIdentifier() + "%")) {
                arg = placeholder.replace(arg);
            }
        }
        return arg;
    }

    protected Scoreboard updateScoreboard(Map<String, String> layout, Scoreboard scoreboard, Placeholder... placeholders) {
        ScoreboardBuilder builder = new ScoreboardBuilder(scoreboard).clearLines();
        addLayout(builder, layout, placeholders);
        addTeams(builder);
        return builder.build();
    }
}
