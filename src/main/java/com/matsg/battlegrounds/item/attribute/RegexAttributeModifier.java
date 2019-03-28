package com.matsg.battlegrounds.item.attribute;

import com.matsg.battlegrounds.api.item.AttributeModifier;
import com.matsg.battlegrounds.api.item.AttributeValue;

public class RegexAttributeModifier implements AttributeModifier {

    private String regex;

    public RegexAttributeModifier(String regex) {
        this.regex = regex;
    }

    public AttributeValue modify(AttributeValue attributeValue, Object... args) {
        String regexValue = regex.substring(1, regex.length());
        AttributeOperator operator = AttributeOperator.fromText(regex.substring(0, 1));

        if (regexValue.startsWith("arg")) {
            int index = Integer.parseInt(regexValue.substring(3, regexValue.length())) - 1;
            if (args != null && args.length >= index) {
                attributeValue.setValue(operator.apply(attributeValue.getValue(), attributeValue.parseString(args[index].toString())));
            }
        } else {
            attributeValue.setValue(operator.apply(attributeValue.getValue(), attributeValue.parseString(regexValue)));
        }

        return attributeValue;
    }
}
