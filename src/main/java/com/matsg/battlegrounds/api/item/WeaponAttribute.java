package com.matsg.battlegrounds.api.item;

public interface WeaponAttribute {

    WeaponAttribute applyModifier(AttributeModifier modifier);

    String getIdentifier();

    double getValue();

    void setValue(double value);
}