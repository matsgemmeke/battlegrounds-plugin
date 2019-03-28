package com.matsg.battlegrounds.gamemode.cbt;

import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.Team;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.gui.scoreboard.AbstractScoreboard;
import com.matsg.battlegrounds.gui.scoreboard.ScoreboardBuilder;
import org.bukkit.World;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Map;
import java.util.Set;

public class CBTScoreboard extends AbstractScoreboard {

    public CBTScoreboard(Game game) {
        this.game = game;
        this.scoreboardId = "cbt";

        layout.put("title", scoreboardId);
    }

    public String getScoreboardId() {
        return scoreboardId;
    }

    public Set<World> getWorlds() {
        return worlds;
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
                !scoreboard.getObjective(DisplaySlot.SIDEBAR).getCriteria().equals(scoreboardId) ? getNewScoreboard(layout) : updateScoreboard(layout, scoreboard);
    }
}
