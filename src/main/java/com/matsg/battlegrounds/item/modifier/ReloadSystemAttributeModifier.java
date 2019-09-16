package com.matsg.battlegrounds.item.modifier;

import com.matsg.battlegrounds.item.ReloadSystem;
import com.matsg.battlegrounds.api.util.AttributeModifier;
import com.matsg.battlegrounds.api.util.ValueObject;
import com.matsg.battlegrounds.item.ReloadSystemType;
import com.matsg.battlegrounds.item.factory.ReloadSystemFactory;
import com.matsg.battlegrounds.util.data.ReloadSystemValueObject;

public class ReloadSystemAttributeModifier implements AttributeModifier<ReloadSystem> {

    private ReloadSystemFactory reloadSystemFactory;
    private String regex;

    public ReloadSystemAttributeModifier(String regex, ReloadSystemFactory reloadSystemFactory) {
        this.regex = regex;
        this.reloadSystemFactory = reloadSystemFactory;
    }

    public ValueObject<ReloadSystem> modify(ValueObject<ReloadSystem> valueObject, String[] args) {
        String value = regex.substring(1, regex.length());

        if (value.startsWith("arg")) {
            int index = Integer.parseInt(value.substring(3, value.length())) - 1;
            value = args[index];
        }

        ReloadSystem reloadSystem;
        ReloadSystemType reloadSystemType;

        try {
            reloadSystemType = ReloadSystemType.valueOf(value);
        } catch (Exception e) {
            throw new AttributeModificationException("Unable to modify reloadsystem attribute with regex " + regex, e);
        }

        reloadSystem = reloadSystemFactory.make(reloadSystemType);

        return new ReloadSystemValueObject(reloadSystem);
    }
}
