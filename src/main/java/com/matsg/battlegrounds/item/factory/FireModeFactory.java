package com.matsg.battlegrounds.item.factory;

import com.matsg.battlegrounds.FactoryCreationException;
import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.item.mechanism.FireMode;
import com.matsg.battlegrounds.item.mechanism.FireModeType;
import com.matsg.battlegrounds.item.mechanism.BurstMode;
import com.matsg.battlegrounds.item.mechanism.FullyAutomatic;
import com.matsg.battlegrounds.item.mechanism.SemiAutomatic;

public class FireModeFactory {

    private TaskRunner taskRunner;

    public FireModeFactory(TaskRunner taskRunner) {
        this.taskRunner = taskRunner;
    }

    public FireMode make(FireModeType fireModeType, int rateOfFire, int burst) {
        switch (fireModeType) {
            case BURST_MODE:
                return new BurstMode(rateOfFire, burst, taskRunner);
            case FULLY_AUTOMATIC:
                return new FullyAutomatic(rateOfFire, taskRunner);
            case SEMI_AUTOMATIC:
                return new SemiAutomatic();
            default:
                throw new FactoryCreationException("Invalid firemode type \"" + fireModeType + "\"");
        }
    }
}
