package com.matsg.battlegrounds.item.modifier;

import com.matsg.battlegrounds.api.util.ValueObject;
import com.matsg.battlegrounds.api.util.AttributeModifier;
import com.matsg.battlegrounds.util.valueobject.BooleanValueObject;

public class BooleanAttributeModifier implements AttributeModifier<Boolean> {

    private String regex;

    public BooleanAttributeModifier(String regex) {
        this.regex = regex;
    }

    public ValueObject<Boolean> modify(ValueObject<Boolean> valueObject, String[] args) {
        String value = regex.substring(1, regex.length());

        if (value.startsWith("arg")) {
            int index = Integer.parseInt(value.substring(3, value.length())) - 1;
            value = args[index];
        }

        return new BooleanValueObject(Boolean.valueOf(value));
    }
}
