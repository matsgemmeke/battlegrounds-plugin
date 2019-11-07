package com.matsg.battlegrounds.item.factory;

import com.matsg.battlegrounds.FactoryCreationException;
import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.api.item.TacticalEffect;
import com.matsg.battlegrounds.item.mechanism.BlindnessEffect;
import com.matsg.battlegrounds.item.mechanism.NoiseEffect;
import com.matsg.battlegrounds.item.mechanism.SmokeEffect;
import com.matsg.battlegrounds.item.mechanism.TacticalEffectType;

public class TacticalEffectFactory {

    private TaskRunner taskRunner;

    public TacticalEffectFactory(TaskRunner taskRunner) {
        this.taskRunner = taskRunner;
    }

    public TacticalEffect make(TacticalEffectType tacticalEffectType, int duration) {
        switch (tacticalEffectType) {
            case BLINDNESS:
                return new BlindnessEffect(duration);
            case NOISE:
                return new NoiseEffect(taskRunner);
            case SMOKE:
                return new SmokeEffect(taskRunner);
            default:
                throw new FactoryCreationException("Invalid tactical effect type \"" + tacticalEffectType + "\"");
        }
    }
}
