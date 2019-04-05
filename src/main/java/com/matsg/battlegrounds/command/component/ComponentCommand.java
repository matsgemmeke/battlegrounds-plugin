package com.matsg.battlegrounds.command.component;

public interface ComponentCommand {

    void execute(ComponentContext context, int componentId, String[] args);
}
