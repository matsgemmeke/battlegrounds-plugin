package com.matsg.battlegrounds.item.modifier;

import com.matsg.battlegrounds.api.util.ValueObject;
import com.matsg.battlegrounds.item.factory.FireModeFactory;
import com.matsg.battlegrounds.item.mechanism.FireMode;
import com.matsg.battlegrounds.item.mechanism.SemiAutomatic;
import com.matsg.battlegrounds.util.data.FireModeValueObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FireModeAttributeModifierTest {

    private FireModeFactory fireModeFactory;

    @Before
    public void setUp() {
        this.fireModeFactory = new FireModeFactory(null);
    }

    @Test
    public void modifyValidRegexWithoutArgs() {
        String regex = "=SEMI_AUTOMATIC";
        ValueObject<FireMode> valueObject = new FireModeValueObject(null);

        FireModeAttributeModifier modifier = new FireModeAttributeModifier(regex, fireModeFactory);
        ValueObject<FireMode> attribute = modifier.modify(valueObject, new String[0]);

        assertTrue(attribute instanceof FireModeValueObject);
        assertTrue(attribute.getValue() instanceof SemiAutomatic);
    }

    @Test
    public void modifyValidRegexWithArgs() {
        String regex = "=arg1";
        String[] args = new String[] { "SEMI_AUTOMATIC(1,0)" };
        ValueObject<FireMode> valueObject = new FireModeValueObject(null);

        FireModeAttributeModifier modifier = new FireModeAttributeModifier(regex, fireModeFactory);
        ValueObject<FireMode> attribute = modifier.modify(valueObject, args);

        assertTrue(attribute instanceof FireModeValueObject);
        assertTrue(attribute.getValue() instanceof SemiAutomatic);
    }

    @Test(expected = AttributeModificationException.class)
    public void modifyInvalidRegexWithoutArgs() {
        String regex = "=INVALID";
        ValueObject<FireMode> valueObject = new FireModeValueObject(null);

        FireModeAttributeModifier modifier = new FireModeAttributeModifier(regex, fireModeFactory);
        modifier.modify(valueObject, new String[0]);
    }

    @Test(expected = AttributeModificationException.class)
    public void modifyInvalidFireModeType() {
        String regex = "=arg1";
        String[] args = new String[] { "INVALID(1,0)" };
        ValueObject<FireMode> valueObject = new FireModeValueObject(null);

        FireModeAttributeModifier modifier = new FireModeAttributeModifier(regex, fireModeFactory);
        modifier.modify(valueObject, args);
    }

    @Test(expected = AttributeModificationException.class)
    public void modifyInvalidRateOfFire() {
        String regex = "=arg1";
        String[] args = new String[] { "SEMI_AUTOMATIC(INVALID,0)" };
        ValueObject<FireMode> valueObject = new FireModeValueObject(null);

        FireModeAttributeModifier modifier = new FireModeAttributeModifier(regex, fireModeFactory);
        modifier.modify(valueObject, args);
    }

    @Test(expected = AttributeModificationException.class)
    public void modifyInvalidBurst() {
        String regex = "=arg1";
        String[] args = new String[] { "SEMI_AUTOMATIC(1,INVALID)" };
        ValueObject<FireMode> valueObject = new FireModeValueObject(null);

        FireModeAttributeModifier modifier = new FireModeAttributeModifier(regex, fireModeFactory);
        modifier.modify(valueObject, args);
    }
}
