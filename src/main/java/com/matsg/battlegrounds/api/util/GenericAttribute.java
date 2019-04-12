package com.matsg.battlegrounds.api.util;

public interface GenericAttribute<T> extends Cloneable {

    String getId();

    T getValue();

    GenericAttribute applyModifier(AttributeModifier<T> modifier, String... args);

    GenericAttribute clone();
}
