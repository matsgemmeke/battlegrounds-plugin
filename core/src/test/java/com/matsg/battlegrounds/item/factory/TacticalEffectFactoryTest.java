package com.matsg.battlegrounds.item.factory;

import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.api.item.TacticalEffect;
import com.matsg.battlegrounds.item.mechanism.BlindnessEffect;
import com.matsg.battlegrounds.item.mechanism.NoiseEffect;
import com.matsg.battlegrounds.item.mechanism.SmokeEffect;
import com.matsg.battlegrounds.item.mechanism.TacticalEffectType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TacticalEffectFactoryTest {

    private TaskRunner taskRunner;

    @Before
    public void setUp() {
        this.taskRunner = mock(TaskRunner.class);
    }

    @Test
    public void makeBlindnessTacticalEffect() {
        TacticalEffectType tacticalEffectType = TacticalEffectType.BLINDNESS;

        TacticalEffectFactory factory = new TacticalEffectFactory(taskRunner);
        TacticalEffect tacticalEffect = factory.make(tacticalEffectType, 0);

        assertTrue(tacticalEffect instanceof BlindnessEffect);
    }

    @Test
    public void makeNoiseTacticalEffect() {
        TacticalEffectType tacticalEffectType = TacticalEffectType.NOISE;

        TacticalEffectFactory factory = new TacticalEffectFactory(taskRunner);
        TacticalEffect tacticalEffect = factory.make(tacticalEffectType, 0);

        assertTrue(tacticalEffect instanceof NoiseEffect);
    }

    @Test
    public void makeSmokeTacticalEffect() {
        TacticalEffectType tacticalEffectType = TacticalEffectType.SMOKE;

        TacticalEffectFactory factory = new TacticalEffectFactory(taskRunner);
        TacticalEffect tacticalEffect = factory.make(tacticalEffectType, 0);

        assertTrue(tacticalEffect instanceof SmokeEffect);
    }
}
