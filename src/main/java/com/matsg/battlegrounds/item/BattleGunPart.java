package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.api.item.GunPart;
import com.matsg.battlegrounds.util.EnumMessage;

public enum BattleGunPart implements GunPart {

    INTERNAL(1, EnumMessage.GUN_PART_INTERNAL.getMessage()),
    LOWER_RAIL(2, EnumMessage.GUN_PART_LOWER_RAIL.getMessage()),
    MAGAZINE(3, EnumMessage.GUN_PART_MAGAZINE.getMessage()),
    MUZZLE(4, EnumMessage.GUN_PART_MUZZLE.getMessage()),
    STOCK(5, EnumMessage.GUN_PART_STOCK.getMessage()),
    UPPER_RAIL(6, EnumMessage.GUN_PART_UPPER_RAIL.getMessage());

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