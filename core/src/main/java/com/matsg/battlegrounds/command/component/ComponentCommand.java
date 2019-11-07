package com.matsg.battlegrounds.command.component;

import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.command.Command;

public abstract class ComponentCommand extends Command {

    public ComponentCommand(Translator translator) {
        super(translator);
    }

    /**
     * Executes the component command with the context in which the component should be added.
     *
     * @param context The context in which the component should be added.
     * @param componentId The first available component id of the arena.
     * @param args The given command arguments.
     */
    public abstract void execute(ComponentContext context, int componentId, String[] args);
}
