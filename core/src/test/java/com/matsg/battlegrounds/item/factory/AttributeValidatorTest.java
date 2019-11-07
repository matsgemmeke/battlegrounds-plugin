package com.matsg.battlegrounds.item.factory;

import org.junit.Test;

import static org.junit.Assert.*;

public class AttributeValidatorTest {

    @Test
    public void constructAttributeValidator() {
        new AttributeValidator();
    }

    @Test(expected = ValidationFailedException.class)
    public void shouldBeBetweenValueTooLow() throws ValidationFailedException {
        AttributeValidator.shouldBeBetween(0, 10, 20);
    }

    @Test(expected = ValidationFailedException.class)
    public void shouldBeBetweenValueTooHigh() throws ValidationFailedException {
        AttributeValidator.shouldBeBetween(30, 10, 20);
    }

    @Test
    public void shouldBeBetweenWithValidValue() throws ValidationFailedException {
        int expected = 15, value = AttributeValidator.shouldBeBetween(expected, 10, 20);

        assertEquals(expected, value);
    }

    @Test(expected = ValidationFailedException.class)
    public void shouldBeHigherThanValueTooLow() throws ValidationFailedException {
        AttributeValidator.shouldBeHigherThan(0, 10);
    }

    @Test
    public void shouldBeHigherThanWithValidValue() throws ValidationFailedException {
        int expected = 20, value = AttributeValidator.shouldBeHigherThan(expected, 10);

        assertEquals(expected, value);
    }

    @Test(expected = ValidationFailedException.class)
    public void shouldBeHigherThanValueTooHigh() throws ValidationFailedException {
        AttributeValidator.shouldBeLowerThan(20, 10);
    }

    @Test
    public void shouldBeLowerThanWithValidValue() throws ValidationFailedException {
        int expected = 0, value = AttributeValidator.shouldBeLowerThan(expected, 10);

        assertEquals(expected, value);
    }

    @Test(expected = ValidationFailedException.class)
    public void shouldEqualValueDoesNotEqual() throws ValidationFailedException {
        AttributeValidator.shouldEqual(1, 2);
    }

    @Test
    public void shouldEqualWithValidValue() throws ValidationFailedException {
        AttributeValidator.shouldEqual(1, 1);
    }

    @Test(expected = ValidationFailedException.class)
    public void shouldEqualOrBeHigherThanValueTooLow() throws ValidationFailedException {
        AttributeValidator.shouldEqualOrBeHigherThan(0, 10);
    }

    @Test
    public void shouldEqualOrBeHigherThanWithValidValue() throws ValidationFailedException {
        int expected = 20, value = AttributeValidator.shouldEqualOrBeHigherThan(expected, 10);

        assertEquals(expected, value);
    }

    @Test(expected = ValidationFailedException.class)
    public void shouldEqualOrBeLowerThanValueTooHigh() throws ValidationFailedException {
        AttributeValidator.shouldEqualOrBeLowerThan(20, 10);
    }

    @Test
    public void shouldEqualOrBeLowerThanWithValidValue() throws ValidationFailedException {
        int expected = 0, value = AttributeValidator.shouldEqualOrBeLowerThan(expected, 10);

        assertEquals(expected, value);
    }

    @Test(expected = ValidationFailedException.class)
    public void shouldNotBeBetweenValueIsBetween() throws ValidationFailedException {
        AttributeValidator.shouldNotBeBetween(15, 10, 20);
    }

    @Test
    public void shouldNotBeBetweenWithValidValue() throws ValidationFailedException {
        int expected = 0, value = AttributeValidator.shouldNotBeBetween(expected, 10, 20);

        assertEquals(expected, value);
    }

    @Test(expected = ValidationFailedException.class)
    public void shouldNotEqualValueEquals() throws ValidationFailedException {
        AttributeValidator.shouldNotEqual(1, 1);
    }

    @Test
    public void shouldNotEqualWithValidValue() throws ValidationFailedException {
        AttributeValidator.shouldNotEqual(1, 2);
    }
}
