package com.matsg.battlegrounds.api.item;

public interface Attachment extends Item {

    Attachment clone();

    String getDescription();

    GunPart getGunPart();

    AttributeModifier getModifier(String attribute);

    boolean isToggleable();
}