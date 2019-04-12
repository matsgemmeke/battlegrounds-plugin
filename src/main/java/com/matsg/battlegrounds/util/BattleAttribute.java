package com.matsg.battlegrounds.util;

import com.matsg.battlegrounds.api.util.GenericAttribute;
import com.matsg.battlegrounds.api.util.ValueObject;
import com.matsg.battlegrounds.api.util.AttributeModifier;

public class BattleAttribute<T> implements GenericAttribute<T> {

    private String id;
    private ValueObject<T> valueObject;

    public BattleAttribute(String id, ValueObject<T> valueObject) {
        this.id = id;
        this.valueObject = valueObject;
    }

    public GenericAttribute clone() {
        try {
            BattleAttribute attribute = (BattleAttribute) super.clone();
            attribute.valueObject = valueObject.copy();
            return attribute;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getId() {
        return id;
    }

    public T getValue() {
        return valueObject.getValue();
    }

    public GenericAttribute applyModifier(AttributeModifier<T> modifier, String... args) {
        valueObject = modifier.modify(valueObject, args);
        return this;
    }
}
