package com.matsg.battlegrounds.item.modifier;

import com.matsg.battlegrounds.api.util.AttributeModifier;
import com.matsg.battlegrounds.api.util.ValueObject;
import com.matsg.battlegrounds.util.Operator;
import com.matsg.battlegrounds.util.valueobject.IntegerValueObject;

public class IntegerAttributeModifier implements AttributeModifier<Integer> {

    private Integer value;
    private Operator operator;
    private String regex;

    public IntegerAttributeModifier(Integer value, Operator operator) {
        this.value = value;
        this.operator = operator;
    }

    public IntegerAttributeModifier(Integer value) {
        this(value, Operator.EQUALIZATION);
    }

    public IntegerAttributeModifier(String regex) {
        this.regex = regex;
    }

    public ValueObject<Integer> modify(ValueObject<Integer> valueObject, String[] args) {
        if (value != null && operator != null) {
            return new IntegerValueObject((int) operator.apply(valueObject.getValue(), value));
        }

        Integer value;
        Operator operator = Operator.fromText(regex.substring(0, 1));

        if (regex.contains("arg")) {
            int index = Integer.parseInt(regex.substring(4, regex.length())) - 1;
            value = Integer.parseInt(args[index]);
        } else {
            value = Integer.parseInt(regex.substring(1, regex.length()));
        }

        return new IntegerValueObject((int) operator.apply(valueObject.getValue(), value));
    }
}
