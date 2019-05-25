package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.item.GunPart;

public enum BattleGunPart implements GunPart {

    INTERNAL(1, TranslationKey.GUN_PART_INTERNAL),
    LOWER_RAIL(2, TranslationKey.GUN_PART_LOWER_RAIL),
    MAGAZINE(3, TranslationKey.GUN_PART_MAGAZINE),
    MUZZLE(4, TranslationKey.GUN_PART_MUZZLE),
    STOCK(5, TranslationKey.GUN_PART_STOCK),
    UPPER_RAIL(6, TranslationKey.GUN_PART_UPPER_RAIL);

    private int id;
    private String name;

    BattleGunPart(int id, TranslationKey key) {
        this.id = id;
        this.name = BattlegroundsPlugin.getPlugin().getTranslator().translate(key);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
