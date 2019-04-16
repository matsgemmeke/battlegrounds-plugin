package com.matsg.battlegrounds.game.mode.tdm;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.storage.Yaml;
import com.matsg.battlegrounds.api.event.GameEndEvent;
import com.matsg.battlegrounds.api.event.GamePlayerDeathEvent.DeathCause;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.GameScoreboard;
import com.matsg.battlegrounds.api.game.Spawn;
import com.matsg.battlegrounds.api.game.Team;
import com.matsg.battlegrounds.api.game.Objective;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.entity.Hitbox;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.game.BattleTeam;
import com.matsg.battlegrounds.game.mode.ArenaGameMode;
import com.matsg.battlegrounds.game.mode.GameModeType;
import com.matsg.battlegrounds.game.mode.Result;
import com.matsg.battlegrounds.game.objective.EliminationObjective;
import com.matsg.battlegrounds.game.objective.ScoreObjective;
import com.matsg.battlegrounds.game.objective.TimeObjective;
import com.matsg.battlegrounds.util.EnumTitle;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class TeamDeathmatch extends ArenaGameMode {

    private boolean scoreboardEnabled;
    private double minSpawnDistance;
    private int killsToWin, lives, timeLimit;

    public TeamDeathmatch(Battlegrounds plugin, Game game, Yaml yaml) {
        super(plugin, game, yaml);
        this.killsToWin = yaml.getInt("kills-to-win");
        this.lives = yaml.getInt("lives");
        this.minSpawnDistance = yaml.getDouble("minimum-spawn-distance");
        this.scoreboardEnabled = yaml.getBoolean("scoreboard.enabled");
        this.teams.addAll(getConfigTeams());
        this.timeLimit = yaml.getInt("time-limit");
        
        setName(messageHelper.create(TranslationKey.TDM_NAME));
        setShortName(messageHelper.create(TranslationKey.TDM_SHORT));

        objectives.add(new EliminationObjective());
        objectives.add(new ScoreObjective(killsToWin));
        objectives.add(new TimeObjective(timeLimit));
    }

    public GameModeType getType() {
        return GameModeType.TEAM_DEATHMATCH;
    }

    public void addPlayer(GamePlayer gamePlayer) {
        Team team = getEmptiestTeam();
        team.addPlayer(gamePlayer);
        gamePlayer.sendMessage(messageHelper.create(TranslationKey.TEAM_ASSIGNMENT, new Placeholder("bg_team", team.getChatColor() + team.getName())));
    }

    private List<Team> getConfigTeams() {
        List<Team> list = new ArrayList<>();
        for (String teamId : config.getConfigurationSection("teams").getKeys(false)) {
            ConfigurationSection section = config.getConfigurationSection("teams." + teamId);
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
        return game.getArena().getRandomSpawn(gamePlayer.getTeam().getId());
    }

    public GameScoreboard getScoreboard() {
        return scoreboardEnabled ? new TDMScoreboard(game, config) : null;
    }

    public void onDeath(GamePlayer gamePlayer, DeathCause deathCause) {
        game.getPlayerManager().broadcastMessage(ChatColor.translateAlternateColorCodes('&', messageHelper.create(TranslationKey.PREFIX) +
                messageHelper.create(deathCause.getMessagePath(), new Placeholder("bg_player", gamePlayer.getTeam().getChatColor() + gamePlayer.getName() + ChatColor.WHITE))));
        handleDeath(gamePlayer);
    }

    public void onKill(GamePlayer gamePlayer, GamePlayer killer, Weapon weapon, Hitbox hitbox) {
        game.getPlayerManager().broadcastMessage(messageHelper.create(getKillMessageKey(hitbox),
                new Placeholder("bg_killer", killer.getTeam().getChatColor() + killer.getName() + ChatColor.WHITE),
                new Placeholder("bg_player", gamePlayer.getTeam().getChatColor() + gamePlayer.getName() + ChatColor.WHITE),
                new Placeholder("bg_weapon", weapon.getName()))
        );
        handleDeath(gamePlayer);
        killer.addExp(100);
        killer.setKills(killer.getKills() + 1);
        killer.getTeam().setScore(killer.getTeam().getScore() + 1);
        game.getPlayerManager().updateExpBar(killer);

        Objective objective = getAchievedObjective();

        if (objective != null) {
            game.callEvent(new GameEndEvent(game, objective, getTopTeam(), getSortedTeams()));
            game.stop();
        }
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
            Team team = gamePlayer.getTeam();
            Spawn spawn = game.getArena().getTeamBase(team.getId());
            if (spawn == null) {
                spawn = game.getArena().getRandomSpawn(team.getId());
            }
            gamePlayer.getPlayer().teleport(spawn.getLocation());
        }
    }

    public void start() {
        for (GamePlayer gamePlayer : game.getPlayerManager().getPlayers()) {
            gamePlayer.setLives(lives);
            EnumTitle.TDM_START.send(gamePlayer.getPlayer());
        }
    }

    public void stop() {
        Objective objective = getAchievedObjective();

        for (Team team : teams) {
            Result result = Result.getResult(team, getSortedTeams());
            if (result != null) {
                for (GamePlayer gamePlayer : team.getPlayers()) {
                    objective.getTitle().send(gamePlayer.getPlayer(), new Placeholder("bg_result", messageHelper.create(result.getTranslationKey())));
                }
            }
        }

        teams.clear();
        teams = getConfigTeams();
    }
}
