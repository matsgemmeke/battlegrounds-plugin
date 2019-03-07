package com.matsg.battlegrounds.item.factory;

public class AttributeValidator {

    public static <T extends Number> T shouldBeBetween(T attribute, T low, T high) throws ValidationFailedException {
        return validate(attribute, attribute.doubleValue() >= low.doubleValue() && attribute.doubleValue() <= high.doubleValue(), "Value must be between " + low + " and " + high);
    }

    public static <T extends Number> T shouldBeHigherThan(T attribute, T value) throws ValidationFailedException {
        return validate(attribute, attribute.doubleValue() > value.doubleValue(), "Value must be higher than " + value);
    }

    public static <T extends Number> T shouldBeLowerThan(T attribute, T value) throws ValidationFailedException {
        return validate(attribute, attribute.doubleValue() < value.doubleValue(), "Value must be lower than " + value);
    }

    public static <T> T shouldEqual(T attribute, T value) throws ValidationFailedException {
        return validate(attribute, attribute == value, "Value must equal " + value);
    }

    public static <T extends Number> T shouldEqualOrBeHigherThan(T attribute, T value) throws ValidationFailedException {
        return validate(attribute, attribute.doubleValue() >= value.doubleValue(), "Value must equal or be higher than " + value);
    }

    public static <T extends Number> T shouldEqualOrBeLowerThan(T attribute, T value) throws ValidationFailedException {
        return validate(attribute, attribute.doubleValue() <= value.doubleValue(), "Value must equal or be lower than " + value);
    }

    public static <T extends Number> T shouldNotBeBetween(T attribute, T low, T high) throws ValidationFailedException {
        return validate(attribute, attribute.doubleValue() < low.doubleValue() || attribute.doubleValue() > high.doubleValue(), "Value must not be between " + low + " and " + high);
    }

    public static <T> T shouldNotEqual(T attribute, T value) throws ValidationFailedException {
        return validate(attribute, attribute != value, "Value must not equal " + value);
    }

    private static <T> T validate(T attribute, boolean validation, String errorMessage) throws ValidationFailedException {
        if (!validation) {
            throw new ValidationFailedException("Validation for attribute failed (" + errorMessage + ")");
        }
        return attribute;
    }
}
