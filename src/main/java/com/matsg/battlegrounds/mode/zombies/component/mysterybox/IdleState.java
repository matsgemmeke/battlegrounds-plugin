package com.matsg.battlegrounds.mode.zombies.component.mysterybox;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.mode.zombies.component.MysteryBox;
import com.matsg.battlegrounds.mode.zombies.component.MysteryBoxState;

public class IdleState implements MysteryBoxState {

    private Game game;
    private int maxRotations;
    private MysteryBox mysteryBox;

    public IdleState(Game game, MysteryBox mysteryBox, int maxRotations) {
        this.game = game;
        this.mysteryBox = mysteryBox;
        this.maxRotations = maxRotations;
    }

    public void handleInteraction(GamePlayer gamePlayer) {
        mysteryBox.setRolls(mysteryBox.getRolls() + 1);
        mysteryBox.setState(new RotatingState(game, mysteryBox, gamePlayer, maxRotations));
    }
}
