package com.matsg.battlegrounds.util.data;

import com.matsg.battlegrounds.api.util.ValueObject;
import com.matsg.battlegrounds.item.mechanism.FireMode;

public class FireModeValueObject implements ValueObject<FireMode> {

    private final FireMode value;

    public FireModeValueObject(FireMode value) {
        this.value = value;
    }

    public FireMode getValue() {
        return value;
    }

    public ValueObject<FireMode> copy() {
        FireMode fireMode = value.clone();
        return new FireModeValueObject(fireMode);
    }
}
