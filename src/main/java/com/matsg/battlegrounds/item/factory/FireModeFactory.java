package com.matsg.battlegrounds.item.factory;

import com.matsg.battlegrounds.FactoryCreationException;
import com.matsg.battlegrounds.item.FireMode;
import com.matsg.battlegrounds.item.FireModeType;
import com.matsg.battlegrounds.item.firemode.*;

public class FireModeFactory {

    public FireMode make(FireModeType fireModeType) {
        switch (fireModeType) {
            case BURST:
                return new BurstMode();
            case FULLY_AUTOMATIC:
                return new FullyAutomatic();
            case SEMI_AUTOMATIC:
                return new SemiAutomatic();
            default:
                throw new FactoryCreationException("Invalid firemode type \"" + fireModeType + "\"");
        }
    }
}
