package com.matsg.battlegrounds.util.valueobject;

import com.matsg.battlegrounds.api.util.ValueObject;
import com.matsg.battlegrounds.item.FireMode;

public class FireModeValueObject implements ValueObject<FireMode> {

    private final FireMode value;

    public FireModeValueObject(FireMode value) {
        this.value = value;
    }

    public FireMode getValue() {
        return value;
    }

    public ValueObject<FireMode> copy() {
        return new FireModeValueObject(value);
    }
}
