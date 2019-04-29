package com.matsg.battlegrounds.game.mode.tdm;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.game.*;
import com.matsg.battlegrounds.api.event.GameEndEvent;
import com.matsg.battlegrounds.api.event.GamePlayerDeathEvent.DeathCause;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.entity.Hitbox;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.game.mode.ArenaGameMode;
import com.matsg.battlegrounds.game.mode.GameModeType;
import com.matsg.battlegrounds.game.mode.Result;
import com.matsg.battlegrounds.game.objective.EliminationObjective;
import com.matsg.battlegrounds.game.objective.ScoreObjective;
import com.matsg.battlegrounds.game.objective.TimeObjective;
import com.matsg.battlegrounds.util.EnumTitle;
import org.bukkit.ChatColor;

public class TeamDeathmatch extends ArenaGameMode {

    private TDMConfig config;

    public TeamDeathmatch(Battlegrounds plugin, Game game, TDMConfig config) {
        super(plugin, game);
        this.config = config;
        this.teams.addAll(config.getTeams());
        
        setName(messageHelper.create(TranslationKey.TDM_NAME));
        setShortName(messageHelper.create(TranslationKey.TDM_SHORT));

        objectives.add(new EliminationObjective());
        objectives.add(new ScoreObjective(config.getKillsToWin()));
        objectives.add(new TimeObjective(config.getTimeLimit()));
    }

    public TDMConfig getConfig() {
        return config;
    }

    public GameModeType getType() {
        return GameModeType.TEAM_DEATHMATCH;
    }

    public void addPlayer(GamePlayer gamePlayer) {
        Team team = getEmptiestTeam();
        team.addPlayer(gamePlayer);
        gamePlayer.sendMessage(messageHelper.create(TranslationKey.TEAM_ASSIGNMENT, new Placeholder("bg_team", team.getChatColor() + team.getName())));
    }

    public Spawn getRespawnPoint(GamePlayer gamePlayer) {
        return game.getArena().getRandomSpawn(gamePlayer.getTeam().getId());
    }

    public GameScoreboard getScoreboard() {
        GameScoreboard scoreboard = new TDMScoreboard(game, config);
        scoreboard.getWorlds().addAll(config.getScoreboardWorlds());
        scoreboard.setLayout(config.getScoreboardLayout());

        return config.isScoreboardEnabled() ? scoreboard : null;
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
        super.start();
        for (GamePlayer gamePlayer : game.getPlayerManager().getPlayers()) {
            gamePlayer.setLives(config.getLives());
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
        teams = config.getTeams();
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
}
