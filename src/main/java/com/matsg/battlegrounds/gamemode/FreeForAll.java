package com.matsg.battlegrounds.gamemode;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.config.Yaml;
import com.matsg.battlegrounds.api.event.GameEndEvent;
import com.matsg.battlegrounds.api.event.GamePlayerDeathEvent.DeathCause;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.GameScoreboard;
import com.matsg.battlegrounds.api.game.Spawn;
import com.matsg.battlegrounds.api.game.Team;
import com.matsg.battlegrounds.api.gamemode.Objective;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.api.player.Hitbox;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.game.BattleTeam;
import com.matsg.battlegrounds.gamemode.objective.EliminationObjective;
import com.matsg.battlegrounds.gamemode.objective.ScoreObjective;
import com.matsg.battlegrounds.gamemode.objective.TimeObjective;
import com.matsg.battlegrounds.gui.scoreboard.AbstractScoreboard;
import com.matsg.battlegrounds.gui.scoreboard.ScoreboardBuilder;
import com.matsg.battlegrounds.util.EnumTitle;
import com.matsg.battlegrounds.util.Message;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.World;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FreeForAll extends AbstractGameMode {

    private boolean scoreboardEnabled;
    private Color color;
    private double minSpawnDistance;
    private int killsToWin, lives;
    private String[] endMessage;

    public FreeForAll(Battlegrounds plugin, Game game, Yaml yaml) {
        super(plugin, game, Message.create(TranslationKey.FFA_NAME), Message.create(TranslationKey.FFA_SHORT), yaml);
        this.color = getConfigColor();
        this.killsToWin = yaml.getInt("kills-to-win");
        this.lives = yaml.getInt("lives");
        this.minSpawnDistance = yaml.getDouble("minimum-spawn-distance");
        this.scoreboardEnabled = yaml.getBoolean("scoreboard.enabled");
        this.timeLimit = yaml.getInt("time-limit");

        List<String> endMessage = yaml.getStringList("end-message");
        this.endMessage = endMessage.toArray(new String[endMessage.size()]);

        objectives.add(new EliminationObjective());
        objectives.add(new ScoreObjective(killsToWin));
        objectives.add(new TimeObjective(timeLimit));
    }

    public void addPlayer(GamePlayer gamePlayer) {
        if (getTeam(gamePlayer) != null) {
            return;
        }
        Team team = new BattleTeam(0, gamePlayer.getName(), color, ChatColor.WHITE);
        team.addPlayer(gamePlayer);
        teams.add(team);
    }

    private Color getConfigColor() {
        String[] array = yaml.getString("armor-color").split(",");
        return Color.fromRGB(Integer.parseInt(array[0]), Integer.parseInt(array[1]), Integer.parseInt(array[2]));
    }

    public Spawn getRespawnPoint(GamePlayer gamePlayer) {
        return game.getArena().getRandomSpawn(minSpawnDistance);
    }

    public GameScoreboard getScoreboard() {
        return scoreboardEnabled ? new FFAScoreboard(game, yaml) : null;
    }

    public void onDeath(GamePlayer gamePlayer, DeathCause deathCause) {
        game.getPlayerManager().broadcastMessage(Message.createSimple(plugin.getTranslator().getTranslation(deathCause.getMessagePath()),
                new Placeholder("bg_player", gamePlayer.getName())));
        handleDeath(gamePlayer);
    }

    public void onKill(GamePlayer gamePlayer, GamePlayer killer, Weapon weapon, Hitbox hitbox) {
        game.getPlayerManager().broadcastMessage(Message.create(getKillMessageKey(hitbox),
                new Placeholder("bg_killer", killer.getName()),
                new Placeholder("bg_player", gamePlayer.getName()),
                new Placeholder("bg_weapon", weapon.getName())));

        handleDeath(gamePlayer);
        killer.addExp(100);
        killer.setKills(killer.getKills() + 1);
        killer.getTeam().setScore(killer.getTeam().getScore() + 1);
        game.getPlayerManager().updateExpBar(killer);

        Objective objective = getReachedObjective();

        if (objective != null) {
            game.callEvent(new GameEndEvent(game, objective, getTopTeam(), getSortedTeams()));
            game.stop();
        }
    }

    public void onStart() {
        super.onStart();
        for (GamePlayer gamePlayer : game.getPlayerManager().getPlayers()) {
            gamePlayer.setLives(lives);
            EnumTitle.FFA_START.send(gamePlayer.getPlayer());
        }
    }

    public void onStop() {
        List<Team> teams = getSortedTeams();
        Objective objective = getReachedObjective();
        Placeholder[] placeholders = new Placeholder[] {
                new Placeholder("bg_first", teams.size() > 0 && teams.get(0) != null ? teams.get(0).getPlayers().iterator().next().getName() : "---"),
                new Placeholder("bg_first_score", teams.size() > 0 && teams.get(0) != null ? teams.get(0).getPlayers().iterator().next().getKills() : 0),
                new Placeholder("bg_second", teams.size() > 1 && teams.get(1) != null ? teams.get(1).getPlayers().iterator().next().getName() : "---"),
                new Placeholder("bg_second_score", teams.size() > 1 && teams.get(1) != null ? teams.get(1).getPlayers().iterator().next().getKills() : 0),
                new Placeholder("bg_third", teams.size() > 2 && teams.get(2) != null ? teams.get(2).getPlayers().iterator().next().getName() : "---"),
                new Placeholder("bg_third_score", teams.size() > 2 && teams.get(2) != null ? teams.get(2).getPlayers().iterator().next().getKills() : 0)
        };

        for (String message : endMessage) {
            game.getPlayerManager().broadcastMessage(Message.create(TranslationKey.PREFIX) + Message.createSimple(message, placeholders));
        }

        for (Team team : teams) {
            Result result = Result.getResult(team, getSortedTeams());
            if (result != null) {
                for (GamePlayer gamePlayer : team.getPlayers()) {
                    objective.getTitle().send(gamePlayer.getPlayer(), new Placeholder("bg_result", plugin.getTranslator().getTranslation(result.getTranslationKey().getPath())));
                }
            }
        }

        this.teams.clear();
    }

    public void removePlayer(GamePlayer gamePlayer) {
        Team team = getTeam(gamePlayer);
        if (team == null) {
            return;
        }
        teams.remove(team);
    }

    public void spawnPlayers(GamePlayer... players) {
        for (Team team : teams) {
            for (GamePlayer gamePlayer : team.getPlayers()) {
                Spawn spawn = game.getArena().getRandomSpawn();
                spawn.setGamePlayer(gamePlayer);
                gamePlayer.getPlayer().teleport(spawn.getLocation());
            }
        }
    }

    private class FFAScoreboard extends AbstractScoreboard {

        private FFAScoreboard(Game game, Yaml yaml) {
            this.game = game;
            this.scoreboardId = "ffa";
            this.worlds = new HashSet<>();

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
                for (GamePlayer gamePlayer : game.getPlayerManager().getPlayers()) {
                    builder.addLine(DisplaySlot.SIDEBAR, gamePlayer.getKills(), gamePlayer.getName());
                }
            }
        }

        public void addTeams(ScoreboardBuilder builder) {
            for (Team team : game.getGameMode().getTeams()) {
                org.bukkit.scoreboard.Team sbTeam = builder.addTeam(team.getName());
                sbTeam.setNameTagVisibility(NameTagVisibility.NEVER);
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
                    new Placeholder("bg_time", game.getTimeControl().formatTime())
            };
        }
    }
}