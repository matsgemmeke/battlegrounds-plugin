package com.matsg.battlegrounds.mode.zombies.handler;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.entity.PlayerState;
import com.matsg.battlegrounds.api.event.EventHandler;
import com.matsg.battlegrounds.api.event.GamePlayerDeathEvent;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.mode.zombies.PerkManager;
import com.matsg.battlegrounds.mode.zombies.Zombies;
import com.matsg.battlegrounds.mode.zombies.item.PerkEffectType;

public class GamePlayerDeathEventHandler implements EventHandler<GamePlayerDeathEvent> {

    private Game game;
    private PerkManager perkManager;
    private Zombies zombies;

    public GamePlayerDeathEventHandler(Game game, Zombies zombies, PerkManager perkManager) {
        this.game = game;
        this.zombies = zombies;
        this.perkManager = perkManager;
    }

    public void handle(GamePlayerDeathEvent event) {
        if (!zombies.isActive()) {
            return;
        }

        GamePlayer gamePlayer = event.getGamePlayer();

        if (gamePlayer.getState() != PlayerState.ACTIVE) {
            return;
        }

        gamePlayer.setState(PlayerState.SPECTATING);
        gamePlayer.getState().apply(game, gamePlayer);

        if (perkManager.hasPerkEffect(gamePlayer, PerkEffectType.QUICK_REVIVE)) {

        }

        perkManager.removePerks(gamePlayer);

        if (game.getPlayerManager().getLivingPlayers().length <= 0) {
            game.stop();
        }
    }
}
