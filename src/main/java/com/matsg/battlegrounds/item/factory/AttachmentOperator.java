package com.matsg.battlegrounds.item.factory;

public enum AttachmentOperator {

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

    AttachmentOperator(String text) {
        this.text = text;
    }

    public static AttachmentOperator fromText(String text) {
        for (AttachmentOperator operator : values()) {
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
