package com.matsg.battlegrounds.item.modifier;

import com.matsg.battlegrounds.api.util.AttributeModifier;
import com.matsg.battlegrounds.api.util.ValueObject;
import com.matsg.battlegrounds.item.FireMode;
import com.matsg.battlegrounds.item.FireModeType;
import com.matsg.battlegrounds.item.factory.FireModeFactory;
import com.matsg.battlegrounds.util.data.FireModeValueObject;

public class FireModeAttributeModifier implements AttributeModifier<FireMode> {

    private FireModeFactory fireModeFactory;
    private String regex;

    public FireModeAttributeModifier(String regex, FireModeFactory fireModeFactory) {
        this.regex = regex;
        this.fireModeFactory = fireModeFactory;
    }

    public ValueObject<FireMode> modify(ValueObject<FireMode> valueObject, String[] args) {
        String value = regex.substring(1);

        if (value.startsWith("arg")) {
            int index = Integer.parseInt(value.substring(3)) - 1;
            value = args[index];
        }

        FireMode fireMode;
        FireModeType fireModeType;

        try {
            fireModeType = FireModeType.valueOf(value);
        } catch (Exception e) {
            throw new AttributeModificationException("Unable to modify firemode attribute with regex " + regex, e);
        }

        fireMode = fireModeFactory.make(fireModeType);

        return new FireModeValueObject(fireMode);
    }
}
