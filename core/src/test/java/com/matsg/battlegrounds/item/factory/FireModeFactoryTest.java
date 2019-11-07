package com.matsg.battlegrounds.item.factory;

import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.item.mechanism.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FireModeFactoryTest {

    private TaskRunner taskRunner;

    @Before
    public void setUp() {
        this.taskRunner = mock(TaskRunner.class);
    }

    @Test
    public void makeBurstMode() {
        FireModeType fireModeType = FireModeType.BURST_MODE;

        FireModeFactory factory = new FireModeFactory(taskRunner);
        FireMode fireMode = factory.make(fireModeType, 0, 0);

        assertTrue(fireMode instanceof BurstMode);
    }

    @Test
    public void makeFullyAutomaticFireMode() {
        FireModeType fireModeType = FireModeType.FULLY_AUTOMATIC;
        FireModeFactory factory = new FireModeFactory(taskRunner);

        FireMode fireMode = factory.make(fireModeType, 0, 0);

        assertTrue(fireMode instanceof FullyAutomatic);
    }

    @Test
    public void makeSemiAutomaticFireMode() {
        FireModeType fireModeType = FireModeType.SEMI_AUTOMATIC;
        FireModeFactory factory = new FireModeFactory(taskRunner);

        FireMode fireMode = factory.make(fireModeType, 0, 0);

        assertTrue(fireMode instanceof SemiAutomatic);
    }
}
