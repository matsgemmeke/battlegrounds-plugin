package com.matsg.battlegrounds.mode.tdm;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.event.EventChannel;
import com.matsg.battlegrounds.api.event.GamePlayerDeathEvent;
import com.matsg.battlegrounds.api.event.GamePlayerKillEntityEvent;
import com.matsg.battlegrounds.api.game.*;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.mode.GameModeCountdown;
import com.matsg.battlegrounds.mode.GameModeType;
import com.matsg.battlegrounds.mode.shared.ClassicGameMode;
import com.matsg.battlegrounds.mode.shared.SpawningBehavior;
import com.matsg.battlegrounds.mode.shared.handler.GeneralDeathHandler;
import com.matsg.battlegrounds.mode.shared.handler.GeneralKillHandler;
import com.matsg.battlegrounds.util.EnumTitle;

public class TeamDeathmatch extends ClassicGameMode {

    private TDMConfig config;

    public TeamDeathmatch(Battlegrounds plugin, Game game, Translator translator, SpawningBehavior spawningBehavior, TDMConfig config) {
        super(plugin, game, translator, spawningBehavior);
        this.config = config;
        this.teams.addAll(config.getTeams());
        this.name = translator.translate(TranslationKey.TDM_NAME);
        this.shortName = translator.translate(TranslationKey.TDM_SHORT);
    }

    public void onCreate() {
        // Register gamemode specific event handlers
        plugin.getEventDispatcher().registerEventChannel(GamePlayerDeathEvent.class, new EventChannel<>(
                new GeneralDeathHandler(game, this)
        ));
        plugin.getEventDispatcher().registerEventChannel(GamePlayerKillEntityEvent.class, new EventChannel<>(
                new GeneralKillHandler(plugin, game, this, translator)
        ));
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
        gamePlayer.sendMessage(translator.translate(TranslationKey.TEAM_ASSIGNMENT, new Placeholder("bg_team", team.getChatColor() + team.getName())));
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

    public void removePlayer(GamePlayer gamePlayer) {
        Team team = getTeam(gamePlayer);
        if (team == null) {
            return;
        }
        team.removePlayer(gamePlayer);
    }

    public void start() {
        super.start();
        for (GamePlayer gamePlayer : game.getPlayerManager().getPlayers()) {
            gamePlayer.setLives(config.getLives());
            EnumTitle.TDM_START.send(gamePlayer.getPlayer());
        }
    }

    public void startCountdown() {
        super.startCountdown();

        GameModeCountdown countdown = new GameModeCountdown(game, translator, config.getCountdownLength());
        countdown.runTaskTimer(0, 20);

        game.setCountdown(countdown);
    }

    public void stop() {
        super.stop();
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
