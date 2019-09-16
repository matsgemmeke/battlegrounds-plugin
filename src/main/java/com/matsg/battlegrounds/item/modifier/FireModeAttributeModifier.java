package com.matsg.battlegrounds.item.modifier;

import com.matsg.battlegrounds.api.util.AttributeModifier;
import com.matsg.battlegrounds.api.util.ValueObject;
import com.matsg.battlegrounds.item.FireModeType;
import com.matsg.battlegrounds.util.data.FireModeValueObject;

public class FireModeAttributeModifier implements AttributeModifier<FireModeType> {

    private String regex;

    public FireModeAttributeModifier(String regex) {
        this.regex = regex;
    }

    public ValueObject<FireModeType> modify(ValueObject<FireModeType> valueObject, String[] args) {
        String value = regex.substring(1);

        if (value.startsWith("arg")) {
            int index = Integer.parseInt(value.substring(3)) - 1;
            value = args[index];
        }

        FireModeType fireMode;

        try {
            fireMode = FireModeType.valueOf(value);
        } catch (Exception e) {
            throw new AttributeModificationException("Unable to modify firemode attribute with regex " + regex, e);
        }

        return new FireModeValueObject(fireMode);
    }
}
