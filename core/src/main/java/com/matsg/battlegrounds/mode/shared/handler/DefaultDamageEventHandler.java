package com.matsg.battlegrounds.mode.shared.handler;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.event.EventHandler;
import com.matsg.battlegrounds.api.event.GamePlayerDamageEntityEvent;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.GameMode;

public class DefaultDamageEventHandler implements EventHandler<GamePlayerDamageEntityEvent> {

    private Game game;
    private GameMode gameMode;

    public DefaultDamageEventHandler(Game game, GameMode gameMode) {
        this.game = game;
        this.gameMode = gameMode;
    }

    public void handle(GamePlayerDamageEntityEvent event) {
        if (event.getGame() != game || !gameMode.isActive() || !(event.getEntity() instanceof GamePlayer)) {
            return;
        }

        double damage = event.getDamage();
        GamePlayer gamePlayer = (GamePlayer) event.getEntity();

        gamePlayer.damage(damage);
    }
}
