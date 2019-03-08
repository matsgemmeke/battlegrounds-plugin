package com.matsg.battlegrounds.api.item;

public interface ItemAttribute<T> extends Cloneable {

    ItemAttribute applyModifier(AttributeModifier<T> modifier, String... args);

    ItemAttribute clone();

    String getId();

    AttributeValue<T> getAttributeValue();
}
