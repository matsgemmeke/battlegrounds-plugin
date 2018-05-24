package com.matsg.battlegrounds.gui.scoreboard;

import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.Team;
import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.util.ScoreboardBuilder;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team.Option;
import org.bukkit.scoreboard.Team.OptionStatus;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class AbstractScoreboard implements GameScoreboard {

    protected static Battlegrounds plugin = BattlegroundsPlugin.getPlugin();
    protected Game game;
    protected Map<String, String> layout;
    protected Set<World> worlds;
    protected String scoreboardId;

    public String getScoreboardId() {
        return scoreboardId;
    }

    public Set<World> getWorlds() {
        return worlds;
    }

    public void addLayout(ScoreboardBuilder builder, Map<String, String> layout, Placeholder... placeholders) {
        for (String line : layout.keySet()) {
            if (line.contains("line-") && layout.get(line).length() > 0) {
                builder.setLine(DisplaySlot.SIDEBAR, Integer.parseInt(line.substring(5, line.length())),
                        ChatColor.translateAlternateColorCodes('&', Placeholder.replace(layout.get(line), placeholders)));
            }
        }
    }

    public void addTeams(ScoreboardBuilder builder) {
        for (Team team : game.getGameMode().getTeams()) {
            org.bukkit.scoreboard.Team sbTeam = builder.addTeam(team.getName());
            sbTeam.setOption(Option.NAME_TAG_VISIBILITY, OptionStatus.FOR_OWN_TEAM);
            for (GamePlayer gamePlayer : team.getPlayers()) {
                sbTeam.addEntry(gamePlayer.getName());
            }
        }
    }

    public abstract Scoreboard buildScoreboard(Map<String, String> layout, Scoreboard scoreboard);

    public Scoreboard createScoreboard() {
        return buildScoreboard(layout, null);
    }

    public void display(Game game) {
        for (GamePlayer gamePlayer : game.getPlayerManager().getPlayers()) {
            display(gamePlayer.getPlayer());
        }
    }

    public void display(Player player) {
        player.setScoreboard(buildScoreboard(layout, player.getScoreboard()));
    }

    protected String getDate() {
        StringBuilder builder = new StringBuilder();
        if (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) < 10) {
            builder.append("0");
        }
        builder.append(Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "/");
        if ((Calendar.getInstance().get(Calendar.MONTH) + 1) < 10) {
            builder.append("0");
        }
        builder.append((Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" + Calendar.getInstance().get(Calendar.YEAR));
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

    protected Scoreboard updateScoreboard(Map<String, String> layout, Scoreboard scoreboard, Placeholder... placeholders) {
        ScoreboardBuilder builder = new ScoreboardBuilder(scoreboard).clearLines();
        addLayout(builder, layout, placeholders);
        return builder.build();
    }
}