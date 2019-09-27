package com.matsg.battlegrounds.item.factory;

import com.matsg.battlegrounds.item.mechanism.MagazineReload;
import com.matsg.battlegrounds.item.mechanism.PerRoundReload;
import com.matsg.battlegrounds.item.mechanism.ReloadSystem;
import com.matsg.battlegrounds.item.mechanism.ReloadSystemType;
import org.junit.Test;

import static org.junit.Assert.*;

public class ReloadSystemFactoryTest {

    @Test
    public void makeMagazineReloadSystem() {
        ReloadSystemType reloadSystemType = ReloadSystemType.MAGAZINE;

        ReloadSystemFactory factory = new ReloadSystemFactory();
        ReloadSystem reloadSystem = factory.make(reloadSystemType);

        assertTrue(reloadSystem instanceof MagazineReload);
    }

    @Test
    public void makePerRoundReloadSystem() {
        ReloadSystemType reloadSystemType = ReloadSystemType.PER_ROUND;

        ReloadSystemFactory factory = new ReloadSystemFactory();
        ReloadSystem reloadSystem = factory.make(reloadSystemType);

        assertTrue(reloadSystem instanceof PerRoundReload);
    }
}
