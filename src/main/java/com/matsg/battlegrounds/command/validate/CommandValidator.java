package com.matsg.battlegrounds.command.validate;

import org.bukkit.command.CommandSender;

public interface CommandValidator {

    /**
     * Validates command arguments based on the given input.
     *
     * @param args The given arguments.
     * @return The response of the validator.
     */
    ValidationResponse validate(String[] args);
}
