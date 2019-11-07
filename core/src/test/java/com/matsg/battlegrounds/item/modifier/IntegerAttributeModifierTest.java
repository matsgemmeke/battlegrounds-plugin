package com.matsg.battlegrounds.item.modifier;

import com.matsg.battlegrounds.api.util.ValueObject;
import com.matsg.battlegrounds.util.Operator;
import com.matsg.battlegrounds.util.data.IntegerValueObject;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IntegerAttributeModifierTest {

    @Test
    public void modifyGivenValueWithDefaultOperator() {
        Integer value = 10;
        ValueObject<Integer> valueObject = new IntegerValueObject(0);

        IntegerAttributeModifier modifier = new IntegerAttributeModifier(value);
        ValueObject<Integer> attribute = modifier.modify(valueObject, null);

        assertEquals(10, attribute.getValue().intValue());
    }

    @Test
    public void modifyGivenValueWithGivenOperator() {
        Integer value = 10;
        Operator operator = Operator.ADDITION;
        ValueObject<Integer> valueObject = new IntegerValueObject(5);

        IntegerAttributeModifier modifier = new IntegerAttributeModifier(value, operator);
        ValueObject<Integer> attribute = modifier.modify(valueObject, new String[0]);

        assertEquals(15, attribute.getValue().intValue());
    }

    @Test
    public void modifyValidRegexWithoutArgs() {
        String regex = "=10";
        ValueObject<Integer> valueObject = new IntegerValueObject(0);

        IntegerAttributeModifier modifier = new IntegerAttributeModifier(regex);
        ValueObject<Integer> attribute = modifier.modify(valueObject, new String[0]);

        assertEquals(10, attribute.getValue().intValue());
    }

    @Test
    public void modifyValidRegexWithArgs() {
        String regex = "=arg1";
        String[] args = new String[] { "10" };
        ValueObject<Integer> valueObject = new IntegerValueObject(0);

        IntegerAttributeModifier modifier = new IntegerAttributeModifier(regex);
        ValueObject<Integer> attribute = modifier.modify(valueObject, args);

        assertEquals(10, attribute.getValue().intValue());
    }

    @Test(expected = AttributeModificationException.class)
    public void modifyInvalidRegexWithoutArgs() {
        String regex = "=INVALID";
        ValueObject<Integer> valueObject = new IntegerValueObject(0);

        IntegerAttributeModifier modifier = new IntegerAttributeModifier(regex);
        modifier.modify(valueObject, new String[0]);
    }

    @Test(expected = AttributeModificationException.class)
    public void modifyInvalidRegexWithArgs() {
        String regex = "=arg1";
        String[] args = new String[] { "INVALID" };
        ValueObject<Integer> valueObject = new IntegerValueObject(0);

        IntegerAttributeModifier modifier = new IntegerAttributeModifier(regex);
        modifier.modify(valueObject, args);
    }
}
