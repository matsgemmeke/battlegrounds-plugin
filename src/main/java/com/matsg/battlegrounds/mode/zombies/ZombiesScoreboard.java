package com.matsg.battlegrounds.mode.zombies;

import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.gui.scoreboard.AbstractScoreboard;
import com.matsg.battlegrounds.gui.scoreboard.ScoreboardBuilder;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Map;

public class ZombiesScoreboard extends AbstractScoreboard {

    private Zombies zombies;

    public ZombiesScoreboard(Game game, Zombies zombies) {
        this.game = game;
        this.zombies = zombies;
        this.scoreboardId = "zombies";

        layout.putAll(zombies.getConfig().getScoreboardLayout());

        for (String world : zombies.getConfig().getString("zombies-scoreboard.worlds").split(",")) {
            if (world.equals("*")) {
                worlds.clear();
                worlds.addAll(plugin.getServer().getWorlds());
                break;
            }
            worlds.add(plugin.getServer().getWorld(world));
        }
    }

    public void addTeams(ScoreboardBuilder builder) {
        int index = Integer.MIN_VALUE;

        for (String line : layout.keySet()) {
            if (layout.get(line).contains("%bg_players%")) {
                index = Integer.parseInt(line.substring(4));
                break;
            }
        }

        if (index > Integer.MIN_VALUE) {
            builder.removeLine(DisplaySlot.SIDEBAR, index);
            for (GamePlayer gamePlayer : game.getPlayerManager().getPlayers()) {
                builder.addLine(DisplaySlot.SIDEBAR, index, "  " + gamePlayer.getState().getChatColor() + gamePlayer.getName() + ": " + gamePlayer.getPoints());
            }
        }
    }

    public Scoreboard buildScoreboard(Map<String, String> layout, Scoreboard scoreboard, GamePlayer gamePlayer) {
        return scoreboard == null || scoreboard.getObjective(DisplaySlot.SIDEBAR) == null ||
                !scoreboard.getObjective(DisplaySlot.SIDEBAR).getCriteria().equals(scoreboardId) ? getNewScoreboard(layout, getPlaceholders(gamePlayer)) : updateScoreboard(layout, scoreboard, getPlaceholders(gamePlayer));
    }

    private Placeholder[] getPlaceholders(GamePlayer gamePlayer) {
        return new Placeholder[] {
                new Placeholder("bg_arena", game.getArena().getName()),
                new Placeholder("bg_date", getDate()),
                new Placeholder("bg_headshots", gamePlayer.getHeadshots()),
                new Placeholder("bg_kills", gamePlayer.getKills()),
                new Placeholder("bg_mobs", game.getMobManager().getMobs().size()),
                new Placeholder("bg_round", zombies.getWave().getRound())
        };
    }
}
