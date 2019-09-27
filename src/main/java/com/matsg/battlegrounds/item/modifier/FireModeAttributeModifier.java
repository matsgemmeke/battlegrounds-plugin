package com.matsg.battlegrounds.item.modifier;

import com.matsg.battlegrounds.api.util.AttributeModifier;
import com.matsg.battlegrounds.api.util.ValueObject;
import com.matsg.battlegrounds.item.mechanism.FireMode;
import com.matsg.battlegrounds.item.mechanism.FireModeType;
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

        int rateOfFire = 0;
        int burst = 0;

        if (value.startsWith("arg")) {
            int index = Integer.parseInt(value.substring(3)) - 1;

            String[] valueArgs = args[index].substring(args[index].indexOf('(') + 1, args[index].indexOf(')')).split(",");

            try {
                rateOfFire = Integer.parseInt(valueArgs[0]);
                burst = Integer.parseInt(valueArgs[1]);
            } catch (NumberFormatException e) {
                throw new AttributeModificationException("Unable to modify firemode attribute with regex " + regex, e);
            }

            value = args[index].substring(0, args[index].indexOf('('));
        }

        FireMode fireMode;
        FireModeType fireModeType;

        try {
            fireModeType = FireModeType.valueOf(value);
        } catch (Exception e) {
            throw new AttributeModificationException("Unable to modify firemode attribute with regex " + regex, e);
        }

        fireMode = fireModeFactory.make(fireModeType, rateOfFire, burst);

        return new FireModeValueObject(fireMode);
    }
}
