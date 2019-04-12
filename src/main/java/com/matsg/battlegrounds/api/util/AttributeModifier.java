package com.matsg.battlegrounds.api.util;

public interface AttributeModifier<T> {

    ValueObject<T> modify(ValueObject<T> valueObject, String[] args);
}
