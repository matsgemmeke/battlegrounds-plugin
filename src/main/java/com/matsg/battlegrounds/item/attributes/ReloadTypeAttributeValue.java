package com.matsg.battlegrounds.item.attributes;

import com.matsg.battlegrounds.api.item.AttributeValue;
import com.matsg.battlegrounds.api.item.ReloadType;

public class ReloadTypeAttributeValue implements AttributeValue<ReloadType> {

    private ReloadType value;

    public ReloadTypeAttributeValue(ReloadType value) {
        this.value = value;
    }

    public ReloadType getValue() {
        return value;
    }

    public void setValue(ReloadType value) {
        this.value = value;
    }

    public AttributeValue<ReloadType> copy() {
        return new ReloadTypeAttributeValue(value);
    }

    public ReloadType parseString(String arg) {
        return ReloadType.valueOf(arg);
    }
}