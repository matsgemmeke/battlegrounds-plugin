package com.matsg.battlegrounds.item.attribute;

import com.matsg.battlegrounds.api.item.AttributeValue;

public class BooleanAttributeValue implements AttributeValue<Boolean> {

    private Boolean value;

    public BooleanAttributeValue(Boolean value) {
        this.value = value;
    }

    public Boolean getValue() {
        return value;
    }

    public void setValue(Boolean value) {
        this.value = value;
    }

    public AttributeValue<Boolean> copy() {
        return new BooleanAttributeValue(value);
    }

    public Boolean parseString(String arg) {
        return Boolean.parseBoolean(arg);
    }
}
