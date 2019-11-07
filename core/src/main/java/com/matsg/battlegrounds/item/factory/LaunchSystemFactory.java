package com.matsg.battlegrounds.item.factory;

import com.matsg.battlegrounds.FactoryCreationException;
import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.InternalsProvider;
import com.matsg.battlegrounds.item.mechanism.GrenadeLaunch;
import com.matsg.battlegrounds.item.mechanism.LaunchSystem;
import com.matsg.battlegrounds.item.mechanism.LaunchSystemType;
import com.matsg.battlegrounds.item.mechanism.RocketLaunch;

public class LaunchSystemFactory {

    private InternalsProvider internals;
    private TaskRunner taskRunner;

    public LaunchSystemFactory(InternalsProvider internals, TaskRunner taskRunner) {
        this.internals = internals;
        this.taskRunner = taskRunner;
    }

    LaunchSystem make(LaunchSystemType launchSystemType, double launchSpeed) {
        switch (launchSystemType) {
            case GRENADE:
                return new GrenadeLaunch(launchSpeed, taskRunner);
            case ROCKET:
                return new RocketLaunch(launchSpeed, internals, taskRunner);
            default:
                throw new FactoryCreationException("Invalid launch system type \"" + launchSystemType + "\"");
        }
    }
}
