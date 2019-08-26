package com.matsg.battlegrounds.mode.zombies.component.mysterybox;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.mode.zombies.component.MysteryBox;
import com.matsg.battlegrounds.mode.zombies.component.MysteryBoxState;

public class InactiveState implements MysteryBoxState {

    private boolean inUse;
    private MysteryBox mysteryBox;

    public InactiveState(MysteryBox mysteryBox) {
        this.mysteryBox = mysteryBox;
        this.inUse = false;
    }

    public boolean isInUse() {
        return inUse;
    }

    public boolean handleInteraction(GamePlayer gamePlayer) {
        return false;
    }

    public boolean handleLookInteraction(GamePlayer gamePlayer) {
        return false;
    }

    public void initState() {
        mysteryBox.setActive(false);
        mysteryBox.setCurrentWeapon(null);
        mysteryBox.setRolls(0);
    }

    public void remove() { }
}
