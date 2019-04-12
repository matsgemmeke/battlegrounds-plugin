package com.matsg.battlegrounds.api.util;

public interface GenericAttribute<T> extends Cloneable {

    GenericAttribute applyModifier(AttributeModifier<T> modifier, String... args);

    GenericAttribute clone();

    String getId();

    T getValue();
}
