package com.matsg.battlegrounds.item.factory;

import com.matsg.battlegrounds.FactoryCreationException;
import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.item.mechanism.*;

public class TacticalEffectFactory {

    private TaskRunner taskRunner;

    public TacticalEffectFactory(TaskRunner taskRunner) {
        this.taskRunner = taskRunner;
    }

    public TacticalEffect make(TacticalEffectType tacticalEffectType, int duration) {
        switch (tacticalEffectType) {
            case BAIT:
                return new BaitEffect(taskRunner, duration);
            case BLINDNESS:
                return new BlindnessEffect(taskRunner, duration);
            case NOISE:
                return new NoiseEffect(taskRunner);
            case SMOKE:
                return new SmokeEffect(taskRunner);
            default:
                throw new FactoryCreationException("Invalid tactical effect type \"" + tacticalEffectType + "\"");
        }
    }
}
