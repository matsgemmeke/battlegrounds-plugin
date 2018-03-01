package com.matsg.battlegrounds.api.item;

public interface WeaponAttribute {

    WeaponAttribute applyModifier(AttributeModifier modifier);

    String getIdentifier();

    Object getValue();

    void setValue(Object value);
}