package com.matsg.battlegrounds.util.data;

import com.matsg.battlegrounds.api.util.ValueObject;
import com.matsg.battlegrounds.item.ReloadSystem;

public class ReloadSystemValueObject implements ValueObject<ReloadSystem> {

    private final ReloadSystem value;

    public ReloadSystemValueObject(ReloadSystem value) {
        this.value = value;
    }

    public ReloadSystem getValue() {
        return value;
    }

    public ValueObject<ReloadSystem> copy() {
        return new ReloadSystemValueObject(value);
    }
}
