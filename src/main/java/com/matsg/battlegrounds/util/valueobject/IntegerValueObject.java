package com.matsg.battlegrounds.util.valueobject;

import com.matsg.battlegrounds.api.util.ValueObject;

public class IntegerValueObject implements ValueObject<Integer> {

    private Integer value;

    public IntegerValueObject(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public ValueObject<Integer> copy() {
        return new IntegerValueObject(value);
    }
}
