package com.matsg.battlegrounds.util;

public enum Operator {

    ADDITION("+") {
        public Number apply(Number x1, Number x2) {
            return x1.doubleValue() + x2.doubleValue();
        }
    },
    DIVISION("/") {
        public Number apply(Number x1, Number x2) {
            return x1.doubleValue() / x2.doubleValue();
        }
    },
    EQUALIZATION("=") {
        public Number apply(Number x1, Number x2) {
            return x2;
        }
    },
    MULTIPLICATION("*") {
        public Number apply(Number x1, Number x2) {
            return x1.doubleValue() * x2.doubleValue();
        }
    },
    SUBTRACTION("-") {
        public Number apply(Number x1, Number x2) {
            return x1.doubleValue() - x2.doubleValue();
        }
    };

    private final String text;

    Operator(String text) {
        this.text = text;
    }

    public static Operator fromText(String text) {
        for (Operator operator : values()) {
            if (operator.text.equals(text)) {
                return operator;
            }
        }
        throw new IllegalArgumentException();
    }

    public abstract Number apply(Number x1, Number x2);

    public String toString() {
        return text;
    }
}
