package com.matsg.battlegrounds.gamemode.tdm;

import com.matsg.battlegrounds.api.config.Yaml;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.Team;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.gui.scoreboard.AbstractScoreboard;
import com.matsg.battlegrounds.util.ScoreboardBuilder;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TDMScoreboard extends AbstractScoreboard {

    public TDMScoreboard(Game game, Yaml yaml) {
        this.game = game;
        this.layout = getScoreboardLayout(yaml.getConfigurationSection("scoreboard.layout"));
        this.scoreboardId = "tdm";
        this.worlds = new HashSet<>();

        for (String world : yaml.getString("scoreboard.worlds").split(",")) {
            if (world.equals("*")) {
                worlds.clear();
                worlds.addAll(plugin.getServer().getWorlds());
                break;
            } else {
                worlds.add(plugin.getServer().getWorld(world));
            }
        }
    }

    public String getScoreboardId() {
        return scoreboardId;
    }

    public Set<World> getWorlds() {
        return worlds;
    }

    public void addLayout(ScoreboardBuilder builder, Map<String, String> layout, Placeholder... placeholders) {
        super.addLayout(builder, layout, placeholders);
        int index = Integer.MIN_VALUE;

        for (String line : layout.keySet()) {
            if (layout.get(line).contains("%bg_scores%")) {
                index = Integer.parseInt(line.substring(5, line.length()));
                break;
            }
        }

        if (index > Integer.MIN_VALUE) {
            builder.removeLine(DisplaySlot.SIDEBAR, index);
            for (Team team : game.getGameMode().getTeams()) {
                builder.addLine(DisplaySlot.SIDEBAR, index, team.getChatColor() + team.getName() + ": " + ChatColor.WHITE + team.getScore());
            }
        }
    }

    public Scoreboard buildScoreboard(Map<String, String> layout, Scoreboard scoreboard) {
        return scoreboard == null || scoreboard.getObjective(DisplaySlot.SIDEBAR) == null
                || !scoreboard.getObjective(DisplaySlot.SIDEBAR).getCriteria().equals(scoreboardId) ? getNewScoreboard(layout, getPlaceholders()) : updateScoreboard(layout, scoreboard, getPlaceholders());
    }

    private Placeholder[] getPlaceholders() {
        return new Placeholder[] {
                new Placeholder("bg_date", getDate()),
                new Placeholder("bg_gamemode", game.getGameMode().getName()),
                new Placeholder("bg_time", game.getTimeControl().formatTime())
        };
    }
}