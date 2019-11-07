package com.matsg.battlegrounds.item.factory;

import com.matsg.battlegrounds.FactoryCreationException;
import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.item.mechanism.IgnitionSystem;
import com.matsg.battlegrounds.item.mechanism.IgnitionSystemType;
import com.matsg.battlegrounds.item.mechanism.FuseIgnition;
import com.matsg.battlegrounds.item.mechanism.TriggerIgnition;

public class IgnitionSystemFactory {

    private TaskRunner taskRunner;

    public IgnitionSystemFactory(TaskRunner taskRunner) {
        this.taskRunner = taskRunner;
    }

    public IgnitionSystem make(IgnitionSystemType ignitionSystemType) {
        switch (ignitionSystemType) {
            case FUSE:
                return new FuseIgnition(taskRunner);
            case TRIGGER:
                return new TriggerIgnition(taskRunner);
            default:
                throw new FactoryCreationException("Invalid ignition system type \"" + ignitionSystemType + "\"");
        }
    }
}
