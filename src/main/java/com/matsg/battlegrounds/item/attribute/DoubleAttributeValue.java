package com.matsg.battlegrounds.item.attribute;

import com.matsg.battlegrounds.api.item.AttributeValue;

public class DoubleAttributeValue implements AttributeValue<Double> {

    private Double value;

    public DoubleAttributeValue(Double value) {
        this.value = value;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public AttributeValue<Double> copy() {
        return new DoubleAttributeValue(value);
    }

    public Double parseString(String arg) {
        return Double.parseDouble(arg);
    }
}
