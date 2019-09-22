package com.matsg.battlegrounds.mode.ffa;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.event.EventChannel;
import com.matsg.battlegrounds.api.event.EventDispatcher;
import com.matsg.battlegrounds.api.event.GamePlayerDeathEvent;
import com.matsg.battlegrounds.api.event.GamePlayerKillEntityEvent;
import com.matsg.battlegrounds.api.game.*;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.game.BattleTeam;
import com.matsg.battlegrounds.mode.shared.ClassicGameMode;
import com.matsg.battlegrounds.mode.GameModeCountdown;
import com.matsg.battlegrounds.mode.GameModeType;
import com.matsg.battlegrounds.mode.shared.SpawningBehavior;
import com.matsg.battlegrounds.mode.shared.handler.GeneralDeathHandler;
import com.matsg.battlegrounds.mode.shared.handler.GeneralKillHandler;
import com.matsg.battlegrounds.util.EnumTitle;
import org.bukkit.ChatColor;

import java.util.List;

public class FreeForAll extends ClassicGameMode {

    private FFAConfig config;

    public FreeForAll(Battlegrounds plugin, Game game, Translator translator, SpawningBehavior spawningBehavior, FFAConfig config) {
        super(plugin, GameModeType.FREE_FOR_ALL, game, translator, spawningBehavior);
        this.config = config;
        this.name = translator.translate(TranslationKey.FFA_NAME);
        this.shortName = translator.translate(TranslationKey.FFA_SHORT);
    }

    public void onCreate() {
        EventDispatcher eventDispatcher = plugin.getEventDispatcher();

        // Register gamemode specific event handlers
        eventDispatcher.registerEventChannel(GamePlayerDeathEvent.class, new EventChannel<>(
                new GeneralDeathHandler(game, this)
        ));
        eventDispatcher.registerEventChannel(GamePlayerKillEntityEvent.class, new EventChannel<>(
                new GeneralKillHandler(eventDispatcher, game, this, translator)
        ));
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

    protected Countdown makeCountdown() {
        return new GameModeCountdown(game, translator, config.getCountdownLength());
    }

    public void removePlayer(GamePlayer gamePlayer) {
        Team team = getTeam(gamePlayer);
        if (team == null) {
            return;
        }
        teams.remove(team);
    }

    public void start() {
        super.start();
        for (GamePlayer gamePlayer : game.getPlayerManager().getPlayers()) {
            gamePlayer.setLives(config.getLives());
            EnumTitle.FFA_START.send(gamePlayer.getPlayer());
        }
    }

    public void stop() {
        super.stop();

        List<Team> teams = getSortedTeams();
        Placeholder[] placeholders = new Placeholder[] {
                new Placeholder("bg_first", teams.size() > 0 && teams.get(0) != null ? teams.get(0).getPlayers()[0].getName() : "---"),
                new Placeholder("bg_first_score", teams.size() > 0 && teams.get(0) != null ? teams.get(0).getPlayers()[0].getKills() : 0),
                new Placeholder("bg_second", teams.size() > 1 && teams.get(1) != null ? teams.get(1).getPlayers()[0].getName() : "---"),
                new Placeholder("bg_second_score", teams.size() > 1 && teams.get(1) != null ? teams.get(1).getPlayers()[0].getKills() : 0),
                new Placeholder("bg_third", teams.size() > 2 && teams.get(2) != null ? teams.get(2).getPlayers()[0].getName() : "---"),
                new Placeholder("bg_third_score", teams.size() > 2 && teams.get(2) != null ? teams.get(2).getPlayers()[0].getKills() : 0)
        };

        for (String message : config.getEndMessage()) {
            game.getPlayerManager().broadcastMessage(translator.translate(TranslationKey.PREFIX) + translator.createSimpleMessage(message, placeholders));
        }

        this.teams.clear();
    }
}
