package com.matsg.battlegrounds.item.factory;

import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.item.mechanism.FuseIgnition;
import com.matsg.battlegrounds.item.mechanism.IgnitionSystem;
import com.matsg.battlegrounds.item.mechanism.IgnitionSystemType;
import com.matsg.battlegrounds.item.mechanism.TriggerIgnition;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class IgnitionSystemFactoryTest {

    private TaskRunner taskRunner;

    @Before
    public void setUp() {
        this.taskRunner = mock(TaskRunner.class);
    }

    @Test
    public void makeFuseIgnitionSystem() {
        IgnitionSystemType ignitionSystemType = IgnitionSystemType.FUSE;

        IgnitionSystemFactory factory = new IgnitionSystemFactory(taskRunner);
        IgnitionSystem ignitionSystem = factory.make(ignitionSystemType);

        assertTrue(ignitionSystem instanceof FuseIgnition);
    }

    @Test
    public void makeTriggerIgnitionSystem() {
        IgnitionSystemType ignitionSystemType = IgnitionSystemType.TRIGGER;

        IgnitionSystemFactory factory = new IgnitionSystemFactory(taskRunner);
        IgnitionSystem ignitionSystem = factory.make(ignitionSystemType);

        assertTrue(ignitionSystem instanceof TriggerIgnition);
    }
}
