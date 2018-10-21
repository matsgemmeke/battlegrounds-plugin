package com.matsg.battlegrounds.nms;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.matsg.battlegrounds.api.Version;
import com.matsg.battlegrounds.nms.version.Version3;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ReflectionUtils.class)
public class VersionManagerTest {

    @Test
    public void testVersionManager() {
        PowerMockito.mockStatic(ReflectionUtils.class);

        when(ReflectionUtils.getVersion()).thenReturn("1_13_0");

        VersionManager versionManager = new VersionManager();
        Version version = versionManager.getVersion();

        assertNotNull(version);
        assertTrue(version instanceof Version3);
    }
}
