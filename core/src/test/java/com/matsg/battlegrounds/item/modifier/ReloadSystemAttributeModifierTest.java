package com.matsg.battlegrounds.item.modifier;

import com.matsg.battlegrounds.api.util.ValueObject;
import com.matsg.battlegrounds.item.factory.ReloadSystemFactory;
import com.matsg.battlegrounds.item.mechanism.MagazineReload;
import com.matsg.battlegrounds.item.mechanism.ReloadSystem;
import com.matsg.battlegrounds.util.data.ReloadSystemValueObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ReloadSystemAttributeModifierTest {

    private ReloadSystemFactory reloadSystemFactory;

    @Before
    public void setUp() {
        this.reloadSystemFactory = new ReloadSystemFactory();
    }

    @Test
    public void modifyValidRegexWithoutArgs() {
        String regex = "=MAGAZINE";
        ValueObject<ReloadSystem> valueObject = new ReloadSystemValueObject(null);

        ReloadSystemAttributeModifier modifier = new ReloadSystemAttributeModifier(regex, reloadSystemFactory);
        ValueObject<ReloadSystem> attribute = modifier.modify(valueObject, new String[0]);

        assertTrue(attribute instanceof ReloadSystemValueObject);
        assertTrue(attribute.getValue() instanceof MagazineReload);
    }

    @Test
    public void modifyValidRegexWithArgs() {
        String regex = "=arg1";
        String[] args = new String[] { "MAGAZINE" };
        ValueObject<ReloadSystem> valueObject = new ReloadSystemValueObject(null);

        ReloadSystemAttributeModifier modifier = new ReloadSystemAttributeModifier(regex, reloadSystemFactory);
        ValueObject<ReloadSystem> attribute = modifier.modify(valueObject, args);

        assertTrue(attribute instanceof ReloadSystemValueObject);
        assertTrue(attribute.getValue() instanceof MagazineReload);
    }

    @Test(expected = AttributeModificationException.class)
    public void modifyInvalidRegexWithoutArgs() {
        String regex = "=INVALID";
        ValueObject<ReloadSystem> valueObject = new ReloadSystemValueObject(null);

        ReloadSystemAttributeModifier modifier = new ReloadSystemAttributeModifier(regex, reloadSystemFactory);
        modifier.modify(valueObject, new String[0]);
    }

    @Test(expected = AttributeModificationException.class)
    public void modifyInvalidFireModeType() {
        String regex = "=arg1";
        String[] args = new String[] { "INVALID(1,0)" };
        ValueObject<ReloadSystem> valueObject = new ReloadSystemValueObject(null);

        ReloadSystemAttributeModifier modifier = new ReloadSystemAttributeModifier(regex, reloadSystemFactory);
        modifier.modify(valueObject, args);
    }
}
