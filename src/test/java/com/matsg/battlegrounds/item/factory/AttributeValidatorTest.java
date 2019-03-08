package com.matsg.battlegrounds.item.factory;

import org.junit.Test;

import static org.junit.Assert.*;

public class AttributeValidatorTest {

    @Test
    public void testAttributeValidatorConstructor() {
        new AttributeValidator();
    }

    @Test(expected = ValidationFailedException.class)
    public void testShouldBeBetweenValueTooLow() throws ValidationFailedException {
        AttributeValidator.shouldBeBetween(0, 10, 20);
    }

    @Test(expected = ValidationFailedException.class)
    public void testShouldBeBetweenValueTooHigh() throws ValidationFailedException {
        AttributeValidator.shouldBeBetween(30, 10, 20);
    }

    @Test
    public void testShouldBeBetween() throws ValidationFailedException {
        int expected = 15, value = AttributeValidator.shouldBeBetween(expected, 10, 20);

        assertEquals(expected, value);
    }

    @Test(expected = ValidationFailedException.class)
    public void testShouldBeHigherThanValueTooLow() throws ValidationFailedException {
        AttributeValidator.shouldBeHigherThan(0, 10);
    }

    @Test
    public void testShouldBeHigherThan() throws ValidationFailedException {
        int expected = 20, value = AttributeValidator.shouldBeHigherThan(expected, 10);

        assertEquals(expected, value);
    }

    @Test(expected = ValidationFailedException.class)
    public void testShouldBeHigherThanValueTooHigh() throws ValidationFailedException {
        AttributeValidator.shouldBeLowerThan(20, 10);
    }

    @Test
    public void testShouldBeLowerThan() throws ValidationFailedException {
        int expected = 0, value = AttributeValidator.shouldBeLowerThan(expected, 10);

        assertEquals(expected, value);
    }

    @Test(expected = ValidationFailedException.class)
    public void testShouldEqualValueDoesNotEqual() throws ValidationFailedException {
        AttributeValidator.shouldEqual(1, 2);
    }

    @Test
    public void testShouldEqual() throws ValidationFailedException {
        AttributeValidator.shouldEqual(1, 1);
    }

    @Test(expected = ValidationFailedException.class)
    public void testShouldEqualOrBeHigherThanValueTooLow() throws ValidationFailedException {
        AttributeValidator.shouldEqualOrBeHigherThan(0, 10);
    }

    @Test
    public void testShouldEqualOrBeHigherThan() throws ValidationFailedException {
        int expected = 20, value = AttributeValidator.shouldEqualOrBeHigherThan(expected, 10);

        assertEquals(expected, value);
    }

    @Test(expected = ValidationFailedException.class)
    public void testShouldEqualOrBeLowerThanValueTooHigh() throws ValidationFailedException {
        AttributeValidator.shouldEqualOrBeLowerThan(20, 10);
    }

    @Test
    public void testShouldEqualOrBeLowerThan() throws ValidationFailedException {
        int expected = 0, value = AttributeValidator.shouldEqualOrBeLowerThan(expected, 10);

        assertEquals(expected, value);
    }

    @Test(expected = ValidationFailedException.class)
    public void testShouldNotBeBetweenValueIsBetween() throws ValidationFailedException {
        AttributeValidator.shouldNotBeBetween(15, 10, 20);
    }

    @Test
    public void testShouldNotBeBetween() throws ValidationFailedException {
        int expected = 0, value = AttributeValidator.shouldNotBeBetween(expected, 10, 20);

        assertEquals(expected, value);
    }

    @Test(expected = ValidationFailedException.class)
    public void testShouldNotEqualValueEquals() throws ValidationFailedException {
        AttributeValidator.shouldNotEqual(1, 1);
    }

    @Test
    public void testShouldNotEqual() throws ValidationFailedException {
        AttributeValidator.shouldNotEqual(1, 2);
    }
}
