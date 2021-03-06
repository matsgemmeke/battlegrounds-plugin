package com.matsg.battlegrounds.item.factory;

import com.matsg.battlegrounds.FactoryCreationException;
import com.matsg.battlegrounds.item.mechanism.ReloadSystem;
import com.matsg.battlegrounds.item.mechanism.ReloadSystemType;
import com.matsg.battlegrounds.item.mechanism.MagazineReload;
import com.matsg.battlegrounds.item.mechanism.PerRoundReload;

public class ReloadSystemFactory {

    public ReloadSystem make(ReloadSystemType reloadSystemType) {
        switch (reloadSystemType) {
            case MAGAZINE:
                return new MagazineReload();
            case PER_ROUND:
                return new PerRoundReload();
            default:
                throw new FactoryCreationException("Invalid reload system type \"" + reloadSystemType + "\"");
        }
    }
}
