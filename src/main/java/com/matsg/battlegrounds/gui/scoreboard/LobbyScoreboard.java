package com.matsg.battlegrounds.gui.scoreboard;

import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.util.ScoreboardBuilder;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LobbyScoreboard implements GameScoreboard {

    private static Battlegrounds plugin = BattlegroundsPlugin.getPlugin();
    private Game game;
    private int countdown;
    private Map<String, String> layout;
    private Set<World> worlds;
    private String scoreboardId;

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

    private void addLayout(ScoreboardBuilder builder, Map<String, String> layout) {
        for (String line : layout.keySet()) {
            if (line.contains("line-") && layout.get(line).length() > 0) {
                builder.setLine(DisplaySlot.SIDEBAR, Integer.parseInt(line.substring(5, line.length())),
                        ChatColor.translateAlternateColorCodes('&', Placeholder.replace(layout.get(line), getPlaceholders())));
            }
        }
    }

    private Scoreboard buildScoreboard(Map<String, String> layout, Scoreboard scoreboard) {
        return scoreboard == null || scoreboard.getObjective(DisplaySlot.SIDEBAR) == null
                || !scoreboard.getObjective(DisplaySlot.SIDEBAR).getCriteria().equals(scoreboardId) ? getNewScoreboard(layout) : updateScoreboard(layout, scoreboard);
    }

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

    private String getDate() {
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

    private Placeholder[] getPlaceholders() {
        return new Placeholder[] {
                new Placeholder("bg_arena", game.getArena() != null ? game.getArena().getName() : "---"),
                new Placeholder("bg_countdown", countdown),
                new Placeholder("bg_date", getDate()),
                new Placeholder("bg_gamemode", game.getGameMode().getSimpleName()),
                new Placeholder("bg_players", game.getPlayerManager().getPlayers().size())
        };
    }

    private Scoreboard getNewScoreboard(Map<String, String> layout) {
        ScoreboardBuilder builder = new ScoreboardBuilder();
        builder.addObjective(scoreboardId, "dummy", DisplaySlot.SIDEBAR);
        builder.setTitle(DisplaySlot.SIDEBAR, ChatColor.translateAlternateColorCodes('&', layout.get("title")));
        addLayout(builder, layout);
        return builder.build();
    }

    private Scoreboard updateScoreboard(Map<String, String> layout, Scoreboard scoreboard) {
        ScoreboardBuilder builder = new ScoreboardBuilder(scoreboard).clearLines();
        addLayout(builder, layout);
        return builder.build();
    }
}