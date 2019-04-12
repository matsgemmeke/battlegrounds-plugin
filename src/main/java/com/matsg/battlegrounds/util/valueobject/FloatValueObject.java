package com.matsg.battlegrounds.util.valueobject;

import com.matsg.battlegrounds.api.util.ValueObject;

public class FloatValueObject implements ValueObject<Float> {

    private Float value;

    public FloatValueObject(Float value) {
        this.value = value;
    }

    public Float getValue() {
        return value;
    }

    public ValueObject<Float> copy() {
        return new FloatValueObject(value);
    }
}
