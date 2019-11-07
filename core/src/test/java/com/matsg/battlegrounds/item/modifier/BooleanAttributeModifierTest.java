package com.matsg.battlegrounds.item.modifier;

import com.matsg.battlegrounds.api.util.ValueObject;
import com.matsg.battlegrounds.util.data.BooleanValueObject;
import org.junit.Test;

import static org.junit.Assert.*;

public class BooleanAttributeModifierTest {

    @Test
    public void modifyValidRegexWithoutArgs() {
        String regex = "=true";
        ValueObject<Boolean> valueObject = new BooleanValueObject(false);

        BooleanAttributeModifier modifier = new BooleanAttributeModifier(regex);
        ValueObject<Boolean> attribute = modifier.modify(valueObject, new String[0]);

        assertTrue(attribute.getValue());
    }

    @Test
    public void modifyValidRegexWithArgs() {
        String regex = "=arg1";
        String[] args = new String[] { "true" };
        ValueObject<Boolean> valueObject = new BooleanValueObject(false);

        BooleanAttributeModifier modifier = new BooleanAttributeModifier(regex);
        ValueObject<Boolean> attribute = modifier.modify(valueObject, args);

        assertTrue(attribute.getValue());
    }

    @Test
    public void modifyInvalidRegexWithoutArgs() {
        String regex = "=INVALID";
        ValueObject<Boolean> valueObject = new BooleanValueObject(false);

        BooleanAttributeModifier modifier = new BooleanAttributeModifier(regex);
        ValueObject<Boolean> attribute = modifier.modify(valueObject, new String[0]);

        assertFalse(attribute.getValue());
    }

    @Test
    public void modifyInvalidRegexWithArgs() {
        String regex = "=arg1";
        String[] args = new String[] { "INVALID" };
        ValueObject<Boolean> valueObject = new BooleanValueObject(false);

        BooleanAttributeModifier modifier = new BooleanAttributeModifier(regex);
        ValueObject<Boolean> attribute = modifier.modify(valueObject, args);

        assertFalse(attribute.getValue());
    }
}
