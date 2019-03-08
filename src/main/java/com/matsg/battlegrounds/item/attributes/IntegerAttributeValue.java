package com.matsg.battlegrounds.item.attributes;

import com.matsg.battlegrounds.api.item.AttributeValue;

public class IntegerAttributeValue implements AttributeValue<Integer> {

    private Integer value;

    public IntegerAttributeValue(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public AttributeValue<Integer> copy() {
        return new IntegerAttributeValue(value);
    }

    public Integer parseString(String arg) {
        return Integer.parseInt(arg);
    }
}
