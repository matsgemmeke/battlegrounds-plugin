package com.matsg.battlegrounds.api.item;

public interface AttributeModifier<T> {

    AttributeValue<T> modify(AttributeValue<T> attributeValue, Object... args);
}