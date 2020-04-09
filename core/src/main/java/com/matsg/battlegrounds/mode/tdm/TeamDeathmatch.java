package com.matsg.battlegrounds.mode.tdm;

import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.event.*;
import com.matsg.battlegrounds.api.game.*;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.gui.ViewFactory;
import com.matsg.battlegrounds.mode.GameModeCountdown;
import com.matsg.battlegrounds.mode.GameModeType;
import com.matsg.battlegrounds.mode.shared.ClassicGameMode;
import com.matsg.battlegrounds.mode.shared.SpawningBehavior;
import com.matsg.battlegrounds.mode.shared.handler.DefaultDamageEventHandler;
import com.matsg.battlegrounds.mode.shared.handler.DefaultDeathEventHandler;
import com.matsg.battlegrounds.mode.shared.handler.DefaultKillEventHandler;
import com.matsg.battlegrounds.util.EnumTitle;

public class TeamDeathmatch extends ClassicGameMode {

    private TDMConfig config;

    public TeamDeathmatch(
            Battlegrounds plugin,
            Game game,
            SpawningBehavior spawningBehavior,
            TaskRunner taskRunner,
            Translator translator,
            ViewFactory viewFactory,
            TDMConfig config
    ) {
        super(plugin, GameModeType.TEAM_DEATHMATCH, game, spawningBehavior, taskRunner, translator, viewFactory);
        this.config = config;
        this.teams.addAll(config.getTeams());
        this.name = translator.translate(TranslationKey.TDM_NAME.getPath());
        this.shortName = translator.translate(TranslationKey.TDM_SHORT.getPath());
    }

    public void onCreate() {
        EventDispatcher eventDispatcher = plugin.getEventDispatcher();

        // Register gamemode specific event handlers
        eventDispatcher.registerEventChannel(GamePlayerDamageEntityEvent.class, new EventChannel<>(
                new DefaultDamageEventHandler(game, this)
        ));
        eventDispatcher.registerEventChannel(GamePlayerDeathEvent.class, new EventChannel<>(
                new DefaultDeathEventHandler(game, this)
        ));
        eventDispatcher.registerEventChannel(GamePlayerKillEntityEvent.class, new EventChannel<>(
                new DefaultKillEventHandler(eventDispatcher, game, this, translator)
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
        gamePlayer.sendMessage(translator.translate(TranslationKey.TEAM_ASSIGNMENT.getPath(), new Placeholder("bg_team", team.getChatColor() + team.getName())));
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

    public Countdown makeCountdown() {
        return new GameModeCountdown(game, translator, config.getCountdownLength());
    }

    public void removePlayer(GamePlayer gamePlayer) {
        Team team = gamePlayer.getTeam();
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
