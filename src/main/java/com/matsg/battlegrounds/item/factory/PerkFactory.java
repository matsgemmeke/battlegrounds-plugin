package com.matsg.battlegrounds.item.factory;

import com.matsg.battlegrounds.api.item.Perk;
import com.matsg.battlegrounds.api.item.PerkEffect;
import com.matsg.battlegrounds.item.BattlePerk;
import com.matsg.battlegrounds.item.perk.*;

public class PerkFactory {

    public Perk make(PerkEffectType perkEffectType) {
        PerkEffect perkEffect;

        switch (perkEffectType) {
            case DOUBLE_TAP:
                perkEffect = new DoubleTap();
                break;
            case JUGGERNOG:
                perkEffect = new Juggernog();
                break;
            case QUICK_REVIVE:
                perkEffect = new QuickRevive();
                break;
            case SPEED_COLA:
                perkEffect = new SpeedCola();
                break;
            case STAMIN_UP:
                perkEffect = new StaminUp();
                break;
            default:
                throw new IllegalArgumentException("Invalid perk effect type");
        }

        return new BattlePerk(perkEffect);
    }
}
