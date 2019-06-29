package com.matsg.battlegrounds.mode.shared;

public class SpawningResult {

    public static final SpawningResult PASSED = new SpawningResult();
    private boolean passed;
    private String message;

    public SpawningResult(String message) {
        this.message = message;
        this.passed = false;
    }

    private SpawningResult() {
        this.passed = true;
    }

    public String getMessage() {
        return message;
    }

    public boolean passed() {
        return passed;
    }
}
