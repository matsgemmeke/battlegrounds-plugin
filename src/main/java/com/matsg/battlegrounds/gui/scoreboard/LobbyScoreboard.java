package com.matsg.battlegrounds.gui.scoreboard;

import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.util.Placeholder;
import org.bukkit.World;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LobbyScoreboard extends AbstractScoreboard {

    private int countdown;

    public LobbyScoreboard(Game game) {
        this.game = game;
        this.layout = plugin.getBattlegroundsConfig().getLobbyScoreboardLayout();
        this.scoreboardId = "lobby";
        this.worlds = new HashSet<>();

        for (String world : plugin.getBattlegroundsConfig().lobbyScoreboard.getString("worlds").split(",")) {
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

    public void setCountdown(int countdown) {
        this.countdown = countdown;
    }

    public Scoreboard buildScoreboard(Map<String, String> layout, Scoreboard scoreboard) {
        return scoreboard == null || scoreboard.getObjective(DisplaySlot.SIDEBAR) == null
                || !scoreboard.getObjective(DisplaySlot.SIDEBAR).getCriteria().equals(scoreboardId) ? getNewScoreboard(layout, getPlaceholders()) : updateScoreboard(layout, scoreboard, getPlaceholders());
    }

    private Placeholder[] getPlaceholders() {
        return new Placeholder[] {
                new Placeholder("bg_arena", game.getArena() != null ? game.getArena().getName() : "---"),
                new Placeholder("bg_countdown", countdown),
                new Placeholder("bg_date", getDate()),
                new Placeholder("bg_gamemode", game.getGameMode().getSimpleName()),
                new Placeholder("bg_players", game.getPlayerManager().getPlayers().size())
        };
    }
}