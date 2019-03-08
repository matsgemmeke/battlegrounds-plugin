package com.matsg.battlegrounds.api.util;

public class Placeholder {

    private final String identifier, value;

    public Placeholder(String identifier, double value) {
        this.identifier = identifier;
        this.value = String.valueOf(value);
    }

    public Placeholder(String identifier, int value) {
        this.identifier = identifier;
        this.value = String.valueOf(value);
    }

    public Placeholder(String identifier, Object value) {
        this.identifier = identifier;
        this.value = value.toString();
    }

    public Placeholder(String identifier, String value) {
        this.identifier = identifier;
        this.value = value;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public String getValue() {
        return this.value;
    }

    public String replace(String arg) {
        return arg.replaceAll("%" + identifier + "%", value);
    }
}
