package com.matsg.battlegrounds.item.attributes;

import com.matsg.battlegrounds.api.item.AttributeValue;
import com.matsg.battlegrounds.item.FireMode;

public class FireModeAttributeValue implements AttributeValue<FireMode> {

    private FireMode value;

    public FireModeAttributeValue(FireMode value) {
        this.value = value;
    }

    public FireMode getValue() {
        return value;
    }

    public void setValue(FireMode value) {
        this.value = value;
    }

    public AttributeValue<FireMode> copy() {
        return new FireModeAttributeValue(value);
    }

    public FireMode parseString(String arg) {
        return FireMode.valueOf(arg);
    }
}