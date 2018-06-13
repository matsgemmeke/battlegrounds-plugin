package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.api.item.WeaponAttribute;

public class BattleWeaponAttribute implements WeaponAttribute {

    private Object value;
    private String identifier;

    public BattleWeaponAttribute(String identifier, Object value) {
        this.identifier = identifier;
        this.value = value;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public WeaponAttribute applyModifier(AttributeModifier modifier) {
        value = modifier.modify(value);
        return this;
    }
}