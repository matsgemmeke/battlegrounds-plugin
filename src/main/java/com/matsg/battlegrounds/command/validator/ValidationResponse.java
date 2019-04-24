package com.matsg.battlegrounds.command.validator;

public class ValidationResponse {

    public static final ValidationResponse PASSED = new ValidationResponse();
    private boolean passed;
    private String message;

    /**
     * Create a failure response
     */
    public ValidationResponse(String message) {
        this.message = message;
        this.passed = false;
    }

    /**
     * Create a success response
     */
    public ValidationResponse() {
        this.passed = true;
    }

    public String getMessage() {
        return message;
    }

    public boolean passed() {
        return passed;
    }
}
