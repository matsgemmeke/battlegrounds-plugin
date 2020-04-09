package com.matsg.battlegrounds.util.data;

import com.matsg.battlegrounds.api.util.ValueObject;
import com.matsg.battlegrounds.item.mechanism.ReloadSystem;

public class ReloadSystemValueObject implements ValueObject<ReloadSystem> {

    private final ReloadSystem value;

    public ReloadSystemValueObject(ReloadSystem value) {
        this.value = value;
    }

    public ReloadSystem getValue() {
        return value;
    }

    public ValueObject<ReloadSystem> copy() {
        ReloadSystem reloadSystem = value.clone();
        return new ReloadSystemValueObject(reloadSystem);
    }
}
