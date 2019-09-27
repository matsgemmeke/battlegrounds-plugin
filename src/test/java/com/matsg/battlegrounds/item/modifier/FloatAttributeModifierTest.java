package com.matsg.battlegrounds.item.modifier;

import com.matsg.battlegrounds.api.util.ValueObject;
import com.matsg.battlegrounds.util.Operator;
import com.matsg.battlegrounds.util.data.FloatValueObject;
import org.junit.Test;

import static org.junit.Assert.*;

public class FloatAttributeModifierTest {

    @Test
    public void modifyGivenValueWithDefaultOperator() {
        Float value = 10.0f;
        ValueObject<Float> valueObject = new FloatValueObject(0.0f);

        FloatAttributeModifier modifier = new FloatAttributeModifier(value);
        ValueObject<Float> attribute = modifier.modify(valueObject, null);

        assertEquals(10.0f, attribute.getValue(), 0.0f);
    }

    @Test
    public void modifyGivenValueWithGivenOperator() {
        Float value = 10.0f;
        Operator operator = Operator.ADDITION;
        ValueObject<Float> valueObject = new FloatValueObject(5.0f);

        FloatAttributeModifier modifier = new FloatAttributeModifier(value, operator);
        ValueObject<Float> attribute = modifier.modify(valueObject, new String[0]);

        assertEquals(15.0f, attribute.getValue(), 0.0f);
    }

    @Test
    public void modifyValidRegexWithoutArgs() {
        String regex = "=10.0";
        ValueObject<Float> valueObject = new FloatValueObject(0.0f);

        FloatAttributeModifier modifier = new FloatAttributeModifier(regex);
        ValueObject<Float> attribute = modifier.modify(valueObject, new String[0]);

        assertEquals(10.0f, attribute.getValue(), 0.0f);
    }

    @Test
    public void modifyValidRegexWithArgs() {
        String regex = "=arg1";
        String[] args = new String[] { "10.0" };
        ValueObject<Float> valueObject = new FloatValueObject(0.0f);

        FloatAttributeModifier modifier = new FloatAttributeModifier(regex);
        ValueObject<Float> attribute = modifier.modify(valueObject, args);

        assertEquals(10.0f, attribute.getValue(), 0.0f);
    }

    @Test(expected = AttributeModificationException.class)
    public void modifyInvalidRegexWithoutArgs() {
        String regex = "=INVALID";
        ValueObject<Float> valueObject = new FloatValueObject(0.0f);

        FloatAttributeModifier modifier = new FloatAttributeModifier(regex);
        modifier.modify(valueObject, new String[0]);
    }

    @Test(expected = AttributeModificationException.class)
    public void modifyInvalidRegexWithArgs() {
        String regex = "=arg1";
        String[] args = new String[] { "INVALID" };
        ValueObject<Float> valueObject = new FloatValueObject(0.0f);

        FloatAttributeModifier modifier = new FloatAttributeModifier(regex);
        modifier.modify(valueObject, args);
    }
}
