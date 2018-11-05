package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.item.GunPart;
import com.matsg.battlegrounds.util.Message;

public enum BattleGunPart implements GunPart {

    INTERNAL(1, Message.create(TranslationKey.GUN_PART_INTERNAL)),
    LOWER_RAIL(2, Message.create(TranslationKey.GUN_PART_LOWER_RAIL)),
    MAGAZINE(3, Message.create(TranslationKey.GUN_PART_MAGAZINE)),
    MUZZLE(4, Message.create(TranslationKey.GUN_PART_MUZZLE)),
    STOCK(5, Message.create(TranslationKey.GUN_PART_STOCK)),
    UPPER_RAIL(6, Message.create(TranslationKey.GUN_PART_UPPER_RAIL));

    private int id;
    private String name;

    BattleGunPart(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}