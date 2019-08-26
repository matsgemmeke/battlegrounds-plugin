package com.matsg.battlegrounds.mode.zombies.component.mysterybox;

import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.mode.zombies.component.MysteryBox;
import com.matsg.battlegrounds.mode.zombies.component.MysteryBoxState;
import com.matsg.battlegrounds.util.ActionBar;

public class IdleState implements MysteryBoxState {

    private boolean inUse;
    private Game game;
    private MysteryBox mysteryBox;

    public IdleState(Game game, MysteryBox mysteryBox) {
        this.game = game;
        this.mysteryBox = mysteryBox;
        this.inUse = false;
    }

    public boolean isInUse() {
        return inUse;
    }

    public boolean handleInteraction(GamePlayer gamePlayer) {
        game.getPlayerManager().givePoints(gamePlayer, -mysteryBox.getPrice());

        mysteryBox.setRolls(mysteryBox.getRolls() + 1);
        mysteryBox.setState(new RollingState(game, mysteryBox, gamePlayer));
        return true;
    }

    public boolean handleLookInteraction(GamePlayer gamePlayer) {
        ActionBar.MYSTERY_BOX.send(gamePlayer.getPlayer(), new Placeholder("bg_price", mysteryBox.getPrice()));
        return true;
    }

    public void initState() {
        mysteryBox.setRolls(0);
    }

    public void remove() {
        mysteryBox.setActive(false);
    }
}
