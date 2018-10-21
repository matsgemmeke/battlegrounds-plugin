package com.matsg.battlegrounds.gamemode;

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
import com.matsg.battlegrounds.util.EnumMessage;
import com.matsg.battlegrounds.util.EnumTitle;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TeamDeathmatch extends AbstractGameMode {

    private boolean scoreboardEnabled;
    private double minSpawnDistance;
    private int killsToWin, lives;

    public TeamDeathmatch(Battlegrounds plugin, Game game, Yaml yaml) {
        super(plugin, game, EnumMessage.TDM_NAME.getMessage(), EnumMessage.TDM_SHORT.getMessage(), yaml);
        this.killsToWin = yaml.getInt("kills-to-win");
        this.lives = yaml.getInt("lives");
        this.minSpawnDistance = yaml.getDouble("minimum-spawn-distance");
        this.scoreboardEnabled = yaml.getBoolean("scoreboard.enabled");
        this.teams.addAll(getConfigTeams());
        this.timeLimit = yaml.getInt("time-limit");

        objectives.add(new EliminationObjective());
        objectives.add(new ScoreObjective(killsToWin));
        objectives.add(new TimeObjective(timeLimit));
    }

    public void addPlayer(GamePlayer gamePlayer) {
        Team team = getEmptiestTeam();
        team.addPlayer(gamePlayer);
        gamePlayer.sendMessage(EnumMessage.TEAM_ASSIGNMENT.getMessage(new Placeholder("bg_team", team.getChatColor() + team.getName())));
    }

    private List<Team> getConfigTeams() {
        List<Team> list = new ArrayList<>();
        for (String teamId : yaml.getConfigurationSection("teams").getKeys(false)) {
            ConfigurationSection section = yaml.getConfigurationSection("teams." + teamId);
            String[] array = section.getString("armor-color").split(",");
            Color color = Color.fromRGB(Integer.parseInt(array[0]), Integer.parseInt(array[1]), Integer.parseInt(array[2]));

            list.add(new BattleTeam(Integer.parseInt(teamId), section.getString("name"), color, ChatColor.getByChar(section.getString("chatcolor").charAt(0))));
        }
        return list;
    }

    private Team getEmptiestTeam() {
        int size = Integer.MAX_VALUE;
        Team emptiestTeam = null;
        for (Team team : teams) {
            if (team.getTeamSize() < size) {
                emptiestTeam = team;
                size = team.getTeamSize();
            }
        }
        return emptiestTeam;
    }

    public Spawn getRespawnPoint(GamePlayer gamePlayer) {
        return game.getArena().getRandomSpawn(getTeam(gamePlayer), minSpawnDistance);
    }

    public GameScoreboard getScoreboard() {
        return scoreboardEnabled ? new TDMScoreboard(game, yaml) : null;
    }

    private Spawn getTeamBase(Team team) {
        for (Spawn spawn : game.getArena().getSpawns()) {
            if (spawn.getTeamId() == team.getId() && spawn.isTeamBase()) {
                return spawn;
            }
        }
        return null;
    }

    public void onDeath(GamePlayer gamePlayer, DeathCause deathCause) {
        game.getPlayerManager().broadcastMessage(
                ChatColor.translateAlternateColorCodes('&', EnumMessage.PREFIX.getMessage() + Placeholder.replace(plugin.getTranslator().getTranslation(deathCause.getMessagePath()),
                new Placeholder("bg_player", getTeam(gamePlayer).getChatColor() + gamePlayer.getName() + ChatColor.WHITE))));
        handleDeath(gamePlayer);
    }

    public void onKill(GamePlayer gamePlayer, GamePlayer killer, Weapon weapon, Hitbox hitbox) {
        game.getPlayerManager().broadcastMessage(getKillMessage(hitbox).getMessage(new Placeholder[] {
                new Placeholder("bg_killer", killer.getTeam().getChatColor() + killer.getName() + ChatColor.WHITE),
                new Placeholder("bg_player", gamePlayer.getTeam().getChatColor() + gamePlayer.getName() + ChatColor.WHITE),
                new Placeholder("bg_weapon", weapon.getName())
        }));
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
        game.getPlayerManager().broadcastMessage(EnumTitle.TDM_START);
        for (GamePlayer gamePlayer : game.getPlayerManager().getPlayers()) {
            gamePlayer.setLives(lives);
        }
    }

    public void onStop() {
        Objective objective = getReachedObjective();

        for (Team team : teams) {
            Result result = Result.getResult(team, getSortedTeams());
            if (result != null) {
                for (GamePlayer gamePlayer : team.getPlayers()) {
                    objective.getTitle().send(gamePlayer.getPlayer(), new Placeholder("bg_result", result.getResultMessage()));
                }
            }
        }

        teams.clear();
        teams = getConfigTeams();
    }

    public void removePlayer(GamePlayer gamePlayer) {
        Team team = getTeam(gamePlayer);
        if (team == null) {
            return;
        }
        team.removePlayer(gamePlayer);
    }

    public void spawnPlayers(GamePlayer... players) {
        for (GamePlayer gamePlayer : players) {
            Spawn spawn = getTeamBase(getTeam(gamePlayer));
            if (spawn == null) {
                spawn = game.getArena().getRandomSpawn(getTeam(gamePlayer));
            }
            gamePlayer.getPlayer().teleport(spawn.getLocation());
        }
    }

    private class TDMScoreboard extends AbstractScoreboard {

        private TDMScoreboard(Game game, Yaml yaml) {
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
}