package com.matsg.battlegrounds.util;

public enum Operator {

    ADDITION("+") {
        public Object apply(Object x1, Object x2) {
            return (((Number) x1).doubleValue() + ((Number) x2).doubleValue());
        }
    },
    DIVISION("/") {
        public Object apply(Object x1, Object x2) {
            return (((Number) x1).doubleValue() / ((Number) x2).doubleValue());
        }
    },
    EQUALIZATION("=") {
        public Object apply(Object x1, Object x2) {
            return x2;
        }
    },
    MULTIPLICATION("*") {
        public Object apply(Object x1, Object x2) {
            return (((Number) x1).doubleValue() * ((Number) x2).doubleValue());
        }
    },
    SUBTRACTION("-") {
        public Object apply(Object x1, Object x2) {
            return (((Number) x1).doubleValue() - ((Number) x2).doubleValue());
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

    public abstract Object apply(Object x1, Object x2);

    public String toString() {
        return text;
    }
}
