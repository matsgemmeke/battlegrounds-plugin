package com.matsg.battlegrounds.api.item;

public interface AttributeValue<T> {

    AttributeValue<T> copy();

    T getValue();

    T parseString(String arg);

    void setValue(T t);
}