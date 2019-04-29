package com.matsg.battlegrounds.game.mode.ffa;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
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

import java.util.List;

public class FreeForAll extends ArenaGameMode {

    private FFAConfig config;

    public FreeForAll(Battlegrounds plugin, Game game, FFAConfig config) {
        super(plugin, game);
        this.config = config;
        
        setName(messageHelper.create(TranslationKey.FFA_NAME));
        setShortName(messageHelper.create(TranslationKey.FFA_SHORT));

        objectives.add(new EliminationObjective());
        objectives.add(new ScoreObjective(config.getKillsToWin()));
        objectives.add(new TimeObjective(config.getTimeLimit()));
    }

    public FFAConfig getConfig() {
        return config;
    }

    public GameModeType getType() {
        return GameModeType.FREE_FOR_ALL;
    }

    public void addPlayer(GamePlayer gamePlayer) {
        if (getTeam(gamePlayer) != null) {
            return;
        }
        Team team = new BattleTeam(0, gamePlayer.getName(), config.getArmorColor(), ChatColor.WHITE);
        team.addPlayer(gamePlayer);
        teams.add(team);
    }

    public Spawn getRespawnPoint(GamePlayer gamePlayer) {
        GamePlayer nearestPlayer = game.getPlayerManager().getNearestPlayer(gamePlayer.getLocation());
        return game.getArena().getRandomSpawn(nearestPlayer.getLocation(), config.getMinimumSpawnDistance());
    }

    public GameScoreboard getScoreboard() {
        GameScoreboard scoreboard = new FFAScoreboard(game, config);
        scoreboard.getWorlds().addAll(config.getScoreboardWorlds());
        scoreboard.setLayout(config.getScoreboardLayout());

        return config.isScoreboardEnabled() ? scoreboard : null;
    }

    public void onDeath(GamePlayer gamePlayer, DeathCause deathCause) {
        game.getPlayerManager().broadcastMessage(messageHelper.createSimple(deathCause.getMessagePath(),
                new Placeholder("bg_player", gamePlayer.getName())
        ));
        handleDeath(gamePlayer);
    }

    public void onKill(GamePlayer gamePlayer, GamePlayer killer, Weapon weapon, Hitbox hitbox) {
        game.getPlayerManager().broadcastMessage(messageHelper.create(getKillMessageKey(hitbox),
                new Placeholder("bg_killer", killer.getName()),
                new Placeholder("bg_player", gamePlayer.getName()),
                new Placeholder("bg_weapon", weapon.getName())
        ));

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

    public void start() {
        super.start();
        for (GamePlayer gamePlayer : game.getPlayerManager().getPlayers()) {
            gamePlayer.setLives(config.getLives());
            EnumTitle.FFA_START.send(gamePlayer.getPlayer());
        }
    }

    public void stop() {
        List<Team> teams = getSortedTeams();
        Objective objective = getAchievedObjective();
        Placeholder[] placeholders = new Placeholder[] {
                new Placeholder("bg_first", teams.size() > 0 && teams.get(0) != null ? teams.get(0).getPlayers().iterator().next().getName() : "---"),
                new Placeholder("bg_first_score", teams.size() > 0 && teams.get(0) != null ? teams.get(0).getPlayers().iterator().next().getKills() : 0),
                new Placeholder("bg_second", teams.size() > 1 && teams.get(1) != null ? teams.get(1).getPlayers().iterator().next().getName() : "---"),
                new Placeholder("bg_second_score", teams.size() > 1 && teams.get(1) != null ? teams.get(1).getPlayers().iterator().next().getKills() : 0),
                new Placeholder("bg_third", teams.size() > 2 && teams.get(2) != null ? teams.get(2).getPlayers().iterator().next().getName() : "---"),
                new Placeholder("bg_third_score", teams.size() > 2 && teams.get(2) != null ? teams.get(2).getPlayers().iterator().next().getKills() : 0)
        };

        for (String message : config.getEndMessage()) {
            game.getPlayerManager().broadcastMessage(messageHelper.create(TranslationKey.PREFIX) + messageHelper.createSimple(message, placeholders));
        }

        for (Team team : teams) {
            Result result = Result.getResult(team, getSortedTeams());
            if (result != null) {
                for (GamePlayer gamePlayer : team.getPlayers()) {
                    objective.getTitle().send(gamePlayer.getPlayer(), new Placeholder("bg_result", messageHelper.create(result.getTranslationKey())));
                }
            }
        }

        this.teams.clear();
    }
}
