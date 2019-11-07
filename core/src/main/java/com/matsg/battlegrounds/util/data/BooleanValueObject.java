package com.matsg.battlegrounds.util.data;

import com.matsg.battlegrounds.api.util.ValueObject;

public class BooleanValueObject implements ValueObject<Boolean> {

    private final Boolean value;

    public BooleanValueObject(Boolean value) {
        this.value = value;
    }

    public Boolean getValue() {
        return value;
    }

    public ValueObject<Boolean> copy() {
        return new BooleanValueObject(value);
    }
}
