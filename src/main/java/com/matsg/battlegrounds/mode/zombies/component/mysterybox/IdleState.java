package com.matsg.battlegrounds.mode.zombies.component.mysterybox;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.mode.zombies.component.MysteryBox;
import com.matsg.battlegrounds.mode.zombies.component.MysteryBoxState;

public class IdleState implements MysteryBoxState {

    private Game game;
    private MysteryBox mysteryBox;

    public IdleState(Game game, MysteryBox mysteryBox) {
        this.game = game;
        this.mysteryBox = mysteryBox;
    }

    public boolean handleInteraction(GamePlayer gamePlayer) {
        mysteryBox.setRolls(mysteryBox.getRolls() + 1);
        mysteryBox.setState(new RotatingState(game, mysteryBox, gamePlayer));
        return true;
    }

    public void initState() {
        mysteryBox.setRolls(0);
    }
}
