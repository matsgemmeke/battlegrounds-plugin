package com.matsg.battlegrounds.api.item;

import com.matsg.battlegrounds.api.util.AttributeModifier;

public interface Attachment extends Item {

    Attachment clone();

    String getDescription();

    GunPart getGunPart();

    AttributeModifier getModifier(String attribute);

    boolean isToggleable();
}
