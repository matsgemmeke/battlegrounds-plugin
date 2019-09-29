package com.matsg.battlegrounds.mode.shared;

import com.matsg.battlegrounds.api.game.Countdown;

public class NulledCountdown extends Countdown {

    public void run() {
        cancel();
    }
}
