package com.matsg.battlegrounds.config;

public class AttributeValidator {

    private double attribute;
    private String attributeName;

    public AttributeValidator(double attribute) {
        this.attribute = attribute;
    }

    public AttributeValidator(double attribute, String attributeName) {
        this.attribute = attribute;
        this.attributeName = attributeName;
    }

    public double shouldBeBetween(double lower, double higher) throws ValidationFailedException {
        return validate(attribute, attribute >= lower && attribute <= higher, "value must be between " + lower + " and " + higher);
    }

    public double shouldBeHigherThan(double number) throws ValidationFailedException {
        return validate(attribute, attribute > number, "value must be higher than " + number);
    }

    public double shouldBeLowerThan(double number) throws ValidationFailedException {
        return validate(attribute, attribute < number, "value must be lower than " + number);
    }

    public double shouldEqual(double number) throws ValidationFailedException {
        return validate(attribute, attribute == number, "value must equal " + number);
    }

    public double shouldEqualOrBeHigherThan(double number) throws ValidationFailedException {
        return validate(attribute, attribute >= number, "value must equal or be higher than " + number);
    }

    public double shouldEqualOrBeLowerThan(double number) throws ValidationFailedException {
        return validate(attribute, attribute <= number, "value must equal or be lower than " + number);
    }

    public double shouldNotBeBetween(double lower, double higher) throws ValidationFailedException {
        return validate(attribute, attribute < lower || attribute > higher, "value must not be between " + lower + " and " + higher);
    }

    public double shouldNotEqual(double number) throws ValidationFailedException {
        return validate(attribute, attribute != number, "value must not equal " + number);
    }

    private double validate(double number, boolean validation, String errorMessage) throws ValidationFailedException {
        if (!validation) {
            throw new ValidationFailedException("Validation for attribute " + attributeName + " failed (" + errorMessage + ")");
        }
        return number;
    }
}