package com.matsg.battlegrounds.game.mode.tdm;

import com.matsg.battlegrounds.api.storage.Yaml;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.Team;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.gui.scoreboard.AbstractScoreboard;
import com.matsg.battlegrounds.gui.scoreboard.ScoreboardBuilder;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Map;
import java.util.Set;

public class TDMScoreboard extends AbstractScoreboard {

    public TDMScoreboard(Game game, Yaml yaml) {
        this.game = game;
        this.scoreboardId = "tdm";

        layout.putAll(getScoreboardLayout(yaml.getConfigurationSection("scoreboard.layout")));

        for (String world : yaml.getString("scoreboard.worlds").split(",")) {
            if (world.equals("*")) {
                worlds.clear();
                worlds.addAll(plugin.getServer().getWorlds());
                break;
            }
            worlds.add(plugin.getServer().getWorld(world));
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
                index = Integer.parseInt(line.substring(4, line.length()));
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

    public void addTeams(ScoreboardBuilder builder) {
        for (Team team : game.getGameMode().getTeams()) {
            org.bukkit.scoreboard.Team sbTeam = builder.addTeam(team.getName());
            scoreboardTeams.add(sbTeam);
            sbTeam.setNameTagVisibility(NameTagVisibility.HIDE_FOR_OTHER_TEAMS);
            for (GamePlayer gamePlayer : team.getPlayers()) {
                sbTeam.addEntry(gamePlayer.getName());
            }
        }
    }

    public Scoreboard buildScoreboard(Map<String, String> layout, Scoreboard scoreboard, GamePlayer gamePlayer) {
        return scoreboard == null || scoreboard.getObjective(DisplaySlot.SIDEBAR) == null ||
                !scoreboard.getObjective(DisplaySlot.SIDEBAR).getCriteria().equals(scoreboardId) ? getNewScoreboard(layout, getPlaceholders(gamePlayer)) : updateScoreboard(layout, scoreboard, getPlaceholders(gamePlayer));
    }

    private Placeholder[] getPlaceholders(GamePlayer gamePlayer) {
        return new Placeholder[] {
                new Placeholder("bg_date", getDate()),
                new Placeholder("bg_gamemode", game.getGameMode().getName()),
                new Placeholder("bg_player_kills", gamePlayer.getKills()),
                new Placeholder("bg_time", game.getTimeControl().formatTime()) };
    }
}
