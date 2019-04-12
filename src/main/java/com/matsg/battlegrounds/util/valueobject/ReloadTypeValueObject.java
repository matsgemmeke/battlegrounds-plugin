package com.matsg.battlegrounds.util.valueobject;

import com.matsg.battlegrounds.api.util.ValueObject;
import com.matsg.battlegrounds.api.item.ReloadType;

public class ReloadTypeValueObject implements ValueObject<ReloadType> {

    private final ReloadType value;

    public ReloadTypeValueObject(ReloadType value) {
        this.value = value;
    }

    public ReloadType getValue() {
        return value;
    }

    public ValueObject<ReloadType> copy() {
        return new ReloadTypeValueObject(value);
    }
}
