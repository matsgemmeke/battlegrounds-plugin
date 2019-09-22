package com.matsg.battlegrounds.item.factory;

import com.matsg.battlegrounds.FactoryCreationException;
import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.api.Version;
import com.matsg.battlegrounds.item.mechanism.GrenadeLaunch;
import com.matsg.battlegrounds.item.mechanism.LaunchSystem;
import com.matsg.battlegrounds.item.mechanism.LaunchSystemType;
import com.matsg.battlegrounds.item.mechanism.RocketLaunch;

public class LaunchSystemFactory {

    private TaskRunner taskRunner;
    private Version version;

    public LaunchSystemFactory(TaskRunner taskRunner, Version version) {
        this.taskRunner = taskRunner;
        this.version = version;
    }

    LaunchSystem make(LaunchSystemType launchSystemType, double launchSpeed) {
        switch (launchSystemType) {
            case GRENADE:
                return new GrenadeLaunch(launchSpeed, taskRunner);
            case ROCKET:
                return new RocketLaunch(launchSpeed, taskRunner, version);
            default:
                throw new FactoryCreationException("Invalid launch system type \"" + launchSystemType + "\"");
        }
    }
}
