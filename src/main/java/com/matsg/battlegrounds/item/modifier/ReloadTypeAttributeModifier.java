package com.matsg.battlegrounds.item.modifier;

import com.matsg.battlegrounds.api.item.ReloadType;
import com.matsg.battlegrounds.api.util.AttributeModifier;
import com.matsg.battlegrounds.api.util.ValueObject;
import com.matsg.battlegrounds.util.valueobject.ReloadTypeValueObject;

public class ReloadTypeAttributeModifier implements AttributeModifier<ReloadType> {

    private String regex;

    public ReloadTypeAttributeModifier(String regex) {
        this.regex = regex;
    }

    public ValueObject<ReloadType> modify(ValueObject<ReloadType> valueObject, String[] args) {
        String value = regex.substring(1, regex.length());

        if (value.startsWith("arg")) {
            int index = Integer.parseInt(value.substring(3, value.length())) - 1;
            value = args[index];
        }

        ReloadType reloadType;

        try {
            reloadType = ReloadType.valueOf(value);
        } catch (Exception e) {
            throw new AttributeModificationException("Unable to modify reloadtype attribute with regex " + regex, e);
        }

        return new ReloadTypeValueObject(reloadType);
    }
}
