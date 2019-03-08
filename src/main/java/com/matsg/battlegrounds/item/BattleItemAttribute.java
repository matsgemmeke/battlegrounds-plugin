package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.api.item.AttributeModifier;
import com.matsg.battlegrounds.api.item.AttributeValue;
import com.matsg.battlegrounds.api.item.ItemAttribute;

public class BattleItemAttribute<T> implements ItemAttribute<T> {

    private AttributeValue<T> attributeValue;
    private String id;

    public BattleItemAttribute(String id, AttributeValue<T> attributeValue) {
        this.id = id;
        this.attributeValue = attributeValue;
    }

    public ItemAttribute clone() {
        try {
            BattleItemAttribute attribute = (BattleItemAttribute) super.clone();
            if (attributeValue != null) {
                attribute.attributeValue = attributeValue.copy();
            }
            return attribute;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public AttributeValue<T> getAttributeValue() {
        return attributeValue;
    }

    public String getId() {
        return id;
    }

    public ItemAttribute applyModifier(AttributeModifier<T> modifier, String... args) {
        attributeValue = modifier.modify(attributeValue, args);
        return this;
    }
}
