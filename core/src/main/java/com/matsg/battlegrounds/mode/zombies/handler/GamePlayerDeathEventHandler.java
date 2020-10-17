package com.matsg.battlegrounds.mode.zombies.handler;

import com.matsg.battlegrounds.InternalsProvider;
import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.entity.PlayerState;
import com.matsg.battlegrounds.api.event.EventHandler;
import com.matsg.battlegrounds.api.event.GamePlayerDeathEvent;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.entity.TimerDownState;
import com.matsg.battlegrounds.mode.zombies.PerkManager;
import com.matsg.battlegrounds.mode.zombies.Zombies;
import com.matsg.battlegrounds.mode.zombies.item.PerkEffectType;
import org.bukkit.Location;

public class GamePlayerDeathEventHandler implements EventHandler<GamePlayerDeathEvent> {

    private Game game;
    private InternalsProvider internals;
    private PerkManager perkManager;
    private TaskRunner taskRunner;
    private Translator translator;
    private Zombies zombies;

    public GamePlayerDeathEventHandler(
            Game game,
            Zombies zombies,
            PerkManager perkManager,
            InternalsProvider internals,
            TaskRunner taskRunner,
            Translator translator
    ) {
        this.game = game;
        this.zombies = zombies;
        this.perkManager = perkManager;
        this.internals = internals;
        this.taskRunner = taskRunner;
        this.translator = translator;
    }

    public void handle(GamePlayerDeathEvent event) {
        if (!zombies.isActive()) {
            return;
        }

        GamePlayer gamePlayer = event.getGamePlayer();

        if (gamePlayer.getState() != PlayerState.ACTIVE) {
            return;
        }

        gamePlayer.setState(PlayerState.DOWNED);
        gamePlayer.getState().apply(game, gamePlayer);

        int downDuration = zombies.getConfig().getDownDuration();
        int reviveDuration;

        if (perkManager.hasPerkEffect(gamePlayer, PerkEffectType.QUICK_REVIVE)) {
            reviveDuration = zombies.getConfig().getQuickReviveDuration();
        } else {
            reviveDuration = zombies.getConfig().getReviveDuration();
        }

        perkManager.removePerks(gamePlayer);

        if (game.getPlayerManager().getLivingPlayers().length <= 0) {
            game.stop();
        }

        double maxDistance = zombies.getConfig().getReviveMaxDistance();
        int points = gamePlayer.getPoints() / 10;
        Location location = gamePlayer.getLocation();

        TimerDownState downState = new TimerDownState(game, gamePlayer, location, internals, taskRunner, translator, downDuration, reviveDuration);
        downState.setMaxDistance(maxDistance);
        downState.setOnPlayerKill(this::onPlayerKill);
        downState.setPoints(points);
        downState.run();

        gamePlayer.setDownState(downState);
        gamePlayer.setPoints(gamePlayer.getPoints() - points);
    }

    private void onPlayerKill(GamePlayer gamePlayer) {
        System.out.println(gamePlayer.getName());
    }
}
